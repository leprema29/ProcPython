/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.test;

import cm.cirt.calldatarecordmanagement.controllers.ListingImeiMultipleMBean;
import cm.cirt.calldatarecordmanagement.controllers.ListingMBean;
import cm.cirt.calldatarecordmanagement.controllers.ListingMultipleMBean;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import sun.security.util.Length;

/**
 *
 * @author Harry Wanki
 */
public class Main {

    public static void main(String[] args) throws IOException {

        File fichier = new File("C:\\images\\requisitions.txt");
        LineIterator it = FileUtils.lineIterator(fichier, "UTF-8");
        int i = 0;
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                i++;
                System.out.println("Numero de ligne: " + i);
                String[] tab = line.split(";");
                String type = tab[0];
                String demandeur = tab[1];
                String numeroRequisition = tab[2];
                String debut = tab[3];
                String fin = tab[4];
                String numeros = tab[5].trim();
                System.out.println(tab[0] + " " + tab[1] + " " + tab[2] + " " + tab[3] + " " + tab[4] + " " + tab[5]);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
                Date dateDebut = convertDate(debut);

                Date dateFin;
                if (fin.equals("today")) {
                    dateFin = new Date();
                } else {
                    dateFin = convertDate(fin);
                }
                List<String> allPhones = new ArrayList<>();
                String[] tab2 = numeros.split(",");
                allPhones.addAll(Arrays.asList(tab2));
                if (type.equals("numeros")) {
                    multiple(dateDebut, dateFin, demandeur, numeroRequisition, allPhones);
                } else if (type.equals("imei")) {
                    imeiMultiple(dateDebut, dateFin, demandeur, numeroRequisition, allPhones);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static Date convertDate(String dateString) {
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String[] tab = dateString.split("-");
        try {
            if ((tab[0].length() == 2) && (tab[1].length() == 2) && (tab[2].length() == 4)) {
                date = df2.parse(dateString);
            } else {
                date = df.parse(dateString);
            }
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    static void single() throws IOException {
        ListingMBean listing = new ListingMBean();
        Date beginDate = new Date(2019 - 1900, 0, 01);
        Date endDate = new Date(2019 - 1900, 0, 7);
        String telephone = "697017002";
//        String telephone = "694981401";
//        String telephone = "696199425";
        listing.setBeginDate(beginDate);
        listing.setEndDate(endDate);
        listing.setTelephone(telephone);
        System.out.println(beginDate);
        System.out.println(endDate);
        listing.findListing();
    }

    static void multiple(Date beginDate, Date endDate, String demandeurRequisition, String numeroRequisition, List<String> allPhones) throws IOException {
        ListingMultipleMBean listing = new ListingMultipleMBean();
//        allPhones.add("697017002");
//        allPhones.add("678835020");
//        allPhones.add("661000083");
        listing.setBeginDate(beginDate);
        listing.setEndDate(endDate);
        listing.setInputPhones(allPhones);
        listing.setDemandeurRequisition(demandeurRequisition);
        listing.setNumeroRequisition(numeroRequisition);
        System.out.println(beginDate);
        System.out.println(endDate);
        listing.findListing();
    }

    static void imeiMultiple(Date beginDate, Date endDate, String demandeurRequisition, String numeroRequisition, List<String> allPhones) throws IOException {
        ListingImeiMultipleMBean listing = new ListingImeiMultipleMBean();
//        allPhones.add("354678060314840");//Mtn
//        allPhones.add("351558105998150");//Orange
//        allPhones.add("356391080908760");//Nexttel

        listing.setBeginDate(beginDate);
        listing.setEndDate(endDate);
        listing.setInputPhones(allPhones);
        listing.setDemandeurRequisition(demandeurRequisition);
        listing.setNumeroRequisition(numeroRequisition);
        System.out.println(beginDate);
        System.out.println(endDate);
        listing.findListing();
    }
}
