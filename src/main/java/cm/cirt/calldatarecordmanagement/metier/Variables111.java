/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cm.cirt.calldatarecordmanagement.metier;

import java.io.File;
import java.io.FileReader;

/**
 *
 * @author User
 */
public class Variables111 {
    public static final String AUTH_KEY = "app.user.name";
    public static final String AUTH_STAT = "app.user.statut";
    
    public static final String  BOSS_EMAIL = "prosper.pagou@cirt.cm";
    public static final String  WANKI_EMAIL = "h.wanki@cirt.cm";
    public static final String  WHITE_LIST_WARNING_SUBJECT = "White List Alert";
    public static final String CHEMIN_DOCUMENTS_OTHERS_PROJECTS = "E:\\";
    
    public static final String  ACCOUNT_TYPE = "NOIR";
    public static final String BASE_FOLDER = "C:/Data/";
    public static final String DESTINATION_FICHIER = "C:/Data/requisitions/";
    public static final String DESTINATION_DOSSIERS = "C:/Data/requisitions/";
    public static final String DESTINATION_LOGS = "C:/Data/logs/";
   
    public static final String ORANGE_HOST = "192.168.1.112";
    public static final String MTN_HOST = "192.168.1.112";
    public static final String NEXTTEL_HOST = "192.168.1.112";
    public static final String ORANGE_USER = "root";
    public static final String MTN_USER = "root";
    public static final String NEXTTEL_USER = "root";
    public static final String ORANGE_PASSWORD = "Cdr,123+:";
    public static final String MTN_PASSWORD = "Cdr,123+:";
    public static final String NEXTTEL_PASSWORD = "Cdr,123+:";
    public static final String SSH_KEY = "~/.ssh/private_key.pem";
    
    private String accountType;
    private String baseFolder;
    private String destinationFichier;
    private String destinationDossiers;
    private String destinationLogs;
    private String orangeHost;
    private String mtnHost;
    private String nexttelHost;
    private String orangeUser;
    private String mtnUser;
    private String nexttelUser;
    private String orangePassword;
    private String mtnPassword;
    private String nexttelPassword;

    public Variables111() {
    }

    public Variables111(String accountType, String baseFolder, String destinationFichier, String destinationDossiers, String destinationLogs, String orangeHost, String mtnHost, String nexttelHost, String orangeUser, String mtnUser, String nexttelUser, String orangePassword, String mtnPassword, String nexttelPassword) {
        this.accountType = accountType;
        this.baseFolder = baseFolder;
        this.destinationFichier = destinationFichier;
        this.destinationDossiers = destinationDossiers;
        this.destinationLogs = destinationLogs;
        this.orangeHost = orangeHost;
        this.mtnHost = mtnHost;
        this.nexttelHost = nexttelHost;
        this.orangeUser = orangeUser;
        this.mtnUser = mtnUser;
        this.nexttelUser = nexttelUser;
        this.orangePassword = orangePassword;
        this.mtnPassword = mtnPassword;
        this.nexttelPassword = nexttelPassword;
    }

    
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBaseFolder() {
        return baseFolder;
    }

    public void setBaseFolder(String baseFolder) {
        this.baseFolder = baseFolder;
    }

    public String getDestinationFichier() {
        return destinationFichier;
    }

    public void setDestinationFichier(String destinationFichier) {
        this.destinationFichier = destinationFichier;
    }

    public String getDestinationDossiers() {
        return destinationDossiers;
    }

    public void setDestinationDossiers(String destinationDossiers) {
        this.destinationDossiers = destinationDossiers;
    }

    public String getDestinationLogs() {
        return destinationLogs;
    }

    public void setDestinationLogs(String destinationLogs) {
        this.destinationLogs = destinationLogs;
    }

    public String getOrangeHost() {
        return orangeHost;
    }

    public void setOrangeHost(String orangeHost) {
        this.orangeHost = orangeHost;
    }

    public String getMtnHost() {
        return mtnHost;
    }

    public void setMtnHost(String mtnHost) {
        this.mtnHost = mtnHost;
    }

    public String getNexttelHost() {
        return nexttelHost;
    }

    public void setNexttelHost(String nexttelHost) {
        this.nexttelHost = nexttelHost;
    }

    public String getOrangeUser() {
        return orangeUser;
    }

    public void setOrangeUser(String orangeUser) {
        this.orangeUser = orangeUser;
    }

    public String getMtnUser() {
        return mtnUser;
    }

    public void setMtnUser(String mtnUser) {
        this.mtnUser = mtnUser;
    }

    public String getNexttelUser() {
        return nexttelUser;
    }

    public void setNexttelUser(String nexttelUser) {
        this.nexttelUser = nexttelUser;
    }

    public String getOrangePassword() {
        return orangePassword;
    }

    public void setOrangePassword(String orangePassword) {
        this.orangePassword = orangePassword;
    }

    public String getMtnPassword() {
        return mtnPassword;
    }

    public void setMtnPassword(String mtnPassword) {
        this.mtnPassword = mtnPassword;
    }

    public String getNexttelPassword() {
        return nexttelPassword;
    }

    public void setNexttelPassword(String nexttelPassword) {
        this.nexttelPassword = nexttelPassword;
    }   

        public static String getBASE_FOLDER(){
      String homeDir = System.getProperty("user.home");
      return homeDir+"/data/";
    }

    public static String getDESTINATION_FICHIER(){
      String homeDir = System.getProperty("user.home");
      return homeDir+"/data/requisitions/";
    }

    public static String getDESTINATION_DOSSIERS(){
      String homeDir = System.getProperty("user.home");
      return homeDir+"/data/requisitions/";
    }

    public static String getDESTINATION_LOGS(){
      String homeDir = System.getProperty("user.home");
      return homeDir+"/data/logs/";
    }

    public static String getDESTINATION_LOGO(){
      String homeDir = System.getProperty("user.home");
      return homeDir+"/data/requisitions/fichier/";
    }

    public static String getIdRsaPath() {
		String homeDir = System.getProperty("user.home");
		String potentialPath = homeDir + "/.ssh/private_key.pem";
		
		if (new File(potentialPath).exists()) {
           // System.out.println("SSH Key===="+new File(potentialPath).getAbsolutePath()); 
             return new File(potentialPath).getAbsolutePath();
		} else {
		  return Variables.SSH_KEY; 
		}
	  }

      public static String getKnowHostPath() {
		String homeDir = System.getProperty("user.home");
		String potentialPath = homeDir + "/.ssh/known_hosts";
		if (new File(potentialPath).exists()) {
		  return potentialPath;
		} else {
		  return Variables.SSH_KEY; 
		}
	  }

      public static String getFileContent(String filePath) {
  StringBuilder content = new StringBuilder();
  try  {
    FileReader reader = new FileReader(filePath);
    int character;
    while ((character = reader.read()) != -1) {
      content.append((char) character);
    }
  } catch(Exception e){
    
  }
  return content.toString();
}

}
