/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.beans;

import cm.cirt.calldatarecordmanagement.entities.Users;
import cm.cirt.calldatarecordmanagement.metier.Variables111;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 *
 * @author Harry Wanki
 */
public class CirtLog implements Serializable {

    private Date timeStamp;
    private String numbers;
    private Date beginDate;
    private Date endDate;
    private String ipAdress;
    private String task;
    private Users user;
    private String login;
    private String macAddress;

    private File outputFile;
    private FileOutputStream fos;
    private BufferedWriter bw;

    public CirtLog(Date timeStamp, String ipAdress, String login, String task, String macAddress) {
        this.timeStamp = timeStamp;
        this.ipAdress = ipAdress;
        this.login = login;
        this.task = task;
        this.macAddress = macAddress;
    }
    
    public CirtLog(Date timeStamp, String ipAdress, String task, Users user, String macAddress) {
        this.timeStamp = timeStamp;
        this.ipAdress = ipAdress;
        this.task = task;
        this.user = user;
        this.macAddress = macAddress;
    }

    public CirtLog(Date timeStamp, String numbers, Date beginDate, Date endDate, String ipAdress, String task, Users user, String macAddress) {
        this.timeStamp = timeStamp;
        this.numbers = numbers;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.ipAdress = ipAdress;
        this.task = task;
        this.user = user;
        this.macAddress = macAddress;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
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

    public String getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public BufferedWriter getBw() {
        return bw;
    }

    public void setBw(BufferedWriter bw) {
        this.bw = bw;
    }

    
    public CirtLog() {
    }

    public void createListingLog() {
        try {
            Properties prop = new Properties();
            prop.load(CirtLog.class.getResourceAsStream("/cirtlog.properties"));
            SimpleDateFormat dt = new SimpleDateFormat(prop.getProperty("cirtlog.appender.date.Format"));
            SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd");
            String chaine = dt.format(timeStamp) + "," + user.getName() + "," + task + "," + numbers + "," + dt2.format(beginDate) + "," + dt2.format(endDate) + "," + ipAdress + "," + macAddress;
            outputFile = new File(Variables111.DESTINATION_LOGS + "/log-" + dt2.format(this.timeStamp) + ".txt");
            if (outputFile.exists()) {
                bw = new BufferedWriter(new FileWriter(outputFile, true));
            } else {
                fos = new FileOutputStream(outputFile);
                bw = new BufferedWriter(new OutputStreamWriter(fos));
            }
            bw.write(chaine);
            bw.newLine();
            bw.close();
        } catch (IOException io) {

        }
    }
    
    public void createSessionLog() {
        try {
            Properties prop = new Properties();
            prop.load(CirtLog.class.getResourceAsStream("/cirtlog.properties"));
            SimpleDateFormat dt = new SimpleDateFormat(prop.getProperty("cirtlog.appender.date.Format"));
            SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd");
            String chaine = dt.format(timeStamp) + "," + user.getName() + "," + task + "," + ipAdress + "," + macAddress;
            outputFile = new File(Variables111.DESTINATION_LOGS + "/session-" + dt2.format(this.timeStamp) + ".txt");
            if (outputFile.exists()) {
                bw = new BufferedWriter(new FileWriter(outputFile, true));
            } else {
                fos = new FileOutputStream(outputFile);
                bw = new BufferedWriter(new OutputStreamWriter(fos));
            }
            bw.write(chaine);
            bw.newLine();
            bw.close();
        } catch (IOException io) {

        }
    }
    
    public void createFailedSessionLog() {
        try {
            Properties prop = new Properties();
            prop.load(CirtLog.class.getResourceAsStream("/cirtlog.properties"));
            SimpleDateFormat dt = new SimpleDateFormat(prop.getProperty("cirtlog.appender.date.Format"));
            SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd");
            String chaine = dt.format(timeStamp) + "," + this.login + "," + task + "," + ipAdress + "," + macAddress;
            outputFile = new File(Variables111.DESTINATION_LOGS + "/session-" + dt2.format(this.timeStamp) + ".txt");
            if (outputFile.exists()) {
                bw = new BufferedWriter(new FileWriter(outputFile, true));
            } else {
                fos = new FileOutputStream(outputFile);
                bw = new BufferedWriter(new OutputStreamWriter(fos));
            }
            bw.write(chaine);
            bw.newLine();
            bw.close();
        } catch (IOException io) {

        }
    }
}
