/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.controllers;

import cm.cirt.calldatarecordmanagement.abou.ExecuteShell;
import cm.cirt.calldatarecordmanagement.beans.CirtLog;
import cm.cirt.calldatarecordmanagement.beans.Telephones;
import cm.cirt.calldatarecordmanagement.entities.Requisitions;
import cm.cirt.calldatarecordmanagement.entities.Users;
import cm.cirt.calldatarecordmanagement.interfaces.IRequisitions;
import cm.cirt.calldatarecordmanagement.managers.RequisitionsManager;
import cm.cirt.calldatarecordmanagement.metier.MtnCsvToXlsx;
import cm.cirt.calldatarecordmanagement.metier.MtnCsvToXlsxMultiple;
import cm.cirt.calldatarecordmanagement.metier.NexttelCsvToXlsx;
import cm.cirt.calldatarecordmanagement.metier.NexttelCsvToXlsxMultiple;
import cm.cirt.calldatarecordmanagement.metier.OrangeCsvToXlsx;
import cm.cirt.calldatarecordmanagement.metier.OrangeCsvToXlsxMultiple;
import cm.cirt.calldatarecordmanagement.metier.Security;
import cm.cirt.calldatarecordmanagement.metier.SendMail;
import cm.cirt.calldatarecordmanagement.metier.Variables;
import cm.cirt.calldatarecordmanagement.metier.Variables111;
import static cm.cirt.calldatarecordmanagement.metier.Variables111.AUTH_KEY;
import cm.cirt.calldatarecordmanagement.metier.ZipUtils;
import com.itextpdf.text.DocumentException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import javax.inject.Named;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

/**
 *
 * @author Harry Wanki
 */
@ManagedBean
@javax.faces.bean.SessionScoped
public class ListingMultipleMBean implements Serializable {

    private IRequisitions requisitionsManager;

    private Date beginDate;
    private Date endDate;
    private String telephone = "";
    private String goodPhone;
    private String imei = "";
    private boolean telephoneEnable = true;
    private boolean imeiEnable = true;
    private List<String> allFilesName;
    private static Session session = null;
    private static Channel channel = null;
    private static ChannelSftp channelSftp = null;
    private List<String> inputPhones;
    private String allPhones;
    private Integer numbersOfPhones;
    private String dateRequisitionString;
    private String demandeurRequisition;
    private String numeroRequisition;

    private HashSet<String> whiteListNumber;
    private HashSet<String> whiteListUserId;

    
    public String getNumeroRequisition() {
        return numeroRequisition;
    }

    public void setNumeroRequisition(String numeroRequisition) {
        this.numeroRequisition = numeroRequisition;
    }

    public Integer getNumbersOfPhones() {
        return numbersOfPhones;
    }

    public void setNumbersOfPhones(Integer numbersOfPhones) {
        this.numbersOfPhones = numbersOfPhones;
    }

    public String getAllPhones() {
        return allPhones;
    }

    public void setAllPhones(String allPhones) {
        this.allPhones = allPhones;
    }

    public String getDemandeurRequisition() {
        return demandeurRequisition;
    }

    public void setDemandeurRequisition(String demandeurRequisition) {
        this.demandeurRequisition = demandeurRequisition;
    }

    public String getDateRequisitionString() {
        return dateRequisitionString;
    }

    public void setDateRequisitionString(String dateRequisitionString) {
        this.dateRequisitionString = dateRequisitionString;
    }

    public List<String> getInputPhones() {
        return inputPhones;
    }

