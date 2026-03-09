/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.controllers;

import cm.cirt.calldatarecordmanagement.abou.ExecuteShell;
import cm.cirt.calldatarecordmanagement.metier.Variables111;
import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Harry Wanki
 */
public class TaskListingMultiple implements Callable {

    private ExecuteShell shell;
    private String[] param;
    private String operator;
    private String dateRequisition;
    private String demandeurRequisition;

    public TaskListingMultiple() {
    }

    public TaskListingMultiple(String dateRequisition, String demandeurRequisition, String[] param, String operator) {
        this.param = param;
        this.operator = operator;
        this.dateRequisition = dateRequisition;
        this.demandeurRequisition = demandeurRequisition;
        shell = new ExecuteShell(dateRequisition, demandeurRequisition);
    }

    @Override
    public Object call() throws Exception {
//        String nums = param[0].replace(" ", ",");
//        nums = nums.replace("\"", "");
//        String[] ListeNum = nums.split(",", -1);
//
//        for (String numero : ListeNum) {
//            CreateDirectory(numero);
//        }
        return shell.traiter_requisition_Multiple(this.param, "true", this.operator);
    }

    public void CreateDirectory(String NameFolder) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();
        System.out.println("date ! " + date);
        File file = new File(Variables111.DESTINATION_DOSSIERS + this.dateRequisition + "/" + NameFolder);
        if (!file.exists()) {
            if (file.mkdirs()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory! " + Variables111.DESTINATION_DOSSIERS + this.dateRequisition + "/" + NameFolder);
            }
        }

    }
}
