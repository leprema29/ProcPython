/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.controllers;

import cm.cirt.calldatarecordmanagement.metier.Variables111;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

/**
 *
 * @author Harry Wanki
 */
//@Named(value = "whiteListeMBean")
//@RequestScoped
@ManagedBean
@javax.faces.bean.RequestScoped
public class WhiteListeMBean {

    private String whiteList;

    public String getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(String whiteList) {
        this.whiteList = whiteList;
    }

    /**
     * Creates a new instance of WhiteListeMBean
     */
    public WhiteListeMBean() {
    }

    @PostConstruct
    public void init() {
        loadWhiteList();
    }

    public void loadWhiteList() {
        File file = new File(Variables111.BASE_FOLDER + "whitelistphone.txt");
        String val = "";
        try {
            LineIterator it = FileUtils.lineIterator(file, "UTF-8");
            try {
                while (it.hasNext()) {
                    String phone = it.nextLine();
                    val = val + "\n" + phone;
                }
            } finally {
                LineIterator.closeQuietly(it);
            }
        } catch (IOException ex) {
            Logger.getLogger(ListingMultipleMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.whiteList = val.replaceFirst("\n", "");
    }

    public void updateWhitelist() {
        try {
            File fout = new File(Variables111.BASE_FOLDER + "whitelistphone.txt");
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(whiteList);
            bw.close();
        } catch (IOException ex) {
        }
        try {
            File fout = new File(Variables111.BASE_FOLDER + "whitelistphone.txt");
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(whiteList);
            bw.close();
        } catch (IOException ex) {
        }
        try {
            File fout = new File(Variables111.BASE_FOLDER + "whitelistphone.txt");
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(whiteList);
            bw.close();
        } catch (IOException ex) {
        }
        try {
            File fout = new File(Variables111.BASE_FOLDER + "whitelistphone.txt");
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(whiteList);
            bw.close();
        } catch (IOException ex) {
        }
        try {
            File fout = new File(Variables111.BASE_FOLDER + "whitelistphone.txt");
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(whiteList);
            bw.close();
        } catch (IOException ex) {
        }
    }
}
