/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.controllers;

import cm.cirt.calldatarecordmanagement.beans.CirtLog;
import cm.cirt.calldatarecordmanagement.entities.Users;
import cm.cirt.calldatarecordmanagement.interfaces.IUsers;
import cm.cirt.calldatarecordmanagement.managers.UsersManager;
import cm.cirt.calldatarecordmanagement.metier.Security;
import static cm.cirt.calldatarecordmanagement.metier.Variables111.AUTH_KEY;
import static cm.cirt.calldatarecordmanagement.metier.Variables111.AUTH_STAT;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
//import org.primefaces.context.RequestContext;

/**
 *
 * @author Harry Wanki
 */
@ManagedBean
@javax.faces.bean.SessionScoped
public class SessionMBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private IUsers usersManager;

    private String login;
    private String password;
    private String key;
    private Users user;
    private Boolean connected = false;
    private Boolean admin = false;
    private boolean allowed;

    private String oldPassword;
    private String newPassword;

    /**
     * Creates a new instance of SessionMBean
     */
    public SessionMBean() {
    }

    public Boolean isAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }
    
    public Boolean isConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String authentication() {
        this.usersManager = new UsersManager();
        Date date = new Date();
//        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        String retour = "#";
        String usernamePattern = "^[a-zA-Z0-9]+$";
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%^&*()<>?/|}{~`]).{8,}$";

        Matcher usernameMatcher = Pattern.compile(usernamePattern).matcher(this.login);
        Matcher passwordMatcher = Pattern.compile(passwordPattern).matcher(this.password);

        if (usernameMatcher.matches() && passwordMatcher.matches()) {
            
//        if ((this.user = this.usersManager.getUserByLoginPassword(this.login, Security.md5(this.password))) != null) {
        if (((this.user = this.usersManager.getUserByLoginPassword(this.login, Security.md5(this.password))) != null) 
        && (this.key.equals(createOneTimePassword()))) {
            this.connected = Boolean.TRUE;
            if ((this.user.getId() == 5) || (this.user.getId() == 6)) {
                this.allowed = Boolean.FALSE;
            } else {
                this.allowed = Boolean.TRUE;
            }
            
            if (((this.user.getId() == 2) && (this.user.getLogin().equals("prosper"))) || ((this.user.getId() == 9)  && (this.user.getLogin().equals("lee")))) {
                this.admin = Boolean.TRUE;
            } else {
                this.admin = Boolean.FALSE;
            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                    AUTH_KEY, this.user);
            msg = new FacesMessage("Welcome, " + this.user.getName(), this.user.getName());
           HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
//            String macAddress = getClientMACAddress(ipAddress);
            CirtLog log = new CirtLog(date, ipAddress, "Login", user, "");
            log.createSessionLog();
            retour = "home.xhtml";
        } else {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteHost();
            }
//            String macAddress = getClientMACAddress(ipAddress);
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed :", this.login);
//            CirtLog cirtLog = new CirtLog(date, "login", "Login failed", ipAddress, user);
//            cirtLog.log();
            CirtLog log = new CirtLog(date, ipAddress, login, "Login failed", "");
            log.createFailedSessionLog();
        }                   
        }else{ //potentialSQL injection
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteHost();
            }
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed :", this.login);

            CirtLog log = new CirtLog(date, ipAddress, login, "Login failed", "");
            log.createFailedSessionLog();
        
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
//        context.update("loginError");
        return retour;
    }

    public String deconnexion() {
        Date date = new Date();
        if (this.user != null) {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
//            String macAddress = getClientMACAddress(ipAddress);
            CirtLog log = new CirtLog(date, ipAddress, "Logout", user, "");
            log.createSessionLog();
            ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                    .getSession(true)).invalidate();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                    .remove(AUTH_KEY);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                    .remove(AUTH_STAT);
            user = null;
        }
        return "home.xhtml";
    }

    public void changePassword() {
        FacesMessage msg = null;
        if (this.user.getPassword().equals(Security.md5(this.oldPassword))) {
            this.user.setPassword(Security.md5(this.newPassword));
            this.usersManager.update(this.user);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Password changed successfully."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal!", "Old password incorrect"));
//            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Old password incorrect", this.login);
        }
    }

    public String createOneTimePassword() {
        String pwd = "";
        Date dateDuJour = new Date();
        SimpleDateFormat dfJour = new SimpleDateFormat("dd");
        SimpleDateFormat dfDay = new SimpleDateFormat("E");
        SimpleDateFormat dfMois = new SimpleDateFormat("MM");
        SimpleDateFormat dfAnnee = new SimpleDateFormat("yy");

        String jour = dfDay.format(dateDuJour);
        String day = dfJour.format(dateDuJour);
        String mois = dfMois.format(dateDuJour);
        String annee = dfAnnee.format(dateDuJour);

        int sommeJour = Integer.parseInt(day.charAt(0) + "") + Integer.parseInt(day.charAt(1) + "");
        while (sommeJour > 9) {
            String sommeString = sommeJour + "";
            sommeJour = Integer.parseInt(sommeString.charAt(0) + "") + Integer.parseInt(sommeString.charAt(1) + "");
        }

        pwd = "" + annee.charAt(1) + annee.charAt(0)
                + sommeJour
                + mois.charAt(1) + mois.charAt(0)
                + day.charAt(1) + day.charAt(0);
        return pwd;
    }
    
    public String createOneTimePassword2() {
        String pwd = "";
        Date dateDuJour = new Date();
        SimpleDateFormat dfJour = new SimpleDateFormat("dd");
        SimpleDateFormat dfDay = new SimpleDateFormat("E");
        SimpleDateFormat dfMois = new SimpleDateFormat("MMM");
        SimpleDateFormat dfAnnee = new SimpleDateFormat("yyyy");

        String jour = dfJour.format(dateDuJour);
        String day = dfDay.format(dateDuJour);
        String mois = dfMois.format(dateDuJour);
        String annee = dfAnnee.format(dateDuJour);

        pwd = "" + jour.charAt(1) + jour.charAt(0)
                + annee.charAt(3) + annee.charAt(2)
                + day.charAt(2)
                + mois.charAt(2) + mois.charAt(0) + mois.charAt(1)
                + day.charAt(1) + day.charAt(0)
                + annee.charAt(0) + annee.charAt(1);
        return pwd;
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
}