    public void setInputPhones(List<String> inputPhones) {
        this.inputPhones = inputPhones;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getGoodPhone() {
        return goodPhone;
    }

    public void setGoodPhone(String goodPhone) {
        this.goodPhone = goodPhone;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public boolean isTelephoneEnable() {
        return telephoneEnable;
    }

    public void setTelephoneEnable(boolean telephoneEnable) {
        this.telephoneEnable = telephoneEnable;
    }

    public boolean isImeiEnable() {
        return imeiEnable;
    }

    public void setImeiEnable(boolean imeiEnable) {
        this.imeiEnable = imeiEnable;
    }

    public List<String> getAllFilesName() {
        return allFilesName;
    }

    public void setAllFilesName(List<String> allFilesName) {
        this.allFilesName = allFilesName;
    }

    /**
     * Creates a new instance of ListingMBean
     */
    public ListingMultipleMBean() {
    }
//

    @PostConstruct
    public void init() {
        inputPhones = new ArrayList<>();
        inputPhones.add("");
        this.requisitionsManager = new RequisitionsManager();
        this.demandeurRequisition = "";
        if (Variables111.ACCOUNT_TYPE.equals("SED")) {
            this.demandeurRequisition = "SED";
        } else if (Variables111.ACCOUNT_TYPE.equals("DGSN-CAB")) {
            this.demandeurRequisition = "DGSN-CAB";
        } else if (Variables111.ACCOUNT_TYPE.equals("DSP")) {
            this.demandeurRequisition = "DSP";
        } else if (Variables111.ACCOUNT_TYPE.equals("BIR")) {
            this.demandeurRequisition = "BIR";
        } else {
            this.demandeurRequisition = "";
        }
    }

    public void addPhoneInputField() {
        this.inputPhones.add("");
    }

    public void findListing() throws IOException {
        createWhiteList();
        Date dateRequisition = new Date();
        String whitePhone = "";
        Requisitions requisitionMtn = new Requisitions();
        Requisitions requisitionOrange = new Requisitions();
        Requisitions requisitionNexttel = new Requisitions();
        Requisitions requisition = new Requisitions();
        Users loggedUser = new Users();
        List<Requisitions> listRequisitions = new ArrayList<>();
//        Users loggedUser = (Users) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AUTH_KEY);
//        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = "";
//        if (ipAddress == null) {
//            ipAddress = request.getRemoteAddr();
//        }
//        String macAddress = getClientMACAddress(ipAddress);
        boolean isValid = false;
        String operateur = "";
        FacesMessage msg = null;
        HashSet<String> orangePhones = new HashSet<>();
        HashSet<String> mtnPhones = new HashSet<>();
        HashSet<String> nexttelPhones = new HashSet<>();

        for (String tel : this.inputPhones) {
            tel = tel.replace("-", "");
            String op = getOperatorByTelephone(tel);
            if (this.whiteListNumber.contains(tel)) {
                whitePhone = whitePhone + ", " + tel;
            }
            if (op.equals("Orange")) {
                orangePhones.add(tel);
            } else if (op.equals("Mtn")) {
                mtnPhones.add(tel);
            } else if (op.equals("Nexttel")) {
                nexttelPhones.add(tel);
            }
        }
        whitePhone = whitePhone.replaceFirst(", ", "");
        System.out.println("White phone = " + whitePhone);

        for (String tel : this.inputPhones) {
            if (!tel.equals("")) {
                isValid = true;
            }
        }

        if (isValid) {
            if ((this.whiteListUserId.contains(loggedUser.getId() + "")) || (whitePhone.equals(""))) {
                if ((this.beginDate.before(this.endDate)) || (this.beginDate.equals(this.endDate))) {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");
                    String dateDebut = df.format(this.beginDate);
                    String dateFin = df.format(this.endDate);

                    System.out.println("Operateur = " + operateur);
                    String identification = "true";
                    String[] paramOrange = new String[3];
                    String[] paramMtn = new String[3];
                    String[] paramNexttel = new String[3];
                    String multiplePhones = "";
                    String allOrangePhones = "";
                    String allMtnPhones = "";
                    String allNexttelPhones = "";

                    paramOrange[1] = dateDebut;
                    paramOrange[2] = dateFin;
                    paramMtn[1] = dateDebut;
                    paramMtn[2] = dateFin;
                    paramNexttel[1] = dateDebut;
                    paramNexttel[2] = dateFin;
//                this.goodPhone = this.telephone.replace("-", "");
                    this.dateRequisitionString = df2.format(dateRequisition);

                    ExecuteShell shell = new ExecuteShell(df2.format(dateRequisition), demandeurRequisition);
                    List<Future> futuresList = new ArrayList<>();
                    ExecutorService eservice = Executors.newFixedThreadPool(1);

                    for (String num : orangePhones) {
                        requisition = new Requisitions();
                        requisition.setTelephone(num.replace("-", ""));
                        requisition.setUser(loggedUser);
                        requisition.setDateRequisition(dateRequisition);
                        requisition.setEtat(0);
//                        requisition = this.requisitionsManager.create(requisition);
                        listRequisitions.add(requisition);
                        CirtLog log = new CirtLog(dateRequisition, num.replace("-", ""), beginDate, endDate, ipAddress, "Listing", loggedUser, "");
                        log.createListingLog();
//                        this.requisitionsManager.create(requisition);
                        allOrangePhones = allOrangePhones + " " + num;
                    }
                    allOrangePhones = allOrangePhones.trim();
                    allOrangePhones = "\"" + allOrangePhones + "\"";

                    for (String num : mtnPhones) {
                        requisition = new Requisitions();
                        requisition.setTelephone(num.replace("-", ""));
                        requisition.setUser(loggedUser);
                        requisition.setDateRequisition(dateRequisition);
                        requisition.setEtat(0);
//                        requisition = this.requisitionsManager.create(requisition);
                        listRequisitions.add(requisition);
                        CirtLog log = new CirtLog(dateRequisition, num.replace("-", ""), beginDate, endDate, ipAddress, "Listing", loggedUser, "");
                        log.createListingLog();
                        allMtnPhones = allMtnPhones + " " + num;
                    }
                    allMtnPhones = allMtnPhones.trim();
                    allMtnPhones = "\"" + allMtnPhones + "\"";
                    System.out.println("All MTN phones: " + allMtnPhones);

                    for (String num : nexttelPhones) {
                        requisition = new Requisitions();
                        requisition.setTelephone(num.replace("-", ""));
                        requisition.setUser(loggedUser);
                        requisition.setDateRequisition(dateRequisition);
                        requisition.setEtat(0);
//                        requisition = this.requisitionsManager.create(requisition);
                        listRequisitions.add(requisition);
                        CirtLog log = new CirtLog(dateRequisition, num.replace("-", ""), beginDate, endDate, ipAddress, "Listing", loggedUser, "");
                        log.createListingLog();
                        allNexttelPhones = allNexttelPhones + " " + num;
                    }
                    allNexttelPhones = allNexttelPhones.trim();
                    allNexttelPhones = "\"" + allNexttelPhones + "\"";

                    if (!orangePhones.isEmpty()) {
                        paramOrange[0] = allOrangePhones;
//                    futuresList.add(eservice.submit(new TaskListingMultiple(df2.format(dateRequisition), demandeurRequisition, paramOrange, "Orange")));
                        try {
                            shell.traiter_requisition_Multiple(paramOrange, identification, "Orange");
                        } catch (DocumentException | FileNotFoundException ex) {
                            Logger.getLogger(ListingMBean.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (!mtnPhones.isEmpty()) {
                        paramMtn[0] = allMtnPhones;
//                    futuresList.add(eservice.submit(new TaskListingMultiple(df2.format(dateRequisition), demandeurRequisition, paramMtn, "Mtn")));
                        try {
                            shell.traiter_requisition_Multiple(paramMtn, identification, "Mtn");
                        } catch (DocumentException | FileNotFoundException ex) {
                            Logger.getLogger(ListingMBean.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (!nexttelPhones.isEmpty()) {
                        paramNexttel[0] = allNexttelPhones;
//                    futuresList.add(eservice.submit(new TaskListingMultiple(df2.format(dateRequisition), demandeurRequisition, paramNexttel, "Nexttel")));
                        try {
                            shell.traiter_requisition_Multiple(paramNexttel, identification, "Nexttel");
                        } catch (DocumentException | FileNotFoundException ex) {
                            Logger.getLogger(ListingMBean.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    Object taskResult;
                    for (Future future : futuresList) {
                        try {
                            taskResult = future.get();
                            System.out.println("result " + taskResult);
                        } catch (InterruptedException | ExecutionException e) {
                        }
                    }
                    this.allFilesName = new ArrayList<>();
//                CreateDirectory(df2.format(dateRequisition), "r");
                    CreateDirectory(df2.format(dateRequisition), "requisition");

                    for (String tel : mtnPhones) {
                        if (!tel.equals("")) {
                            MtnCsvToXlsxMultiple mctx = new MtnCsvToXlsxMultiple(tel.replace("-", ""), df2.format(dateRequisition), demandeurRequisition);
                            mctx.generate();
                            this.allFilesName.add(tel.replace("-", "") + ".xlsx");
                            this.allFilesName.add("Requisition_" + tel.replace("-", "") + ".pdf");
                            Path source1 = Paths.get(Variables111.DESTINATION_DOSSIERS + df2.format(dateRequisition) + "/" + tel.replace("-", "") + "/" + tel.replace("-", "") + ".xlsx");
                            Path target1 = Paths.get(Variables111.DESTINATION_DOSSIERS + df2.format(dateRequisition) + "/requisition" + "/" + tel.replace("-", "") + ".xlsx");
                            Files.copy(source1, target1);

                            Path source2 = Paths.get(Variables111.DESTINATION_DOSSIERS + df2.format(dateRequisition) + "/" + tel.replace("-", "") + "/" + "Requisition_" + tel.replace("-", "") + ".pdf");
                            Path target2 = Paths.get(Variables111.DESTINATION_DOSSIERS + df2.format(dateRequisition) + "/requisition" + "/" + "Requisition_" + tel.replace("-", "") + ".pdf");
                            Files.copy(source2, target2);

//                        deleteFolder(df2.format(dateRequisition), "Mtn");
                        }
                    }
//                deleteFolder(df2.format(dateRequisition), "Mtn");
                    for (String tel : orangePhones) {
                        if (!tel.equals("")) {
                            OrangeCsvToXlsxMultiple mctx = new OrangeCsvToXlsxMultiple(tel.replace("-", ""), df2.format(dateRequisition), demandeurRequisition);
                            mctx.generate();
                            this.allFilesName.add(tel.replace("-", "") + ".xlsx");
                            this.allFilesName.add("Requisition_" + tel.replace("-", "") + ".pdf");
                            Path source1 = Paths.get(Variables111.DESTINATION_DOSSIERS + df2.format(dateRequisition) + "/" + tel.replace("-", "") + "/" + tel.replace("-", "") + ".xlsx");
                            Path target1 = Paths.get(Variables111.DESTINATION_DOSSIERS + df2.format(dateRequisition) + "/requisition" + "/" + tel.replace("-", "") + ".xlsx");
                            Files.copy(source1, target1);

                            Path source2 = Paths.get(Variables111.DESTINATION_DOSSIERS + df2.format(dateRequisition) + "/" + tel.replace("-", "") + "/" + "Requisition_" + tel.replace("-", "") + ".pdf");
                            Path target2 = Paths.get(Variables111.DESTINATION_DOSSIERS + df2.format(dateRequisition) + "/requisition" + "/" + "Requisition_" + tel.replace("-", "") + ".pdf");
                            Files.copy(source2, target2);

//                        deleteFolder(df2.format(dateRequisition), "Orange");
                        }
                    }
//                deleteFolder(df2.format(dateRequisition), "Orange");
                    for (String tel : nexttelPhones) {
                        if (!tel.equals("")) {
                            NexttelCsvToXlsxMultiple mctx = new NexttelCsvToXlsxMultiple(tel.replace("-", ""), df2.format(dateRequisition), demandeurRequisition);
                            mctx.generate();
                            this.allFilesName.add(tel.replace("-", "") + ".xlsx");
                            this.allFilesName.add("Requisition_" + tel.replace("-", "") + ".pdf");

                            Path source1 = Paths.get(Variables111.DESTINATION_DOSSIERS + df2.format(dateRequisition) + "/" + tel.replace("-", "") + "/" + tel.replace("-", "") + ".xlsx");
                            Path target1 = Paths.get(Variables111.DESTINATION_DOSSIERS + df2.format(dateRequisition) + "/requisition" + "/" + tel.replace("-", "") + ".xlsx");
                            Files.copy(source1, target1);

                            Path source2 = Paths.get(Variables111.DESTINATION_DOSSIERS + df2.format(dateRequisition) + "/" + tel.replace("-", "") + "/" + "Requisition_" + tel.replace("-", "") + ".pdf");
                            Path target2 = Paths.get(Variables111.DESTINATION_DOSSIERS + df2.format(dateRequisition) + "/requisition" + "/" + "Requisition_" + tel.replace("-", "") + ".pdf");
                            Files.copy(source2, target2);

//                        deleteFolder(df2.format(dateRequisition), "Nexttel");
                        }
                    }

//                    for (Requisitions req : listRequisitions) {
//                        req.setEtat(1);
//                        this.requisitionsManager.update(req);
//                    }
                    if (!mtnPhones.isEmpty()) {
                        deleteFolder(df2.format(dateRequisition), "Mtn");
                    } else if (!orangePhones.isEmpty()) {
                        deleteFolder(df2.format(dateRequisition), "Orange");
                    } else if (!nexttelPhones.isEmpty()) {
                        deleteFolder(df2.format(dateRequisition), "Nexttel");
                    }

                    String sourceFolder = Variables111.DESTINATION_DOSSIERS + df2.format(dateRequisition) + "/requisition";
                    String outZip = Variables111.DESTINATION_DOSSIERS + df2.format(dateRequisition) + "/" + this.demandeurRequisition + "_" + this.numeroRequisition + ".zip";
                    ZipUtils appZip = new ZipUtils(sourceFolder, outZip);
                    appZip.generateFileList(new File(sourceFolder));
                    appZip.zipIt(outZip);
                    System.out.println(this.allFilesName.size());
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal!", "\"Begin Date\" should be less than \"After Date\""));
                }
            } else {
//                White list numbers
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String dateDebut = df.format(this.beginDate);
                String dateFin = df.format(this.endDate);

                String message = "Attempt to print listing of this (these) number(s): " + whitePhone + "\n"
                        + " . User: " + loggedUser.getLogin() + ", " + loggedUser.getName() + "\n"
                        + " . Listing period: " + dateDebut + " - " + dateFin + "\n"
                        + " . Date: " + df.format(new Date()) + "\n"
                        + " . Ip Address: " + ipAddress;
//                SendMail.sendMessage(Variables111.BOSS_EMAIL, Variables111.WHITE_LIST_WARNING_SUBJECT, message);
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal!", "You are not allowed to get the listing of this (these) number(s): " + whitePhone + ". Please, contact ANTIC for further information."));
            }
        } else {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal!", "You should enter at least one telephone number"));
        }
    }

    public Date convertStringToDate(String stringDate) {
        if (!stringDate.equals("")) {
            return Security.getDateFromString(stringDate);
        } else {
            return null;
        }
    }

    public String convertToComponentTimes(String secondes) {
        if (!secondes.equals("")) {
            int d = Integer.parseInt(secondes);
//        BigDecimal bd = new BigDecimal(secondes);
            return Security.splitToComponentTimes(d);
        } else {
            return secondes;
        }
    }

    public String getOperatorByTelephone(String tel) {
        String op = "";
        if ((tel.startsWith("67")) || (tel.startsWith("650")) || (tel.startsWith("651")) || (tel.startsWith("652"))
                || (tel.startsWith("653")) || (tel.startsWith("654")) || (tel.startsWith("680")) || (tel.startsWith("681"))
                || (tel.startsWith("682")) || (tel.startsWith("683")) || (tel.startsWith("684"))) {
            op = "Mtn";
        } else if ((tel.startsWith("69")) || (tel.startsWith("655")) || (tel.startsWith("656")) || (tel.startsWith("657"))
                || (tel.startsWith("658")) || (tel.startsWith("659")) || (tel.startsWith("685")) || (tel.startsWith("686"))
                || (tel.startsWith("687")) || (tel.startsWith("688")) || (tel.startsWith("689"))) {
            op = "Orange";
        } else if (tel.startsWith("66")) {
            op = "Nexttel";
        } else if (tel.startsWith("64")) {
            op = "Nexttel";
        } else if (tel.startsWith("63")) {
            op = "Nexttel";
        } else if (tel.startsWith("62")) {
            op = "Nexttel";
        } else if (tel.startsWith("61")) {
            op = "Nexttel";
        } else if (tel.startsWith("60")) {
            op = "Nexttel";
        } else if (tel.startsWith("2")) {
            op = "Camtel";
        }
        return op;
    }

    public String formatDate(Date date) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date != null) {
            return dt.format(date);
        } else {
            return "";
        }
    }

    public String getTypeFichier(String nomFichier) {
        String retour = "";
        if (nomFichier.endsWith(".pdf")) {
            retour = "pdf.jpg";
        }
        if (nomFichier.endsWith(".xlsx")) {
            retour = "excel.jpg";
        }
        if (nomFichier.endsWith(".xls")) {
            retour = "excel.jpg";
        }
        if (nomFichier.endsWith(".csv")) {
            retour = "excel.jpg";
        }
        return retour;
    }

    public void deleteFolder(String folder, String operator) {
        String SFTPHOST = ""; // SFTP Host Name or SFTP Host IP Address
        int SFTPPORT = 22; // SFTP Port Number
        String SFTPUSER = ""; // User Name
        String SFTPPASS = ""; // Password
        String SFTPWORKINGDIR = "/root/" + folder; // Source Directory on SFTP server in which the file is located on remote server
        boolean deletedflag = false;

        if (operator.equals("Orange")) {
            SFTPHOST = Variables111.ORANGE_HOST;
            SFTPUSER = Variables111.ORANGE_USER;
            SFTPPASS = Variables111.ORANGE_PASSWORD;
        } else if (operator.equals("Mtn")) {
            SFTPHOST = Variables111.MTN_HOST;
            SFTPUSER = Variables111.MTN_USER;
            SFTPPASS = Variables111.MTN_PASSWORD;
        } else if (operator.equals("Nexttel")) {
            SFTPHOST = Variables111.NEXTTEL_HOST;
            SFTPUSER = Variables111.NEXTTEL_USER;
            SFTPPASS = Variables111.NEXTTEL_PASSWORD;
        }
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            //Set Password
String privateKeyPath = Variables.getIdRsaPath(); // Replace with your actual path
jsch.addIdentity(privateKeyPath);
            //session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect(); // Create SFTP Session
            channel = session.openChannel("sftp"); // Open SFTP Channel
            channel.connect();
            channelSftp = (ChannelSftp) channel;

            recursiveFolderDelete(SFTPWORKINGDIR);

            deletedflag = true;
            if (deletedflag) {
                System.out.println("folder deleted");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }

        }
    }

    @SuppressWarnings("unchecked")
    private static void recursiveFolderDelete(String path) throws SftpException {
        channelSftp.cd(path); // Change Directory on SFTP Server
        // List source directory structure.
        Vector<ChannelSftp.LsEntry> fileAndFolderList = channelSftp.ls(path);
        // Iterate objects in the list to get file/folder names.
        for (ChannelSftp.LsEntry item : fileAndFolderList) {
            // If it is a file (not a directory).
            if (!item.getAttrs().isDir()) {
                channelSftp.rm(path + "/" + item.getFilename()); // Remove file.
            } else if (!(".".equals(item.getFilename()) || "..".equals(item.getFilename()))) { // If it is a subdir.
                try {
                    // removing sub directory.
                    channelSftp.rmdir(path + "/" + item.getFilename());
                } catch (Exception e) { // If subdir is not empty and error occurs.
                    // Do lsFolderRemove on this subdir to enter it and clear its contents.
                    recursiveFolderDelete(path + "/" + item.getFilename());
                }
            }
        }
        channelSftp.rmdir(path); // delete the parent directory after empty

    }

    public void CreateDirectory(String dateRequisition, String NameFolder) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();
        System.out.println("date ! " + date);
        File file = new File(Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + NameFolder);
        if (!file.exists()) {
            if (file.mkdirs()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory! " + Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + NameFolder);
            }
        }
    }

    public String getNumeroByFileName(String fileName) {
        String numero;
        if (fileName.startsWith("Requisition_")) {
            numero = fileName.replace("Requisition_", "");
            numero = numero.replace(".pdf", "");
        } else {
            numero = fileName.replace(".xlsx", "");
        }
        return numero;
    }

    public String getClientMACAddress(String clientIp) {
        String str = "";
        String macAddress = "";
        try {
            Process p = Runtime.getRuntime().exec("nbtstat -A " + clientIp);
            InputStreamReader ir = new InputStreamReader(p.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    if (str.indexOf("MAC Address") > 1) {
                        macAddress = str.substring(str.indexOf("MAC Address") + 14, str.length());
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return macAddress;
    }

    public void initField() {
        inputPhones = new ArrayList<>();
        inputPhones.add("");
        this.requisitionsManager = new RequisitionsManager();
        this.allFilesName = new ArrayList<>();
    }

    public void createWhiteList() {
        this.whiteListNumber = new HashSet<>();
        this.whiteListUserId = new HashSet<>();
        File file = new File(Variables111.BASE_FOLDER + "whitelistphone.txt");
        try {
            LineIterator it = FileUtils.lineIterator(file, "UTF-8");
            try {
                while (it.hasNext()) {
                    String phone = it.nextLine();
                    this.whiteListNumber.add(phone);
                }
            } finally {
                LineIterator.closeQuietly(it);
            }
        } catch (IOException ex) {
            Logger.getLogger(ListingMultipleMBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        File file2 = new File(Variables111.BASE_FOLDER + "whitelistprofile.txt");
        try {
            LineIterator it = FileUtils.lineIterator(file2, "UTF-8");
            try {
                while (it.hasNext()) {
                    String in = it.nextLine();
                    this.whiteListUserId.add(in.trim());
                }
            } finally {
                LineIterator.closeQuietly(it);
            }
        } catch (IOException ex) {
            Logger.getLogger(ListingMultipleMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
