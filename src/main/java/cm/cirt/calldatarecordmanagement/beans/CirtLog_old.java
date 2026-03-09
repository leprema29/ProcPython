/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.beans;

import cm.cirt.calldatarecordmanagement.entities.Users;
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
public class CirtLog_old implements Serializable {

    private Date timeStamp;
    private String task;
    private String taskDescription;
    private String ipAdress;
    private Users user;

    private File outputFile;
    private FileOutputStream fos;
    private BufferedWriter bw;

    public CirtLog_old() {
    }

    public CirtLog_old(Date timeStamp, String task, String taskDescription, String ipAdress, Users user) {
        this.timeStamp = timeStamp;
        this.task = task;
        this.taskDescription = taskDescription;
        this.ipAdress = ipAdress;
        this.user = user;
    }

    public String getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Users getUsers() {
        return user;
    }

    public void setUsers(Users user) {
        this.user = user;
    }

    public void log() {
        try {
            Properties prop = new Properties();
            prop.load(CirtLog.class.getResourceAsStream("/cirtlog.properties"));
            SimpleDateFormat dt = new SimpleDateFormat(prop.getProperty("cirtlog.appender.date.Format"));
            String chaine = dt.format(timeStamp) + "\t" + user.getName() + "\t" + user.getEmail() + "\t" + task + "\t" + taskDescription + "\t" + ipAdress;
            outputFile = new File(prop.getProperty("cirtlog.appender.file.File"));
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
