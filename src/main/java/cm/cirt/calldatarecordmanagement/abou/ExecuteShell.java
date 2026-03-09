/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.abou;

import cm.cirt.calldatarecordmanagement.abou.print.HeaderFooterPageEvent;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.jcraft.jsch.SftpException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.cirt.calldatarecordmanagement.abou.print.PrintMTN;
import static cm.cirt.calldatarecordmanagement.abou.print.PrintMTN.*;
import static cm.cirt.calldatarecordmanagement.abou.print.PrintMTN.MakeCellL;
import static cm.cirt.calldatarecordmanagement.abou.print.PrintMTN.TitreDocument;
import cm.cirt.calldatarecordmanagement.abou.print.PrintMTNMultiple;
import static cm.cirt.calldatarecordmanagement.abou.print.PrintMTNMultiple.TitreDemandeur;
import cm.cirt.calldatarecordmanagement.metier.Variables111;
import cm.cirt.calldatarecordmanagement.abou.print.PrintNEXTTEL;
import cm.cirt.calldatarecordmanagement.abou.print.PrintNEXTTELMultiple;
import cm.cirt.calldatarecordmanagement.abou.print.PrintOrange;
import cm.cirt.calldatarecordmanagement.abou.print.PrintOrangeMultiple;
import java.io.OutputStream;
import java.text.ParseException;

/**
 *
 * @author aboubecker
 */
public class ExecuteShell {

    private String dateRequisition;

    private String demandeur;

    public ExecuteShell() {
    }

    public ExecuteShell(String dateRequisition, String demandeur) {
        this.dateRequisition = dateRequisition;
        this.demandeur = demandeur;
    }

    public String getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(String demandeur) {
        this.demandeur = demandeur;
    }

    public ExecuteShell(String dateRequisition) {
        this.dateRequisition = dateRequisition;
    }

    public String getDateRequisition() {
        return dateRequisition;
    }

    public void setDateRequisition(String dateRequisition) {
        this.dateRequisition = dateRequisition;
    }

    public void execute() {

        System.out.println("dans l'autre");
        Process p;

        try {
            List<String> cmdList = new ArrayList<String>();
            // adding command and args to the list
            cmdList.add("sh");
            cmdList.add("/root/list.sh");
            ProcessBuilder pb = new ProcessBuilder(cmdList);
            p = pb.start();

            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException | InterruptedException e) {
        }
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
    
    public void CreateDirectoryForImei(String NameFolder, String operator) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();
        System.out.println("date ! " + date);
        File file = new File(Variables111.DESTINATION_DOSSIERS + this.dateRequisition + "/" + operator + "/" + NameFolder);
        if (!file.exists()) {
            if (file.mkdirs()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory! " + Variables111.DESTINATION_DOSSIERS + this.dateRequisition + "/" + NameFolder);
            }
        }

    }

