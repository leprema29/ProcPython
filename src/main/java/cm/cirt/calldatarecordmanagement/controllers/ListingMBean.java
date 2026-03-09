/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.controllers;

import cm.cirt.calldatarecordmanagement.abou.ExecuteShell;
import cm.cirt.calldatarecordmanagement.beans.Telephones;
import cm.cirt.calldatarecordmanagement.entities.Requisitions;
import cm.cirt.calldatarecordmanagement.entities.Users;
import cm.cirt.calldatarecordmanagement.interfaces.IRequisitions;
import cm.cirt.calldatarecordmanagement.managers.RequisitionsManager;
import cm.cirt.calldatarecordmanagement.metier.MtnCsvToXlsx;
import cm.cirt.calldatarecordmanagement.metier.NexttelCsvToXlsx;
import cm.cirt.calldatarecordmanagement.metier.OrangeCsvToXlsx;
import cm.cirt.calldatarecordmanagement.metier.Security;
import cm.cirt.calldatarecordmanagement.metier.Variables;
import cm.cirt.calldatarecordmanagement.metier.Variables111;
import static cm.cirt.calldatarecordmanagement.metier.Variables111.AUTH_KEY;
import com.itextpdf.text.DocumentException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author Harry Wanki
 */
@ManagedBean
@javax.faces.bean.SessionScoped
public class ListingMBean implements Serializable {

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
    private List<Telephones> inputPhones;
    private String dateRequisitionString;

    public String getDateRequisitionString() {
        return dateRequisitionString;
    }

    public void setDateRequisitionString(String dateRequisitionString) {
        this.dateRequisitionString = dateRequisitionString;
    }

    public List<Telephones> getInputPhones() {
        return inputPhones;
    }

    public void setInputPhones(List<Telephones> inputPhones) {
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
    public ListingMBean() {
    }
//

    @PostConstruct
    public void init() {
        inputPhones = new ArrayList<>();
        Telephones phone = new Telephones();
        phone.setId(1);
        this.inputPhones.add(phone);
        this.requisitionsManager = new RequisitionsManager();
    }

    public void addPhoneInputField(int position) {
        Telephones tel = this.inputPhones.get(position);
        if (tel.getNumero().equals("")) {
            if (position == 0) {
                if (this.inputPhones.size() > 1) {
                    this.inputPhones.remove(position);
                }
            } else {
                this.inputPhones.remove(position);
            }
        } else {
            Telephones phone = new Telephones();
            int actualSize = this.inputPhones.size();
            phone.setId(actualSize + 1);
            phone.setNumero("");
            this.inputPhones.add(phone);
        }

    }

    public void phoneDisableOthers() {
        if (this.telephone.equals("")) {
            this.telephoneEnable = true;
            this.imeiEnable = true;
        } else {
            this.telephoneEnable = true;
            this.imeiEnable = false;
        }

    }

    public void imeiDisableOthers() {
        if (this.imei.equals("")) {
            this.telephoneEnable = true;
            this.imeiEnable = true;
        } else {
            this.telephoneEnable = false;
            this.imeiEnable = true;
        }

    }

    public void findListing() throws IOException {
        Date dateRequisition = new Date();
        Requisitions requisition = new Requisitions();
        Users loggedUser = (Users) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AUTH_KEY);
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateDebut = df.format(this.beginDate);
        String dateFin = df.format(this.endDate);
        String operateur = getOperatorByTelephone(this.telephone.replace("-", ""));
//        String operateur = "Orange";
        System.out.println("Operateur = " + operateur);
        String identification = "true";
        String[] param = new String[3];
        param[0] = this.telephone.replace("-", "");
        param[1] = dateDebut;
        param[2] = dateFin;
        this.goodPhone = this.telephone.replace("-", "");
        this.dateRequisitionString = df2.format(dateRequisition);
        requisition.setTelephone(this.goodPhone);
        requisition.setUser(loggedUser);
        requisition.setDateRequisition(dateRequisition);
        this.requisitionsManager.create(requisition);
        ExecuteShell shell = new ExecuteShell(df2.format(dateRequisition));
        try {
            shell.traiter_requisition(param, identification, operateur);
        } catch (DocumentException | FileNotFoundException ex) {
            Logger.getLogger(ListingMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (operateur.equals("Mtn")) {
            MtnCsvToXlsx mctx = new MtnCsvToXlsx(this.goodPhone, df2.format(dateRequisition));
            mctx.generate();
        } else if (operateur.equals("Orange")) {
            OrangeCsvToXlsx octx = new OrangeCsvToXlsx(this.goodPhone, df2.format(dateRequisition));
            octx.generate();
        } else if (operateur.equals("Nexttel")) {
            NexttelCsvToXlsx octx = new NexttelCsvToXlsx(this.goodPhone, df2.format(dateRequisition));
            octx.generate();
        }

        this.allFilesName = new ArrayList<>();
        this.allFilesName.add(this.goodPhone + ".xlsx");
        this.allFilesName.add("Requisition_" + this.goodPhone + ".pdf");
        deleteFolder(this.goodPhone, operateur);
        System.out.println(this.allFilesName.size());
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
            //session.setPassword(SFTPPASS);
            //Set Password
String privateKeyPath = Variables.getIdRsaPath(); // Replace with your actual path
jsch.addIdentity(privateKeyPath);
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
}
