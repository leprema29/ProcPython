/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.controllers;

import cm.cirt.calldatarecordmanagement.beans.IdenticationBean;
import cm.cirt.calldatarecordmanagement.metier.Variables111;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.grep4j.core.Grep4j;
import org.grep4j.core.model.Profile;
import org.grep4j.core.model.ProfileBuilder;
import org.grep4j.core.options.Option;
import org.grep4j.core.result.GrepResult;
import org.grep4j.core.result.GrepResults;

/**
 *
 * @author Harry Wanki
 */
@ManagedBean
@javax.faces.bean.SessionScoped
public class IdentificationMBean implements Serializable {

    private String telephone = "";
    private String idNumber = "";
    private String name = "";
    private String operator = "";
    private boolean telephoneEnable = true;
    private boolean idNumberEnable = true;
    private boolean nameEnable = true;
    private String mtnBDI = "/home/data/mtn/backupCSV/Identification_database/bdi.txt";
    private String ocmBDI = "/home/data/orange/backupCSV/BDI/bdi.csv";
    private String nexttelBDI = "/home/data/nexttel/nexttel_cdr_data/processed/identification/bdi.csv";
    private List<IdenticationBean> allSubscribers = new ArrayList<>();
    private List<String> testAll = new ArrayList<>();

    /**
     * Creates a new instance of IdentificationMBean
     */
    public IdentificationMBean() {
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTelephoneEnable() {
        return telephoneEnable;
    }

    public void setTelephoneEnable(boolean telephoneEnable) {
        this.telephoneEnable = telephoneEnable;
    }

    public boolean isIdNumberEnable() {
        return idNumberEnable;
    }

    public void setIdNumberEnable(boolean idNumberEnable) {
        this.idNumberEnable = idNumberEnable;
    }

    public boolean isNameEnable() {
        return nameEnable;
    }

    public void setNameEnable(boolean nameEnable) {
        this.nameEnable = nameEnable;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<IdenticationBean> getAllSubscribers() {
        return allSubscribers;
    }

    public void setAllSubscribers(List<IdenticationBean> allSubscribers) {
        this.allSubscribers = allSubscribers;
    }

    public void phoneDisableOthers() {
        if (this.telephone.equals("")) {
            this.telephoneEnable = true;
            this.idNumberEnable = true;
            this.nameEnable = true;
        } else {
            this.telephoneEnable = true;
            this.idNumberEnable = false;
            this.nameEnable = false;
        }

    }

    public void idNumberDisableOthers() {
        if (this.idNumber.equals("")) {
            this.telephoneEnable = true;
            this.idNumberEnable = true;
            this.nameEnable = true;
        } else {
            this.telephoneEnable = false;
            this.idNumberEnable = true;
            this.nameEnable = false;
        }

    }

    public void nameDisableOthers() {
        if (this.name.equals("")) {
            this.telephoneEnable = true;
            this.idNumberEnable = true;
            this.nameEnable = true;
        } else {
            this.telephoneEnable = false;
            this.idNumberEnable = false;
            this.nameEnable = true;
        }
    }

    public void findIdentification() throws IOException {
//        this.allSubscribers = new ArrayList<>();
        this.allSubscribers = new ArrayList<>();
        if (!this.telephone.equals("")) {
            String op = getOperatorByTelephone(this.telephone.replace("-", ""));
            if (op.equals("CAMTEL")) {

            } else if (op.equals("Orange")) {
                findIdentificationOcmByPhone();
            } else if (op.equals("Mtn")) {
                findIdentificationMtnByPhone();
            } else if (op.equals("Nexttel")) {
                findIdentificationNexttelByPhone();
            }
        } else if (!this.name.equals("")) {
            findIdentificationOcmByName();
            findIdentificationMtnByName();
            findIdentificationNexttelByName();
        } else if (!this.idNumber.equals("")) {
            findIdentificationOcmByNumeroPiece();
            findIdentificationMtnByNumeroPiece();
            findIdentificationNexttelByNumeroPiece();
        }
//        System.out.println("All Taille = " + this.allSubscribers.size());
//        System.out.println("All Taille = " + this.testAll.size());
    }

    public void findIdentificationOcmByPhone() throws IOException {
        List<String> all = sendRequest(this.ocmBDI, this.telephone.replace("-", ""));
        IdenticationBean ib;
        DateFormat df = new SimpleDateFormat(("yyyy-MM-dd"));
        for (String chaine : all) {
            if (chaine != null) {
                ib = new IdenticationBean();
//                System.out.println(chaine);
                chaine = chaine.replace("|", ";");
                System.out.println(chaine);
                String[] tab = chaine.split(";");
                if (tab.length >= 16) {
                    ib.setTelephone(tab[0]);
                    ib.setCni(tab[2]);
                    ib.setName(tab[3]);
                    try {
                        if ((!tab[4].equals("")) && (!tab[4].contains("null"))) {
                            ib.setBirthday(df.parse(tab[4]));
                        }
                        if ((!tab[5].equals("")) && (!tab[5].contains("null"))) {
                            ib.setExpireDate(df.parse(tab[5]));
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(IdentificationMBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ib.setAddress(tab[6]);
                    ib.setStatus(tab[16]);
                    ib.setOperator("Orange");
                    if (ib.getTelephone().equals(this.telephone.replace("-", ""))) {
                        this.allSubscribers.add(ib);
                    }
                }
            }
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
//        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        if (date != null) {
            return dt.format(date);
        } else {
            return "";
        }
    }

    public List<String> sendRequest(String filePath, String chaine) {
        List<String> retour = new ArrayList<>();
        Profile remoteProfile = ProfileBuilder.newBuilder().
                name("dictionary.txt").
                filePath(filePath).
                onRemotehost(Variables111.MTN_HOST).
                credentials(Variables111.MTN_USER, Variables111.MTN_PASSWORD).build();
        GrepResults results = Grep4j.grep(Grep4j.constantExpression(chaine), remoteProfile, Option.ignoreCase());
        for (GrepResult result : results) {
//            retour.add(result.getText());
            try {
                InputStream is = new ByteArrayInputStream(result.getText().getBytes());
                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
//                    System.out.println(line);
//                    System.out.println(". ");
                    retour.add(line);
                }
            } catch (Exception ex) {

            }
        }
        return retour;
    }

    private void findIdentificationMtnByPhone() {
        List<String> all = sendRequest(this.mtnBDI, this.telephone.replace("-", ""));
        IdenticationBean ib;
        DateFormat df = new SimpleDateFormat(("dd-MMM-yy"));
        for (String chaine : all) {
            if (chaine != null) {
                ib = new IdenticationBean();
                String[] tab = chaine.split(",");
                if (tab.length >= 13) {
                    ib.setTelephone(tab[0]);
                    ib.setCni(tab[1]);
                    ib.setName(tab[3]);
                    try {
                        if ((!tab[4].equals("")) && (!tab[4].contains("null"))) {
                            ib.setBirthday(df.parse(tab[4]));
                        }
                        if ((!tab[5].equals("")) && (!tab[5].contains("null"))) {
                            ib.setExpireDate(df.parse(tab[5]));
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(IdentificationMBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ib.setAddress(tab[6]);
                    ib.setStatus(tab[13]);
                    ib.setOperator("MTN");
                    if (ib.getTelephone().equals(this.telephone.replace("-", ""))) {
                        this.allSubscribers.add(ib);
                    }
                }
            }
        }
    }

    private void findIdentificationOcmByName() {
        List<String> all = sendRequest(this.ocmBDI, this.name);
        IdenticationBean ib;
        DateFormat df = new SimpleDateFormat(("yyyy-MM-dd"));
        for (String chaine : all) {
            if (chaine != null) {
                ib = new IdenticationBean();
//            System.out.println(chaine);
                chaine = chaine.replace("|", ";");
//            System.out.println(chaine);
                String[] tab = chaine.split(";");
//                System.out.println("Taille chaine = " + tab.length);
                if (tab.length >= 16) {
                    ib.setTelephone(tab[0]);
                    ib.setCni(tab[2]);
                    ib.setName(tab[3]);
                    try {
                        if ((!tab[4].equals("")) && (!tab[4].contains("null"))) {
                            ib.setBirthday(df.parse(tab[4]));
                        }
                        if ((!tab[5].equals("")) && (!tab[5].contains("null"))) {
                            ib.setExpireDate(df.parse(tab[5]));
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(IdentificationMBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ib.setAddress(tab[6]);
                    ib.setStatus(tab[16]);
                    ib.setOperator("Orange");
                    this.allSubscribers.add(ib);
//                    this.testAll.add(chaine);
//                    System.out.println(chaine);
                }
            }
        }
    }

    private void findIdentificationMtnByName() {
        List<String> all = sendRequest(this.mtnBDI, this.name);
        IdenticationBean ib;
        DateFormat df = new SimpleDateFormat(("dd-MMM-yy"));
        for (String chaine : all) {
            if (chaine != null) {
                ib = new IdenticationBean();
//                System.out.println(chaine);
                String[] tab = chaine.split(",");
                if (tab.length >= 13) {
                    ib.setTelephone(tab[0]);
                    ib.setCni(tab[1]);
                    ib.setName(tab[3]);
                    try {
                        if ((!tab[4].equals("")) && (!tab[4].contains("null"))) {
                            ib.setBirthday(df.parse(tab[4]));
                        }
                        if ((!tab[5].equals("")) && (!tab[5].contains("null"))) {
                            ib.setExpireDate(df.parse(tab[5]));
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(IdentificationMBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ib.setAddress(tab[6]);
                    ib.setStatus(tab[13]);
                    ib.setOperator("MTN");
                    this.allSubscribers.add(ib);
                }
            }
        }
    }

    private void findIdentificationOcmByNumeroPiece() {
        List<String> all = sendRequest(this.ocmBDI, this.idNumber);
        IdenticationBean ib;
        DateFormat df = new SimpleDateFormat(("yyyy-MM-dd"));
        for (String chaine : all) {
            if (chaine != null) {
                ib = new IdenticationBean();
//                System.out.println(chaine);
                chaine = chaine.replace("|", ";");
//                System.out.println(chaine);
                String[] tab = chaine.split(";");
                if (tab.length >= 16) {
                    ib.setTelephone(tab[0]);
                    ib.setCni(tab[2]);
                    ib.setName(tab[3]);
                    try {
                        if ((!tab[4].equals("")) && (!tab[4].contains("null"))) {
                            ib.setBirthday(df.parse(tab[4]));
                        }
                        if ((!tab[5].equals("")) && (!tab[5].contains("null"))) {
                            ib.setExpireDate(df.parse(tab[5]));
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(IdentificationMBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ib.setAddress(tab[6]);
                    ib.setStatus(tab[16]);
                    ib.setOperator("Orange");
                    if (ib.getCni().equals(this.idNumber)) {
                        this.allSubscribers.add(ib);
                    }
                }
            }
        }
    }

    private void findIdentificationMtnByNumeroPiece() {
        List<String> all = sendRequest(this.mtnBDI, this.idNumber);
        IdenticationBean ib;
        DateFormat df = new SimpleDateFormat(("dd-MMM-yy"));
        for (String chaine : all) {
            if (chaine != null) {
                ib = new IdenticationBean();
//                System.out.println(chaine);
                String[] tab = chaine.split(",");
                if (tab.length >= 13) {
                    ib.setTelephone(tab[0]);
                    ib.setCni(tab[1]);
                    ib.setName(tab[3]);
                    try {
                        if ((!tab[4].equals("")) && (!tab[4].contains("null"))) {
                            ib.setBirthday(df.parse(tab[4]));
                        }
                        if ((!tab[5].equals("")) && (!tab[5].contains("null"))) {
                            ib.setExpireDate(df.parse(tab[5]));
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(IdentificationMBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ib.setAddress(tab[6]);
                    ib.setStatus(tab[13]);
                    ib.setOperator("MTN");
                    if (ib.getCni().equals(this.idNumber)) {
                        this.allSubscribers.add(ib);
                    }
                }
            }
        }
    }

    private void findIdentificationNexttelByPhone() {
        List<String> all = sendRequest(this.nexttelBDI, this.telephone.replace("-", ""));
        IdenticationBean ib;
        DateFormat df = new SimpleDateFormat(("dd/MM/yy"));
        for (String chaine : all) {
            if (chaine != null) {
                ib = new IdenticationBean();
//                System.out.println(chaine);
                String[] tab = chaine.split(",");
                if (tab.length >= 11) {
                    ib.setTelephone(tab[0]);
                    ib.setCni(tab[8]);
                    ib.setName(tab[3] + " " + tab[4]);
                    try {
                        if ((!tab[5].equals("")) && (!tab[5].contains("null"))) {
                            ib.setBirthday(df.parse(tab[5]));
                        }
                        if ((!tab[10].equals("")) && (!tab[10].contains("null"))) {
                            ib.setExpireDate(df.parse(tab[10]));
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(IdentificationMBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ib.setAddress(tab[11]);
                    ib.setStatus("");
                    ib.setOperator("Nexttel");
                    if (ib.getTelephone().equals(this.telephone.replace("-", ""))) {
                        this.allSubscribers.add(ib);
                    }
                }
            }
        }
    }

    private void findIdentificationNexttelByName() {
        List<String> all = sendRequest(this.nexttelBDI, this.name);
        IdenticationBean ib;
        DateFormat df = new SimpleDateFormat(("dd/MM/yy"));
        for (String chaine : all) {
            if (chaine != null) {
                ib = new IdenticationBean();
//                System.out.println(chaine);
                String[] tab = chaine.split(",");
                if (tab.length >= 11) {
                    ib.setTelephone(tab[0]);
                    ib.setCni(tab[8]);
                    ib.setName(tab[3] + " " + tab[4]);
                    try {
                        if ((!tab[5].equals("")) && (!tab[5].contains("null"))) {
                            ib.setBirthday(df.parse(tab[5]));
                        }
                        if ((!tab[10].equals("")) && (!tab[10].contains("null"))) {
                            ib.setExpireDate(df.parse(tab[10]));
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(IdentificationMBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ib.setAddress(tab[11]);
                    ib.setStatus("");
                    ib.setOperator("Nexttel");
                    this.allSubscribers.add(ib);
                }
            }
        }
    }

    private void findIdentificationNexttelByNumeroPiece() {
        List<String> all = sendRequest(this.nexttelBDI, this.idNumber);
        IdenticationBean ib;
        DateFormat df = new SimpleDateFormat(("dd/MM/yy"));
        for (String chaine : all) {
            if (chaine != null) {
                ib = new IdenticationBean();
//                System.out.println(chaine);
                String[] tab = chaine.split(",");
                if (tab.length >= 11) {
                    ib.setTelephone(tab[0]);
                    ib.setCni(tab[8]);
                    ib.setName(tab[3] + " " + tab[4]);
                    try {
                        if ((!tab[5].equals("")) && (!tab[5].contains("null"))) {
                            ib.setBirthday(df.parse(tab[5]));
                        }
                        if ((!tab[10].equals("")) && (!tab[10].contains("null"))) {
                            ib.setExpireDate(df.parse(tab[10]));
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(IdentificationMBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ib.setAddress(tab[11]);
                    ib.setStatus("");
                    ib.setOperator("Nexttel");
                    if (ib.getCni().equals(this.idNumber)) {
                        this.allSubscribers.add(ib);
                    }
                }
            }
        }
    }
}