    public boolean traiter_requisition(String[] param, String identification, String operateur) throws DocumentException, BadElementException, FileNotFoundException, IOException {

        CreateDirectory(param[0]);
        PrintMTN pm = new PrintMTN();
        PrintNEXTTEL pn = new PrintNEXTTEL();
        PrintOrange po = new PrintOrange();

        System.out.println("traitement en cours !!!!");
        Document document = new Document();

        OutputStream out = new FileOutputStream(Variables111.DESTINATION_DOSSIERS + this.dateRequisition + "/" + param[0] + "/" + "Requisition_" + param[0] + ".pdf");
//        PdfWriter.getInstance(document, out);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, out);

        HeaderFooterPageEvent headerAndFooter = new HeaderFooterPageEvent();
        pdfWriter.setPageEvent(headerAndFooter);

        document.open();

        document = Charger_enteteMTN(document);

        MakeCellL(null);
        TitreDocument(document, "RÉQUISITION RELATIVE AUX IDENTITÉS DES ABONNÉS DE TÉLÉPHONIE MOBILE");
        TitreDate(document, "Période : du " + ConvertDate(param[1]) + " Au " + ConvertDate(param[2]));
//        TitreDocument(document, "RÉQUISITION RELATIVE AUX IDENTITÉS DES ABONNÉS DE TÉLÉPHONIE MOBILE PAR CELLULE");

//        All0peratorRequisition Allreq = new All0peratorRequisition();
//        Allreq.setVisible(true);
//        this.setVisible(false);
        //Besoins!!!! 
        if ("Camtel".equals(operateur)) {
            System.out.println("Camtel");

        }
        if ("Mtn".equals(operateur)) {
            RemoteRequiMTN rqMTN = new RemoteRequiMTN(this.dateRequisition);

            System.out.println("MTN");

            try {
                rqMTN.remoteFind_MTN(param, document, pm, identification);
            } catch (SftpException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadElementException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        if ("Nexttel".equals(operateur)) {
            System.out.println("NEXTTEL");

            RemoteRequiNEXTTEL rqNEX = new RemoteRequiNEXTTEL(this.dateRequisition);

            try {
                rqNEX.remoteFind_NEXTTEL(param, document, pn, identification);
            } catch (SftpException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadElementException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        if ("Orange".equals(operateur)) {
            RemoteRequiORANGE rqOR = new RemoteRequiORANGE(this.dateRequisition);
            System.out.println("Orange");
            try {
                rqOR.remoteFind_ORANGE(param, document, po, identification);
            } catch (SftpException | DocumentException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        System.out.println("fin du traitement");
        document.close();
        out.close();
        return true;

    }

    public boolean traiter_requisition_Multiple(String[] param, String identification, String operateur) throws DocumentException, BadElementException, FileNotFoundException, IOException {

//        CreateDirectory(param[0]);
        PrintMTNMultiple pm = new PrintMTNMultiple();
        PrintNEXTTELMultiple pn = new PrintNEXTTELMultiple();
        PrintOrangeMultiple po = new PrintOrangeMultiple();

        System.out.println("traitement en cours !!!!");
        System.out.println(operateur + " " + param[0]);

        String[] ListeNum = null;
        int nbNum;
        String nums = param[0].replace(" ", ",");
        nums = nums.replace("\"", "");

        ListeNum = nums.split(",", -1);

        nbNum = ListeNum.length;

        System.out.println("nbNum :" + nbNum);

        Document[] listDoc = new Document[nbNum];

        int i = 0;
        while (i < nbNum) {

            CreateDirectory(ListeNum[i]);

            System.out.println(ListeNum[i]);

            listDoc[i] = new Document();

            PdfWriter pdfWriter = PdfWriter.getInstance(listDoc[i], new FileOutputStream(Variables111.DESTINATION_DOSSIERS + this.dateRequisition + "/" + ListeNum[i] + "/" + "Requisition_" + ListeNum[i] + ".pdf"));

            //        Adding metadata
            listDoc[i].addTitle("Réquisitions");
            listDoc[i].addAuthor(demandeur);
            listDoc[i].addSubject("RÉQUISITION RELATIVE AUX IDENTITÉS DES ABONNÉS DE TÉLÉPHONIE MOBILE");
            listDoc[i].addKeywords("Metadata, iText, PDF");
            listDoc[i].addCreator(demandeur);
            HeaderFooterPageEvent headerAndFooter = new HeaderFooterPageEvent();
            pdfWriter.setPageEvent(headerAndFooter);
            listDoc[i].open();

            listDoc[i] = cm.cirt.calldatarecordmanagement.abou.print.PrintMTNMultiple.Charger_enteteMTN(listDoc[i]);

            MakeCellL(null);
            TitreDocument(listDoc[i], "RÉQUISITION RELATIVE AUX IDENTITÉS DES ABONNÉS DE TÉLÉPHONIE MOBILE");
            cm.cirt.calldatarecordmanagement.abou.print.PrintMTNMultiple.TitreDate(listDoc[i], "Période : du " + ConvertDate(param[1]) + " Au " + ConvertDate(param[2]));
            if (!demandeur.equals("")) {
                TitreDemandeur(listDoc[i], "Service Demandeur: " + demandeur);
            }

            i++;
        }

        //Besoins!!!! 
        if ("Camtel".equals(operateur)) {
            System.out.println("Camtel");

        }
        if ("Mtn".equals(operateur)) {
            RemoteRequiMTN rqMTN = new RemoteRequiMTN(this.dateRequisition);

            System.out.println("MTN");

            try {
                rqMTN.remoteFind_MTN_Multiple(param, listDoc, pm, identification, nbNum, ListeNum);
            } catch (SftpException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadElementException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        if ("Nexttel".equals(operateur)) {
            System.out.println("NEXTTEL");

            RemoteRequiNEXTTEL rqNEX = new RemoteRequiNEXTTEL(this.dateRequisition);

            try {
                rqNEX.remoteFind_NEXTTEL_Multiple(param, listDoc, pn, identification, nbNum, ListeNum);
            } catch (SftpException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadElementException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
//
        if ("Orange".equals(operateur)) {
            RemoteRequiORANGE rqOR = new RemoteRequiORANGE(this.dateRequisition);
            System.out.println("Orange");
            try {
                rqOR.remoteFind_ORANGE_Multiple(param, listDoc, po, identification, nbNum, ListeNum);
            } catch (SftpException | DocumentException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        System.out.println("fin du traitement");

        i = 0;
        while (i < nbNum) {

            System.out.println(i);
            listDoc[i].close();
            i++;

        }

        return true;

    }

    public boolean traiter_requisition_Imei_Multiple(String[] param, String identification, String operateur) throws DocumentException, BadElementException, FileNotFoundException, IOException {

//        CreateDirectory(param[0]);
        PrintMTNMultiple pm = new PrintMTNMultiple();
        PrintNEXTTELMultiple pn = new PrintNEXTTELMultiple();
        PrintOrangeMultiple po = new PrintOrangeMultiple();

        System.out.println("traitement en cours !!!!");
        System.out.println(operateur + " " + param[0]);

        String[] ListeNum = null;
        int nbNum;
        String nums = param[0].replace(" ", ",");
        nums = nums.replace("\"", "");

        ListeNum = nums.split(",", -1);

        nbNum = ListeNum.length;

        System.out.println("nbNum :" + nbNum);

        Document[] listDoc = new Document[nbNum];

        int i = 0;
        while (i < nbNum) {

            CreateDirectory(ListeNum[i]);

            System.out.println(ListeNum[i]);

            listDoc[i] = new Document();

            PdfWriter pdfWriter = PdfWriter.getInstance(listDoc[i], new FileOutputStream(Variables111.DESTINATION_DOSSIERS + this.dateRequisition + "/" + ListeNum[i] + "/" + "Requisition_" + ListeNum[i] + ".pdf"));
//            PdfWriter pdfWriter = null; 
            //        Adding metadata
            listDoc[i].addTitle("Réquisitions");
            listDoc[i].addAuthor(demandeur);
            listDoc[i].addSubject("RÉQUISITION RELATIVE AUX IDENTITÉS DES ABONNÉS DE TÉLÉPHONIE MOBILE");
            listDoc[i].addKeywords("Metadata, iText, PDF");
            listDoc[i].addCreator(demandeur);
            HeaderFooterPageEvent headerAndFooter = new HeaderFooterPageEvent();
            pdfWriter.setPageEvent(headerAndFooter);
            listDoc[i].open();

            listDoc[i] = cm.cirt.calldatarecordmanagement.abou.print.PrintMTNMultiple.Charger_enteteMTN(listDoc[i]);

            MakeCellL(null);
            TitreDocument(listDoc[i], "RÉQUISITION RELATIVE AUX IDENTITÉS DES ABONNÉS DE TÉLÉPHONIE MOBILE");
            cm.cirt.calldatarecordmanagement.abou.print.PrintMTNMultiple.TitreDate(listDoc[i], "Période : du " + ConvertDate(param[1]) + " Au " + ConvertDate(param[2]));
            if (!demandeur.equals("")) {
                TitreDemandeur(listDoc[i], "Service Demandeur: " + demandeur);
            }

            i++;
        }

        //Besoins!!!! 
        if ("Camtel".equals(operateur)) {
            System.out.println("Camtel");

        }
        if ("Mtn".equals(operateur)) {
            RemoteRequiMTN rqMTN = new RemoteRequiMTN(this.dateRequisition);

            System.out.println("MTN");

            try {
                rqMTN.remoteFind_MTN_Imei_Multiple(param, listDoc, pm, identification, nbNum, ListeNum);
            } catch (SftpException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadElementException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        if ("Nexttel".equals(operateur)) {
            System.out.println("NEXTTEL");

            RemoteRequiNEXTTEL rqNEX = new RemoteRequiNEXTTEL(this.dateRequisition);

            try {
                rqNEX.remoteFind_NEXTTEL_Imei_Multiple(param, listDoc, pn, identification, nbNum, ListeNum);
            } catch (SftpException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadElementException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
//
        if ("Orange".equals(operateur)) {
            RemoteRequiORANGE rqOR = new RemoteRequiORANGE(this.dateRequisition);
            System.out.println("Orange");
            try {
                rqOR.remoteFind_ORANGE_Imei_Multiple(param, listDoc, po, identification, nbNum, ListeNum);
            } catch (SftpException | DocumentException ex) {
                Logger.getLogger(ExecuteShell.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        System.out.println("fin du traitement");

        i = 0;
        while (i < nbNum) {

            System.out.println(i);
            listDoc[i].close();
            i++;

        }

        return true;

    }

    public String ConvertDate(String value) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatterPDF = new SimpleDateFormat("dd/MM/yyyy");

        String datePDF = null;
        try {

            Date date = formatter.parse(value);

            datePDF = formatterPDF.format(date);
//            System.out.println(date);
//            System.out.println(formatter.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return datePDF;
    }
}
