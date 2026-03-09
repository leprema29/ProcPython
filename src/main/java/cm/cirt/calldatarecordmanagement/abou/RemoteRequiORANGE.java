/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.abou;

/**
 *
 * @author aboubecker
 */
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import cm.cirt.calldatarecordmanagement.abou.csv.CsvFileWriterORANGE;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.StatistiqueLieux;
import cm.cirt.calldatarecordmanagement.abou.cm.backup.StatistiqueORANGEfrequence1111;
import cm.cirt.calldatarecordmanagement.abou.csv.CsvFileWriterORANGEMultiple;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.FreqCell;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.FreqCorrespondant;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.FreqDureeAppel;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.FreqImei;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.SharedImei;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.identificationORANGE;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.listing;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.listingOrangeSMS;
import static cm.cirt.calldatarecordmanagement.abou.print.PrintMTN.TitrePartie;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import cm.cirt.calldatarecordmanagement.abou.print.PrintOrange;
import static cm.cirt.calldatarecordmanagement.abou.print.PrintMTN.smallBold;
import cm.cirt.calldatarecordmanagement.abou.print.PrintOrangeMultiple;
import cm.cirt.calldatarecordmanagement.metier.Variables111;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class RemoteRequiORANGE {

    private String dateRequisition;

    public RemoteRequiORANGE(String dateRequisition) {
        this.dateRequisition = dateRequisition;
    }

    public RemoteRequiORANGE() {
    }

    public String getDateRequisition() {
        return dateRequisition;
    }

    public void setDateRequisition(String dateRequisition) {
        this.dateRequisition = dateRequisition;
    }

//    public static void main(String[] args) {
    public void remoteFind_ORANGE(String[] args, Document document, PrintOrange po, String identification) throws SftpException, DocumentException {

//        All0peratorRequisition Allreq = new All0peratorRequisition();
//        Allreq.setVisible(true);
        JSch jsch = new JSch();

        String command = "sh requisitionORANGE.sh " + args[0] + " " + args[1] + " " + args[2];
//        String command = "pwd";
//        System.out.println("command :" + command);
        Session session;
        try {

            // Open a Session to remote SSH server and Connect.
            // Set User and IP of the remote host and SSH port.
            session = jsch.getSession(Variables111.ORANGE_USER, Variables111.ORANGE_HOST, 22);
            // When we do SSH to a remote host for the 1st time or if key at the remote host 
            // changes, we will be prompted to confirm the authenticity of remote host. 
            // This check feature is controlled by StrictHostKeyChecking ssh parameter. 
            // By default StrictHostKeyChecking  is set to yes as a security measure.
            session.setConfig("StrictHostKeyChecking", "no");
            //Set Password
String privateKeyPath = Variables.getIdRsaPath(); // Replace with your actual path
jsch.addIdentity(privateKeyPath);
            //session.setPassword(Variables111.ORANGE_PASSWORD);
            session.connect();

            // create the execution channel over the session
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            // Set the command to execute on the channel and execute the command
            channelExec.setCommand(command);
//            channelExec.setCommand("sh myscript.sh Rajesh");
            channelExec.connect();

            // Get an InputStream from this channel and read messages, generated 
            // by the executing command, from the remote side.
            InputStream in = channelExec.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Command execution completed here.
            // Retrieve the exit status of the executed command
            int exitStatus = channelExec.getExitStatus();
            if (exitStatus > 0) {
                System.out.println("Remote script exec error! " + exitStatus);
            }

            //creation fichier
            this.lireFicOrange(args[0], session, document, po, identification);

            //Disconnect the Session
            session.disconnect();

        } catch (JSchException | IOException e) {
        }

    }

    public void remoteFind_ORANGE_Multiple(String[] args, Document[] listDoc, PrintOrangeMultiple po, String identification, int nbNum, String[] ListeNum) throws SftpException, DocumentException {

//        All0peratorRequisition Allreq = new All0peratorRequisition();
//        Allreq.setVisible(true);
        JSch jsch = new JSch();

//        String command = "sh requisitionORANGE_Multiple.sh " + args[0] + " " + args[1] + " " + args[2];
        String command = "/home/data/orange/operations/cdr/requisitionOrange_Multiple_Optimized.sh " + args[0] + " " + args[1] + " " + args[2] + " " + dateRequisition;
//        String command = "pwd";
//        System.out.println("command :" + command);
        Session session;
        try {

            // Open a Session to remote SSH server and Connect.
            // Set User and IP of the remote host and SSH port.
            session = jsch.getSession(Variables111.ORANGE_USER, Variables111.ORANGE_HOST, 22);
            // When we do SSH to a remote host for the 1st time or if key at the remote host 
            // changes, we will be prompted to confirm the authenticity of remote host. 
            // This check feature is controlled by StrictHostKeyChecking ssh parameter. 
            // By default StrictHostKeyChecking  is set to yes as a security measure.
            session.setConfig("StrictHostKeyChecking", "no");
            //Set Password
String privateKeyPath = Variables.getIdRsaPath(); // Replace with your actual path
jsch.addIdentity(privateKeyPath);
            //session.setPassword(Variables111.ORANGE_PASSWORD);
            session.connect();

            // create the execution channel over the session
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            // Set the command to execute on the channel and execute the command
            channelExec.setCommand(command);
//            channelExec.setCommand("sh myscript.sh Rajesh");
            channelExec.connect();

            // Get an InputStream from this channel and read messages, generated 
            // by the executing command, from the remote side.
            InputStream in = channelExec.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Command execution completed here.
            // Retrieve the exit status of the executed command
            int exitStatus = channelExec.getExitStatus();
            if (exitStatus > 0) {
                System.out.println("Remote script exec error! " + exitStatus);
            }

            //creation fichier
            this.lireFicOrange_Multiple(args[0], session, listDoc, po, identification, nbNum, ListeNum);

            //Disconnect the Session
            session.disconnect();

        } catch (JSchException | IOException e) {
        }

    }

    public void remoteFind_ORANGE_Imei_Multiple(String[] args, Document[] listDoc, PrintOrangeMultiple po, String identification, int nbNum, String[] ListeNum) throws SftpException, DocumentException {

//        All0peratorRequisition Allreq = new All0peratorRequisition();
//        Allreq.setVisible(true);
        JSch jsch = new JSch();

//        String command = "sh requisitionORANGE_Multiple.sh " + args[0] + " " + args[1] + " " + args[2];
        String command = "/home/data/orange/operations/cdr/requisitionImeiOrange_Multiple_Optimized.sh " + args[0] + " " + args[1] + " " + args[2] + " " + dateRequisition;
//        String command = "pwd";
//        System.out.println("command :" + command);
        Session session;
        try {

            // Open a Session to remote SSH server and Connect.
            // Set User and IP of the remote host and SSH port.
            session = jsch.getSession(Variables111.ORANGE_USER, Variables111.ORANGE_HOST, 22);
            // When we do SSH to a remote host for the 1st time or if key at the remote host 
            // changes, we will be prompted to confirm the authenticity of remote host. 
            // This check feature is controlled by StrictHostKeyChecking ssh parameter. 
            // By default StrictHostKeyChecking  is set to yes as a security measure.
            session.setConfig("StrictHostKeyChecking", "no");
            //Set Password
String privateKeyPath = Variables.getIdRsaPath(); // Replace with your actual path
jsch.addIdentity(privateKeyPath);
            //session.setPassword(Variables111.ORANGE_PASSWORD);
            session.connect();

            // create the execution channel over the session
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            // Set the command to execute on the channel and execute the command
            channelExec.setCommand(command);
//            channelExec.setCommand("sh myscript.sh Rajesh");
            channelExec.connect();

            // Get an InputStream from this channel and read messages, generated 
            // by the executing command, from the remote side.
            InputStream in = channelExec.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Command execution completed here.
            // Retrieve the exit status of the executed command
            int exitStatus = channelExec.getExitStatus();
            if (exitStatus > 0) {
                System.out.println("Remote script exec error! " + exitStatus);
            }

            //creation fichier
            this.lireFicOrange_Imei_Multiple(args[0], session, listDoc, po, identification, nbNum, ListeNum);

            //Disconnect the Session
            session.disconnect();

        } catch (JSchException | IOException e) {
        }

    }

    public void remoteFind_ORANGE_IMEI(String[] args, Document document, PrintOrange po, String identification) throws SftpException, DocumentException {

//        All0peratorRequisition Allreq = new All0peratorRequisition();
//        Allreq.setVisible(true);
        JSch jsch = new JSch();

//        String command = "sh requisitionORANGE_IMEI.sh " + args[0] + " " + args[1] + " " + args[2];
        String command = "pwd";
//        System.out.println("command :" + command);
        Session session;
        try {

            // Open a Session to remote SSH server and Connect.
            // Set User and IP of the remote host and SSH port.
            session = jsch.getSession(Variables111.ORANGE_USER, Variables111.ORANGE_HOST, 22);
            // When we do SSH to a remote host for the 1st time or if key at the remote host 
            // changes, we will be prompted to confirm the authenticity of remote host. 
            // This check feature is controlled by StrictHostKeyChecking ssh parameter. 
            // By default StrictHostKeyChecking  is set to yes as a security measure.
            session.setConfig("StrictHostKeyChecking", "no");
            //Set Password
String privateKeyPath = Variables.getIdRsaPath(); // Replace with your actual path
jsch.addIdentity(privateKeyPath);
            //session.setPassword(Variables111.ORANGE_PASSWORD);
            session.connect();

            // create the execution channel over the session
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            // Set the command to execute on the channel and execute the command
            channelExec.setCommand(command);
//            channelExec.setCommand("sh myscript.sh Rajesh");
            channelExec.connect();

            // Get an InputStream from this channel and read messages, generated 
            // by the executing command, from the remote side.
            InputStream in = channelExec.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Command execution completed here.
            // Retrieve the exit status of the executed command
            int exitStatus = channelExec.getExitStatus();
            if (exitStatus > 0) {
                System.out.println("Remote script exec error! " + exitStatus);
            }

            //creation fichier
            this.lireFicOrange(args[0], session, document, po, identification);

            //Disconnect the Session
            session.disconnect();

        } catch (JSchException | IOException e) {
        }

    }

    public void lireFicOrange(String num, Session session, Document document, PrintOrange po, String identification) throws JSchException, SftpException, IOException, BadElementException, DocumentException {

        System.out.println("Creating SFTP Channel.");
        ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
        sftpChannel.connect();
        System.out.println("SFTP Channel created.");
        InputStream out = null;
        String identite_numero = "/root/" + num + "/identite_numero.txt";
        String listingAppelEmis = "/root/" + num + "/appelemis.txt";
//        String listingAppelRecus = "/root/" + num + "/appelrecus.txt";
        String listingsms = "/root/" + num + "/smsfinal.txt";
        String identiteAbonne = "/root/" + num + "/IdentificationAbonneesfinal.txt";
        String statfre = "/root/" + num + "/NBnumero.txt";
        String statlieux = "/root/" + num + "/NBSites.txt";

        System.out.println("identite_numero" + identite_numero);
        out = sftpChannel.get(identite_numero);
        BufferedReader br = new BufferedReader(new InputStreamReader(out));
        String line;
        String[] inputNumberOrange = null;

        while ((line = br.readLine()) != null) {
//            System.out.println("identite_numero" + line);
            inputNumberOrange = line.split(",", -1);
        }

        if ("true".equals(identification)) {
            document = po.PrintIDAbonneOrange(num, document, inputNumberOrange);
        }
        //create csv
        CsvFileWriterORANGE.writeCsvFileORANGEIDNumero(num, inputNumberOrange, this.dateRequisition);

        //II. Listing  Appels Emis
        out = sftpChannel.get(listingAppelEmis);
        br = new BufferedReader(new InputStreamReader(out));
        String[] inputcallEmis = null;

        //prepareCSV
        //prepare
        //Create a new list of student objects
        List listingsORANGEemis = new ArrayList();

        PdfPTable table1 = new PdfPTable(6);
        table1.setWidthPercentage(100);
        PdfPCell c2 = new PdfPCell(new Phrase("Numéro appellant", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1.addCell(c2);
        c2 = new PdfPCell(new Phrase("Localisation numéro appelant", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1.addCell(c2);
        c2 = new PdfPCell(new Phrase("IMEI", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1.addCell(c2);
        c2 = new PdfPCell(new Phrase("Date début appel", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1.addCell(c2);
        c2 = new PdfPCell(new Phrase("Durée de l'appel", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1.addCell(c2);
        c2 = new PdfPCell(new Phrase("Numéro appelé", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1.addCell(c2);
        int i = 0;
        String duree = null;
        while ((line = br.readLine()) != null) {
//            System.out.println(line);
            inputcallEmis = line.split(",", -1);
            //convertir && (!"CALL_DURATION".equals(inputcall[3])))
            if (!"".equals(inputcallEmis[1])) {
                duree = ConvertirDuree(inputcallEmis[1]);
                inputcallEmis[1] = duree;
            }
            table1 = po.PrintAddListingOrangeEmis(document, inputcallEmis, table1);
            listing listorangeemis = new listing(inputcallEmis[3], inputcallEmis[8] + "  " + inputcallEmis[9], inputcallEmis[6], inputcallEmis[0], inputcallEmis[1], inputcallEmis[2]);
            listingsORANGEemis.add(listorangeemis);
            i++;
        }
        if ("true".equals(identification)) {
            TitrePartie(document, "II. Listing Appels ");

        } else {
            TitrePartie(document, "I. Listing Appels ");

        }
//        if ("true".equals(identification)) {
//            TitrePartie(document, "II. Listing Appels Emis");
//
//        } else {
//            TitrePartie(document, "I. Listing Appels Emis");
//
//        }

        document.add(table1);
        System.out.println("document PrintAddListingOrangeEmis ");

        //create csv
        CsvFileWriterORANGE.writeCsvFileOrangeListingEmis(num, listingsORANGEemis, this.dateRequisition);

        //III. Listing Appels Recus
//        out = sftpChannel.get(listingAppelRecus);
//        br = new BufferedReader(new InputStreamReader(out));
//        String[] inputcallrecus = null;
//
//        //Create a new list of student objects
//        List listingsORANGErecus = new ArrayList();
//        //prepare
//        //Create a new list of student objects
////        List listingsOrange = new ArrayList();
//
//        table1 = new PdfPTable(6);
//        table1.setWidthPercentage(100);
//        c2 = new PdfPCell(new Phrase("Numéro appelé", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        c2 = new PdfPCell(new Phrase("Localisation numéro Appelé", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        c2 = new PdfPCell(new Phrase("IMEI numéro Appelé", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        c2 = new PdfPCell(new Phrase("Date début appel", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        c2 = new PdfPCell(new Phrase("Durée de l'appel", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        c2 = new PdfPCell(new Phrase("Numéro appellant", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        i = 0;
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//            inputcallrecus = line.split(",", -1);
////            && (!"CALLDURATION".equals(inputcallrecus[1])) && (!"0".equals(i))
//            if (!"".equals(inputcallrecus[1]) ) {
//                duree = ConvertirDuree(inputcallrecus[1]);
//                inputcallrecus[1] = duree;
//            }
//
//            table1 = po.PrintAddListingOrangeRecus(document, inputcallrecus, table1);
//            listingOrangeEmis listorangeemis = new listingOrangeEmis(inputcallrecus[2], inputcallrecus[8] + " " + inputcallrecus[9], inputcallrecus[6], inputcallrecus[0], inputcallrecus[1], inputcallrecus[3]);
//            listingsORANGErecus.add(listorangeemis);
//            i++;
//        }
//
//        if ("true".equals(identification)) {
//            TitrePartie(document, "III. Listing Appels Recus");
//
//        } else {
//            TitrePartie(document, "II. Listing Appels Recus");
//
//        }
//        document.add(table1);
//        System.out.println("document PrintAddListingOrangeRecus ");
        //create csv
//        CsvFileWriterORANGE.writeCsvFileOrangeListingRecus(num, listingsORANGErecus);
        //SMS gestions
        out = sftpChannel.get(listingsms);
        br = new BufferedReader(new InputStreamReader(out));
        String[] inputsms = null;
        //
        //prepare

        //print Abonné
        List ListOrangeSMS = new ArrayList();
        //Create a new list of student objects
        PdfPTable table2 = new PdfPTable(5);
        table2.setWidthPercentage(100);
        c2 = new PdfPCell(new Phrase("Numéro d'envoi", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table2.addCell(c2);
        c2 = new PdfPCell(new Phrase("Localisation numéro récepteur", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table2.addCell(c2);
        c2 = new PdfPCell(new Phrase("IMEI Numéro récepteur", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table2.addCell(c2);
        c2 = new PdfPCell(new Phrase("Date sms", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table2.addCell(c2);
        c2 = new PdfPCell(new Phrase("Numéro récepteur", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table2.addCell(c2);
        while ((line = br.readLine()) != null) {
//            System.out.println(line);
            inputsms = line.split(",", -1);
            table2 = po.PrintAddListingOrangeSMS(document, inputsms, table2);
            listingOrangeSMS listingmtn = new listingOrangeSMS(inputsms[4], inputsms[8] + " " + inputsms[9], inputsms[6], inputsms[0], inputsms[7]);
            ListOrangeSMS.add(listingmtn);
        }

        if ("true".equals(identification)) {
            TitrePartie(document, "III. Listing SMS");
        } else {
            TitrePartie(document, "II. Listing SMS");
        }
//        if ("true".equals(identification)) {
//            TitrePartie(document, "IV. Listing SMS");
//        } else {
//            TitrePartie(document, "III. Listing SMS");
//        }
        document.add(table2);
        //ecrire csv file
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();

//        String fileName = "Requisition" + num + ".csv";
        System.out.println("Write CSV file: sms");

        CsvFileWriterORANGE.writeCsvFileOrangeListingSMS(num, ListOrangeSMS, this.dateRequisition);
        //print Abonné
        List abonnesOrange = new ArrayList();
        out = sftpChannel.get(identiteAbonne);
        br = new BufferedReader(new InputStreamReader(out));
        String[] IdAbonne = null;

        //prepare 
        PdfPTable tableAbonne = new PdfPTable(6);
        tableAbonne.setWidthPercentage(100);
        c2 = new PdfPCell(new Phrase("Numéro", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);
        c2 = new PdfPCell(new Phrase("Nom & Prénom", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);
        c2 = new PdfPCell(new Phrase("Date de naissance", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);
        c2 = new PdfPCell(new Phrase("Numéro CNI", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);
        c2 = new PdfPCell(new Phrase("Date expiration CNI", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);
        c2 = new PdfPCell(new Phrase("Adresse", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);

        while ((line = br.readLine()) != null) {
//            System.out.println(line);
            IdAbonne = line.split(",", -1);
            tableAbonne = po.PrintAddIdAbonneOrange(document, IdAbonne, tableAbonne);
            identificationORANGE listid = new identificationORANGE(IdAbonne[0], IdAbonne[2], IdAbonne[3], IdAbonne[1], IdAbonne[4], IdAbonne[5]);
            abonnesOrange.add(listid);
        }

        if ("true".equals(identification)) {
            TitrePartie(document, "V. Identification des numeros  ");

            document.add(tableAbonne);
        }

        System.out.println("Write CSV file ");

        CsvFileWriterORANGE.writeCsvFileAbonneOrange(num, abonnesOrange, this.dateRequisition);

        if ("true".equals(identification)) {
            TitrePartie(document, "V. Statistiques");

            TitrePartie(document, "V.1 Statistique sur la frequence des numeros");

        } else {
            TitrePartie(document, "III. Statistiques");

            TitrePartie(document, "III.1 Statistique sur la frequence des numeros");

        }
//        if ("true".equals(identification)) {
//            TitrePartie(document, "VI. Statistiques");
//
//            TitrePartie(document, "VI.1 Statistique sur la frequence des numeros");
//
//        } else {
//            TitrePartie(document, "IV. Statistiques");
//
//            TitrePartie(document, "IV.1 Statistique sur la frequence des numeros");
//
//        }

        List StatsFRE = new ArrayList();
//
        out = sftpChannel.get(statfre);
        br = new BufferedReader(new InputStreamReader(out));
        String[] tabstat = null;
//
//        //prepae 
        PdfPTable tablestat = new PdfPTable(4);
        tablestat.setWidthPercentage(100);

        c2 = new PdfPCell(new Phrase("N°", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablestat.addCell(c2);
        c2 = new PdfPCell(new Phrase("Numero de telephone", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablestat.addCell(c2);
        c2 = new PdfPCell(new Phrase("Occurence", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablestat.addCell(c2);
        c2 = new PdfPCell(new Phrase("Duree totale de communications ", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablestat.addCell(c2);
        i = 0;
//
        while ((line = br.readLine()) != null) {
//            System.out.println(line);

            try {
                tabstat = line.split(",", -1);
                if (!"".equals(tabstat[1])) {
                    tabstat[1] = ConvertirDuree(tabstat[1]);
                }
                tablestat = po.PrintAddStatsFREORANGE(document, tabstat, tablestat, i + 1);
                StatistiqueORANGEfrequence1111 listid = new StatistiqueORANGEfrequence1111(tabstat[2], Integer.parseInt(tabstat[0]), tabstat[1]);
                StatsFRE.add(listid);
                i++;
            } catch (BadElementException | IOException e) {
            }

        }
        document.add(tablestat);
        StatsFRE.sort(Comparator.comparingInt(StatistiqueORANGEfrequence1111::getOccurence));

//
//      System.out.println("Write CSV file: Stat 1");
        CsvFileWriterORANGE.writeCsvFileStatsFreORANGE(num, StatsFRE, this.dateRequisition);

        if ("true".equals(identification)) {
            TitrePartie(document, "VI.2 Statistique sur la frequence des Lieux d'appels");

        } else {

            TitrePartie(document, "IV.2 Statistique sur la frequence des Lieux d'appels");
        }

////        //print statistiques
        List StatsORANGElieux = new ArrayList();
//
        out = sftpChannel.get(statlieux);
        br = new BufferedReader(new InputStreamReader(out));
        String[] tabstatLieux = null;
//
//        //prepae 
        PdfPTable tablestatlieux = new PdfPTable(3);
        tablestatlieux.setWidthPercentage(100);

        c2 = new PdfPCell(new Phrase("N", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablestatlieux.addCell(c2);
        c2 = new PdfPCell(new Phrase("Localisation du Numero", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablestatlieux.addCell(c2);
        c2 = new PdfPCell(new Phrase("Occurence", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablestatlieux.addCell(c2);

        i = 0;
        while ((line = br.readLine()) != null) {
//            System.out.println(line);

            try {
                tabstatLieux = line.split(",", -1);
                tablestatlieux = po.PrintAddStatsORANGELieux(document, tabstatLieux, tablestatlieux, i + 1);
                StatistiqueLieux listid = new StatistiqueLieux(tabstatLieux[1], Integer.parseInt(tabstatLieux[0]));
                StatsORANGElieux.add(listid);
                i++;
            } catch (BadElementException | IOException e) {
            }

        }
        document.add(tablestatlieux);
//

        System.out.println("Write CSV file: Stat");
        CsvFileWriterORANGE.writeCsvFileStatsORANGELieux(num, StatsORANGElieux, this.dateRequisition);
        br.close();
    }

    public void lireFicOrange_Multiple(String num, Session session, Document[] listDoc, PrintOrangeMultiple po, String identification, int nbNum, String[] ListeNum) throws JSchException, SftpException, IOException, BadElementException, DocumentException {

        int k = 0;
        while (k < nbNum) {
            num = ListeNum[k];
            System.out.println("numero " + num);
            System.out.println("Creating SFTP Channel.");
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            System.out.println("SFTP Channel created.");
            InputStream out = null;
            String basePath;
            if (Variables111.ACCOUNT_TYPE.equals("YAYAP")) {
                basePath = "/home/data/mtn/operations/cdr/"; // Pour le SED YAYAP
            } else {
                basePath = "/root/"; // Pour l'ARMP
            }
            String identite_numero = basePath + this.dateRequisition + "/" + num + "/identite_numero.txt";
            String listingAppelEmis = basePath + this.dateRequisition + "/" + num + "/appelemis.txt";
//          String listingAppelRecus = "/root/" + num + "/appelrecus.txt";
            String listingsms = basePath + this.dateRequisition + "/" + num + "/smsfinal.txt";
            String identiteAbonne = basePath + this.dateRequisition + "/" + num + "/IdentificationAbonneesfinal.txt";
            String frequenceCellule = basePath + this.dateRequisition + "/" + num + "/frequenceCellule.txt";
            String frequenceCorrespondance = basePath + this.dateRequisition + "/" + num + "/frequenceCorrespondance.txt";
            String frequenceDureeAppel = basePath + this.dateRequisition + "/" + num + "/frequenceDureeAppel.txt";
            String frequenceImei = basePath + this.dateRequisition + "/" + num + "/frequenceImei.txt";

            System.out.println("identite_numero" + identite_numero);
            out = sftpChannel.get(identite_numero);
            BufferedReader br = new BufferedReader(new InputStreamReader(out));
            String line;
            String[] inputNumberOrange = null;

            while ((line = br.readLine()) != null) {
//            System.out.println("identite_numero" + line);
                inputNumberOrange = line.split(",", -1);
            }

            if ("true".equals(identification)) {
                listDoc[k] = po.PrintIDAbonneOrange(num, listDoc[k], inputNumberOrange);
            }
            //create csv
            CsvFileWriterORANGEMultiple.writeCsvFileORANGEIDNumero(num, inputNumberOrange, this.dateRequisition);

            //II. Listing  Appels Emis
            out = sftpChannel.get(listingAppelEmis);
            br = new BufferedReader(new InputStreamReader(out));
            String[] inputcallEmis = null;

            //prepareCSV
            //prepare
            //Create a new list of student objects
            List listingsORANGEemis = new ArrayList();

            PdfPTable table1 = new PdfPTable(6);
            int[] widthsListing = {1, 2, 1, 1, 1, 1};
            table1.setWidths(widthsListing);
            table1.setWidthPercentage(100);
            PdfPCell c2 = new PdfPCell(new Phrase("Numéro appellant", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("Localisation numéro appelant", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("IMEI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date début appel", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("Durée de l'appel", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("Numéro appelé", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            int i = 0;
            String duree = null;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                inputcallEmis = line.split(",", -1);
//                if (inputcallEmis.length >= 10) {
                //convertir && (!"CALL_DURATION".equals(inputcall[3])))
                if (!"".equals(inputcallEmis[1])) {
                    duree = ConvertirDuree(inputcallEmis[1]);
                    inputcallEmis[1] = duree;
                }
                table1 = po.PrintAddListingOrangeEmis(listDoc[k], inputcallEmis, table1);
                String location = inputcallEmis[5] + " " + inputcallEmis[6];
                String finalLocation = "--";
                if (!location.contains("null")) {
                    finalLocation = location + " (Cell: " + inputcallEmis[4] + " Long: " + inputcallEmis[7] + " Lat: " + inputcallEmis[8] + " Azimut: -)";
                }
                listing listorangeemis = new listing(inputcallEmis[3], finalLocation, inputcallEmis[9], inputcallEmis[0], inputcallEmis[1], inputcallEmis[2]);
                listingsORANGEemis.add(listorangeemis);
                i++;
//                }
            }
            if ("true".equals(identification)) {
                TitrePartie(listDoc[k], "II. Listing Appels ");

            } else {
                TitrePartie(listDoc[k], "I. Listing Appels ");

            }
//        if ("true".equals(identification)) {
//            TitrePartie(listDoc[k], "II. Listing Appels Emis");
//
//        } else {
//            TitrePartie(listDoc[k], "I. Listing Appels Emis");
//
//        }

            listDoc[k].add(table1);
            System.out.println("listDoc[k] PrintAddListingOrangeEmis ");

            //create csv
            CsvFileWriterORANGEMultiple.writeCsvFileOrangeListingEmis(num, listingsORANGEemis, this.dateRequisition);

            //III. Listing Appels Recus
//        out = sftpChannel.get(listingAppelRecus);
//        br = new BufferedReader(new InputStreamReader(out));
//        String[] inputcallrecus = null;
//
//        //Create a new list of student objects
//        List listingsORANGErecus = new ArrayList();
//        //prepare
//        //Create a new list of student objects
////        List listingsOrange = new ArrayList();
//
//        table1 = new PdfPTable(6);
//        table1.setWidthPercentage(100);
//        c2 = new PdfPCell(new Phrase("Numéro appelé", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        c2 = new PdfPCell(new Phrase("Localisation numéro Appelé", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        c2 = new PdfPCell(new Phrase("IMEI numéro Appelé", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        c2 = new PdfPCell(new Phrase("Date début appel", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        c2 = new PdfPCell(new Phrase("Durée de l'appel", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        c2 = new PdfPCell(new Phrase("Numéro appellant", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        i = 0;
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//            inputcallrecus = line.split(",", -1);
////            && (!"CALLDURATION".equals(inputcallrecus[1])) && (!"0".equals(i))
//            if (!"".equals(inputcallrecus[1]) ) {
//                duree = ConvertirDuree(inputcallrecus[1]);
//                inputcallrecus[1] = duree;
//            }
//
//            table1 = po.PrintAddListingOrangeRecus(listDoc[k], inputcallrecus, table1);
//            listingOrangeEmis listorangeemis = new listingOrangeEmis(inputcallrecus[2], inputcallrecus[8] + " " + inputcallrecus[9], inputcallrecus[6], inputcallrecus[0], inputcallrecus[1], inputcallrecus[3]);
//            listingsORANGErecus.add(listorangeemis);
//            i++;
//        }
//
//        if ("true".equals(identification)) {
//            TitrePartie(listDoc[k], "III. Listing Appels Recus");
//
//        } else {
//            TitrePartie(listDoc[k], "II. Listing Appels Recus");
//
//        }
//        listDoc[k].add(table1);
//        System.out.println("listDoc[k] PrintAddListingOrangeRecus ");
            //create csv
//        CsvFileWriterORANGE.writeCsvFileOrangeListingRecus(num, listingsORANGErecus);
            //SMS gestions
            out = sftpChannel.get(listingsms);
            br = new BufferedReader(new InputStreamReader(out));
            String[] inputsms = null;
            //
            //prepare

            //print Abonné
            List ListOrangeSMS = new ArrayList();
            //Create a new list of student objects
            PdfPTable table2 = new PdfPTable(5);
            int[] widthsListingSMS = {1, 2, 1, 1, 1};
            table2.setWidths(widthsListingSMS);
            table2.setWidthPercentage(100);
            c2 = new PdfPCell(new Phrase("Numéro d'envoi", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(c2);
            c2 = new PdfPCell(new Phrase("Localisation numéro Destination", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(c2);
            c2 = new PdfPCell(new Phrase("IMEI Numéro Destination", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date sms", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(c2);
            c2 = new PdfPCell(new Phrase("Numéro destination", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(c2);
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                inputsms = line.split(",", -1);
                table2 = po.PrintAddListingOrangeSMS(listDoc[k], inputsms, table2);
                String location = inputsms[6] + " " + inputsms[7];
                String finalLocation = "--";
                if (!location.contains("null")) {
                    finalLocation = location + " (Cell: " + inputsms[5] + " Long: " + inputsms[8] + " Lat: " + inputsms[9] + " Azimut: -)";
                }
                listingOrangeSMS listingmtn = new listingOrangeSMS(inputsms[4], finalLocation, inputsms[10], inputsms[0], inputsms[13]);
                ListOrangeSMS.add(listingmtn);
            }

            if ("true".equals(identification)) {
                TitrePartie(listDoc[k], "III. Listing SMS");
            } else {
                TitrePartie(listDoc[k], "II. Listing SMS");
            }
//        if ("true".equals(identification)) {
//            TitrePartie(listDoc[k], "IV. Listing SMS");
//        } else {
//            TitrePartie(listDoc[k], "III. Listing SMS");
//        }
            listDoc[k].add(table2);
            //ecrire csv file
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date date = new Date();

//        String fileName = "Requisition" + num + ".csv";
            System.out.println("Write CSV file: sms");

            CsvFileWriterORANGEMultiple.writeCsvFileOrangeListingSMS(num, ListOrangeSMS, this.dateRequisition);
            //print Abonné
            List abonnesOrange = new ArrayList();
            out = sftpChannel.get(identiteAbonne);
            br = new BufferedReader(new InputStreamReader(out));
            String[] IdAbonne = null;

            //prepare 
            PdfPTable tableAbonne = new PdfPTable(6);
            tableAbonne.setWidthPercentage(100);
            c2 = new PdfPCell(new Phrase("Numéro", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Nom & Prénom", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date de naissance", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Numéro CNI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date expiration CNI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Quartier", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                IdAbonne = line.split(",", -1);
                if (IdAbonne.length >= 6) {
                    tableAbonne = po.PrintAddIdAbonneOrange(listDoc[k], IdAbonne, tableAbonne);
                    identificationORANGE listid = new identificationORANGE(IdAbonne[0], IdAbonne[2], IdAbonne[3], IdAbonne[1], IdAbonne[4], IdAbonne[5]);
                    abonnesOrange.add(listid);
                }
            }

            if ("true".equals(identification)) {
                TitrePartie(listDoc[k], "IV. Identification des numéros  ");

                listDoc[k].add(tableAbonne);
            }

            System.out.println("Write CSV file ");

            CsvFileWriterORANGEMultiple.writeCsvFileAbonneOrange(num, abonnesOrange, this.dateRequisition);

            TitrePartie(listDoc[k], "V. Fréquence par cellule");

            //print frequence par cellule
            List<FreqCell> StatsMTN = new ArrayList<>();

            out = sftpChannel.get(frequenceCellule);
            br = new BufferedReader(new InputStreamReader(out));
            String[] tabstat = null;
//
//        //prepae 
            PdfPTable tableFreqCell = new PdfPTable(14);
            int[] widths = {1, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
            tableFreqCell.setWidthPercentage(100);
            tableFreqCell.setWidths(widths);

            c2 = new PdfPCell(new Phrase("Total", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("Cellule", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("0h-2h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("2h-4h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("4h-6h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("6h-8h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("8h-10h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("10h-12h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("12h-14h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("14h-16h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("16h-18h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("18h-20h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("20h-22h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("22h-24h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            i = 0;
//
            while ((line = br.readLine()) != null) {
//                System.out.println(line);

                try {
                    tabstat = line.split(",");
                    for (int n = 5; n <= 16; n++) {
                        if (tabstat[n].equals("null")) {
                            tabstat[n] = "-1";
                        }
                    }
                    String location;
                    if (tabstat[1].equals("null")) {
                        location = "Site inconnu";
                        tabstat[1] = "Site inconnu";
                        tabstat[2] = "";
                    } else {
                        location = tabstat[1] + " " + tabstat[2] + " Long: " + tabstat[3] + " Lat: " + tabstat[4] + " Azimut: -";
                    }
                    tableFreqCell = po.PrintAddFreqCelluleOrange(listDoc[k], tabstat, tableFreqCell, i + 1);
                    FreqCell freq = new FreqCell(Integer.parseInt(tabstat[0]), location, Integer.parseInt(tabstat[5]), Integer.parseInt(tabstat[6]), Integer.parseInt(tabstat[7]), Integer.parseInt(tabstat[8]), Integer.parseInt(tabstat[9]), Integer.parseInt(tabstat[10]), Integer.parseInt(tabstat[11]), Integer.parseInt(tabstat[12]), Integer.parseInt(tabstat[13]), Integer.parseInt(tabstat[14]), Integer.parseInt(tabstat[15]), Integer.parseInt(tabstat[16]));
//                    StatistiqueAppels listid = new StatistiqueAppels(tabstat[2], Integer.parseInt(tabstat[0]), tabstat[1]);
                    StatsMTN.add(freq);
                    i++;
                } catch (BadElementException | IOException e) {
                }

            }
            listDoc[k].add(tableFreqCell);
//            StatsMTN.sort(Comparator.comparingInt(StatistiqueAppels::getOccurence));

////        Collections.sort(arraylist, listid.get);
//
            System.out.println("Write CSV file: Stat 1");
            CsvFileWriterORANGEMultiple.writeCsvFileFrequenceCelluleOrange(num, StatsMTN, this.dateRequisition);

            TitrePartie(listDoc[k], "VI. Fréquence par correspondant");
////        //print statistiques
            List<FreqCorrespondant> freqCorrespList = new ArrayList<>();

            out = sftpChannel.get(frequenceCorrespondance);
            br = new BufferedReader(new InputStreamReader(out));
            String[] tab;
//
//        //prepae 
            PdfPTable tableFreqCorresp = new PdfPTable(17);
            int[] widthsFreqCorresp = {1, 2, 2, 3, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
            tableFreqCorresp.setWidthPercentage(100);
            tableFreqCorresp.setWidths(widthsFreqCorresp);

            c2 = new PdfPCell(new Phrase("Total", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("Total Entrant", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("Total Sortant", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("Téléphone", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("Identité", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("0h-2h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("2h-4h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("4h-6h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("6h-8h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("8h-10h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("10h-12h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("12h-14h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("14h-16h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("16h-18h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("18h-20h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("20h-22h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("22h-24h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);

            i = 0;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);

                try {
                    tab = line.split(",");
                    tableFreqCorresp = po.PrintAddFreqCorrespondantOrange(listDoc[k], tab, tableFreqCorresp, i + 1);
//                    StatistiqueMTNLieux11 listid = new StatistiqueMTNLieux11(tab[1], Integer.parseInt(tabstatLieux[0]));
                    String ident;
                    if (tab[4].equals("null")) {
                        ident = "Inconnu";
                    } else {
                        ident = tab[4];
                    }

                    for (int j = 9; j <= 20; j++) {
                        if (tab[j].equals("null")) {
                            tab[j] = "-1";
                        }
                    }
                    for (int j = 0; j <= 2; j++) {
                        if (tab[j].equals("null")) {
                            tab[j] = "-1";
                        }
                    }
                    FreqCorrespondant freq = new FreqCorrespondant(Integer.parseInt(tab[0]), Integer.parseInt(tab[1]), Integer.parseInt(tab[2]), tab[3], ident, Integer.parseInt(tab[9]), Integer.parseInt(tab[10]), Integer.parseInt(tab[11]), Integer.parseInt(tab[12]), Integer.parseInt(tab[13]), Integer.parseInt(tab[14]), Integer.parseInt(tab[15]), Integer.parseInt(tab[16]), Integer.parseInt(tab[17]), Integer.parseInt(tab[18]), Integer.parseInt(tab[19]), Integer.parseInt(tab[20]));
                    freqCorrespList.add(freq);
                    i++;
                } catch (BadElementException | IOException e) {
                }

            }
            listDoc[k].add(tableFreqCorresp);

//            StatsMTNlieux.sort(Comparator.comparingInt(StatistiqueMTNLieux11::getOccurence));
            System.out.println("Write CSV file: Freq Corresp.");
            CsvFileWriterORANGEMultiple.writeCsvFileFreqCorrespondantOrange(num, freqCorrespList, this.dateRequisition);

            TitrePartie(listDoc[k], "VII. Fréquence par durée d'appel");
////        //print statistiques
            List<FreqDureeAppel> freqDureeAppelList = new ArrayList<>();

            out = sftpChannel.get(frequenceDureeAppel);
            br = new BufferedReader(new InputStreamReader(out));
//
//        //prepae 
            PdfPTable tableFreqDureeAppel = new PdfPTable(4);
            tableFreqDureeAppel.setWidthPercentage(100);

            c2 = new PdfPCell(new Phrase("Numéro", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqDureeAppel.addCell(c2);
            c2 = new PdfPCell(new Phrase("Identité", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqDureeAppel.addCell(c2);
            c2 = new PdfPCell(new Phrase("Durée des appels", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqDureeAppel.addCell(c2);
            c2 = new PdfPCell(new Phrase("Nombre de messages", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqDureeAppel.addCell(c2);

            i = 0;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);

                try {
                    tab = line.split(",");
                    String duration = ConvertirDuree(tab[6]);
                    tab[6] = duration;
                    tableFreqDureeAppel = po.PrintAddFreqDureeAppelOrange(listDoc[k], tab, tableFreqDureeAppel, i + 1);
//                    StatistiqueMTNLieux11 listid = new StatistiqueMTNLieux11(tab[1], Integer.parseInt(tabstatLieux[0]));
                    String ident;
                    if (tab[1].equals("null")) {
                        ident = "Inconnu";
                    } else {
                        ident = tab[1];
                    }

                    FreqDureeAppel freq = new FreqDureeAppel(tab[0], ident, duration, Integer.parseInt(tab[7]));
                    freqDureeAppelList.add(freq);
                    i++;
                } catch (BadElementException | IOException e) {
                }

            }
            listDoc[k].add(tableFreqDureeAppel);

//            StatsMTNlieux.sort(Comparator.comparingInt(StatistiqueMTNLieux11::getOccurence));
            System.out.println("Write CSV file: Freq Duree Appel.");
            CsvFileWriterORANGEMultiple.writeCsvFileFreqDureeAppelOrange(num, freqDureeAppelList, this.dateRequisition);

            TitrePartie(listDoc[k], "VIII. Fréquence par IMEI");
////        //print statistiques
            List<FreqImei> freqImeiList = new ArrayList<>();

            out = sftpChannel.get(frequenceImei);
            br = new BufferedReader(new InputStreamReader(out));
//
//        //prepae 
            PdfPTable tableFreqImei = new PdfPTable(4);
            tableFreqImei.setWidthPercentage(100);

            c2 = new PdfPCell(new Phrase("Total", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqImei.addCell(c2);
            c2 = new PdfPCell(new Phrase("IMEI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqImei.addCell(c2);
            c2 = new PdfPCell(new Phrase("Première utilisation", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqImei.addCell(c2);
            c2 = new PdfPCell(new Phrase("Dernière utilisation", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqImei.addCell(c2);
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            i = 0;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);

                try {
                    tab = line.split(",");
                    tableFreqImei = po.PrintAddFreqImeiOrange(listDoc[k], tab, tableFreqImei, i + 1);
                    Date dateDebut = null;
                    Date dateFin = null;
                    try {
                        dateDebut = df.parse(tab[2]);
                        dateFin = df.parse(tab[3]);
                    } catch (ParseException ex) {
                        Logger.getLogger(RemoteRequiORANGE.class.getName()).log(Level.SEVERE, null, ex);
                    }
//                    StatistiqueMTNLieux11 listid = new StatistiqueMTNLieux11(tab[1], Integer.parseInt(tabstatLieux[0]));
                    FreqImei freq = new FreqImei(Integer.parseInt(tab[0]), tab[1], dateDebut, dateFin);
                    freqImeiList.add(freq);
                    i++;
                } catch (BadElementException | IOException e) {
                }

            }
            listDoc[k].add(tableFreqImei);

//            StatsMTNlieux.sort(Comparator.comparingInt(StatistiqueMTNLieux11::getOccurence));
            System.out.println("Write CSV file: Freq Duree Appel.");
            CsvFileWriterORANGEMultiple.writeCsvFileFreqImeiOrange(num, freqImeiList, this.dateRequisition);

            br.close();
            k++;
        }
    }

    public void lireFicOrange_Imei_Multiple(String num, Session session, Document[] listDoc, PrintOrangeMultiple po, String identification, int nbNum, String[] ListeNum) throws JSchException, SftpException, IOException, BadElementException, DocumentException {

        int k = 0;
        while (k < nbNum) {
            num = ListeNum[k];
            System.out.println("numero " + num);
            System.out.println("Creating SFTP Channel.");
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            System.out.println("SFTP Channel created.");
            InputStream out = null;
            String basePath;
            if (Variables111.ACCOUNT_TYPE.equals("YAYAP")) {
                basePath = "/home/data/mtn/operations/cdr/"; // Pour le SED YAYAP
            } else {
                basePath = "/root/"; // Pour l'ARMP
            }
            String imeiPartage = basePath + this.dateRequisition + "/" + num + "/identite_numero.txt";
            String listingAppelEmis = basePath + this.dateRequisition + "/" + num + "/appelemis.txt";
//          String listingAppelRecus = "/root/" + num + "/appelrecus.txt";
            String listingsms = basePath + this.dateRequisition + "/" + num + "/smsfinal.txt";
            String identiteAbonne = basePath + this.dateRequisition + "/" + num + "/IdentificationAbonneesfinal.txt";
            String frequenceCellule = basePath + this.dateRequisition + "/" + num + "/frequenceCellule.txt";
            String frequenceCorrespondance = basePath + this.dateRequisition + "/" + num + "/frequenceCorrespondant.txt";
//            String frequenceDureeAppel = basePath + this.dateRequisition + "/" + num + "/frequenceDureeAppel.txt";
//            String frequenceImei = basePath + this.dateRequisition + "/" + num + "/frequenceImei.txt";

            System.out.println("identite_numero" + imeiPartage);
            out = sftpChannel.get(imeiPartage);
            BufferedReader br = new BufferedReader(new InputStreamReader(out));
            String line;
            String[] inputNumber = null;

            List imeiPartageOrange = new ArrayList();
            TitrePartie(listDoc[k], "I. Imei partagé: différent(s) utilisateur(s) du téléphone");

            PdfPTable tableImeiPartage = new PdfPTable(6);
            int[] widthsImeiPartage = {1, 1, 4, 1, 2, 2};
            tableImeiPartage.setWidths(widthsImeiPartage);
            tableImeiPartage.setWidthPercentage(100);
            PdfPCell c2 = new PdfPCell(new Phrase("Numéro", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableImeiPartage.addCell(c2);
            c2 = new PdfPCell(new Phrase("IMEI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableImeiPartage.addCell(c2);
            c2 = new PdfPCell(new Phrase("Identité", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableImeiPartage.addCell(c2);
            c2 = new PdfPCell(new Phrase("Occurrence", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableImeiPartage.addCell(c2);
            c2 = new PdfPCell(new Phrase("Première utilisation", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableImeiPartage.addCell(c2);
            c2 = new PdfPCell(new Phrase("Dernière utilisation", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableImeiPartage.addCell(c2);
            int ii = 1;

            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                inputNumber = line.split(",");
                if (ii != 0) {
                    //convertir 
                    tableImeiPartage = po.PrintAddImeiPartageOrange(listDoc[k], inputNumber, tableImeiPartage);

                    String identite = inputNumber[2] + "(CNI: " + inputNumber[4] + " Date nais: " + inputNumber[3] + " Adresse: " + inputNumber[6] + ")";
                    if (identite.contains("null")) {
                        identite = "inconnu";
                    }
                    SharedImei sharedImei = new SharedImei(inputNumber[0], inputNumber[1], identite, inputNumber[7], inputNumber[8], inputNumber[9]);
                    imeiPartageOrange.add(sharedImei);
                }
                ii++;
            }

            listDoc[k].add(tableImeiPartage);

            //create csv
            CsvFileWriterORANGEMultiple.writeCsvFileSharedImeiOrange(num, imeiPartageOrange, this.dateRequisition);

            //II. Listing  Appels Emis
            out = sftpChannel.get(listingAppelEmis);
            br = new BufferedReader(new InputStreamReader(out));
            String[] inputcallEmis = null;

            //prepareCSV
            //prepare
            //Create a new list of student objects
            List listingsORANGEemis = new ArrayList();

            PdfPTable table1 = new PdfPTable(6);
            int[] widthsListing = {1, 2, 1, 1, 1, 1};
            table1.setWidths(widthsListing);
            table1.setWidthPercentage(100);
            c2 = new PdfPCell(new Phrase("Numéro appellant", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("Localisation numéro appelant", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("IMEI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date début appel", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("Durée de l'appel", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("Numéro appelé", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            int i = 0;
            String duree = null;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                inputcallEmis = line.split(",", -1);
//                if (inputcallEmis.length >= 10) {
                //convertir && (!"CALL_DURATION".equals(inputcall[3])))
                if (!"".equals(inputcallEmis[1])) {
                    duree = ConvertirDuree(inputcallEmis[1]);
                    inputcallEmis[1] = duree;
                }
                table1 = po.PrintAddListingOrangeEmis(listDoc[k], inputcallEmis, table1);
                String location = inputcallEmis[5] + " " + inputcallEmis[6];
                String finalLocation = "--";
                if (!location.contains("null")) {
                    finalLocation = location + " (Cell: " + inputcallEmis[4] + " Long: " + inputcallEmis[7] + " Lat: " + inputcallEmis[8] + " Azimut: -)";
                }
                listing listorangeemis = new listing(inputcallEmis[3], finalLocation, inputcallEmis[9], inputcallEmis[0], inputcallEmis[1], inputcallEmis[2]);
                listingsORANGEemis.add(listorangeemis);
                i++;
//                }
            }
            if ("true".equals(identification)) {
                TitrePartie(listDoc[k], "II. Listing Appels ");

            } else {
                TitrePartie(listDoc[k], "I. Listing Appels ");

            }
//        if ("true".equals(identification)) {
//            TitrePartie(listDoc[k], "II. Listing Appels Emis");
//
//        } else {
//            TitrePartie(listDoc[k], "I. Listing Appels Emis");
//
//        }

            listDoc[k].add(table1);
            System.out.println("listDoc[k] PrintAddListingOrangeEmis ");

            //create csv
            CsvFileWriterORANGEMultiple.writeCsvFileOrangeListingEmis(num, listingsORANGEemis, this.dateRequisition);

            //III. Listing Appels Recus
//        out = sftpChannel.get(listingAppelRecus);
//        br = new BufferedReader(new InputStreamReader(out));
//        String[] inputcallrecus = null;
//
//        //Create a new list of student objects
//        List listingsORANGErecus = new ArrayList();
//        //prepare
//        //Create a new list of student objects
////        List listingsOrange = new ArrayList();
//
//        table1 = new PdfPTable(6);
//        table1.setWidthPercentage(100);
//        c2 = new PdfPCell(new Phrase("Numéro appelé", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        c2 = new PdfPCell(new Phrase("Localisation numéro Appelé", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        c2 = new PdfPCell(new Phrase("IMEI numéro Appelé", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        c2 = new PdfPCell(new Phrase("Date début appel", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        c2 = new PdfPCell(new Phrase("Durée de l'appel", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        c2 = new PdfPCell(new Phrase("Numéro appellant", smallBold));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table1.addCell(c2);
//        i = 0;
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//            inputcallrecus = line.split(",", -1);
////            && (!"CALLDURATION".equals(inputcallrecus[1])) && (!"0".equals(i))
//            if (!"".equals(inputcallrecus[1]) ) {
//                duree = ConvertirDuree(inputcallrecus[1]);
//                inputcallrecus[1] = duree;
//            }
//
//            table1 = po.PrintAddListingOrangeRecus(listDoc[k], inputcallrecus, table1);
//            listingOrangeEmis listorangeemis = new listingOrangeEmis(inputcallrecus[2], inputcallrecus[8] + " " + inputcallrecus[9], inputcallrecus[6], inputcallrecus[0], inputcallrecus[1], inputcallrecus[3]);
//            listingsORANGErecus.add(listorangeemis);
//            i++;
//        }
//
//        if ("true".equals(identification)) {
//            TitrePartie(listDoc[k], "III. Listing Appels Recus");
//
//        } else {
//            TitrePartie(listDoc[k], "II. Listing Appels Recus");
//
//        }
//        listDoc[k].add(table1);
//        System.out.println("listDoc[k] PrintAddListingOrangeRecus ");
            //create csv
//        CsvFileWriterORANGE.writeCsvFileOrangeListingRecus(num, listingsORANGErecus);
            //SMS gestions
            out = sftpChannel.get(listingsms);
            br = new BufferedReader(new InputStreamReader(out));
            String[] inputsms = null;
            //
            //prepare

            //print Abonné
            List ListOrangeSMS = new ArrayList();
            //Create a new list of student objects
            PdfPTable table2 = new PdfPTable(5);
            int[] widthsListingSMS = {1, 2, 1, 1, 1};
            table2.setWidths(widthsListingSMS);
            table2.setWidthPercentage(100);
            c2 = new PdfPCell(new Phrase("Numéro d'envoi", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(c2);
            c2 = new PdfPCell(new Phrase("Localisation numéro Destination", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(c2);
            c2 = new PdfPCell(new Phrase("IMEI Numéro Destination", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date sms", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(c2);
            c2 = new PdfPCell(new Phrase("Numéro destination", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(c2);
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                inputsms = line.split(",", -1);
                table2 = po.PrintAddListingOrangeSMS(listDoc[k], inputsms, table2);
                String location = inputsms[6] + " " + inputsms[7];
                String finalLocation = "--";
                if (!location.contains("null")) {
                    finalLocation = location + " (Cell: " + inputsms[5] + " Long: " + inputsms[8] + " Lat: " + inputsms[9] + " Azimut: -)";
                }
                listingOrangeSMS listingmtn = new listingOrangeSMS(inputsms[4], finalLocation, inputsms[10], inputsms[0], inputsms[13]);
                ListOrangeSMS.add(listingmtn);
            }

            if ("true".equals(identification)) {
                TitrePartie(listDoc[k], "III. Listing SMS");
            } else {
                TitrePartie(listDoc[k], "II. Listing SMS");
            }
//        if ("true".equals(identification)) {
//            TitrePartie(listDoc[k], "IV. Listing SMS");
//        } else {
//            TitrePartie(listDoc[k], "III. Listing SMS");
//        }
            listDoc[k].add(table2);
            //ecrire csv file
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date date = new Date();

//        String fileName = "Requisition" + num + ".csv";
            System.out.println("Write CSV file: sms");

            CsvFileWriterORANGEMultiple.writeCsvFileOrangeListingSMS(num, ListOrangeSMS, this.dateRequisition);
            //print Abonné
            List abonnesOrange = new ArrayList();
            out = sftpChannel.get(identiteAbonne);
            br = new BufferedReader(new InputStreamReader(out));
            String[] IdAbonne = null;

            //prepare 
            PdfPTable tableAbonne = new PdfPTable(6);
            tableAbonne.setWidthPercentage(100);
            c2 = new PdfPCell(new Phrase("Numéro", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Nom & Prénom", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date de naissance", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Numéro CNI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date expiration CNI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Quartier", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                IdAbonne = line.split(",", -1);
                if (IdAbonne.length >= 6) {
                    tableAbonne = po.PrintAddIdAbonneOrange(listDoc[k], IdAbonne, tableAbonne);
                    identificationORANGE listid = new identificationORANGE(IdAbonne[0], IdAbonne[2], IdAbonne[3], IdAbonne[1], IdAbonne[4], IdAbonne[5]);
                    abonnesOrange.add(listid);
                }
            }

            if ("true".equals(identification)) {
                TitrePartie(listDoc[k], "IV. Identification des numéros  ");

                listDoc[k].add(tableAbonne);
            }

            System.out.println("Write CSV file ");

            CsvFileWriterORANGEMultiple.writeCsvFileAbonneOrange(num, abonnesOrange, this.dateRequisition);

            TitrePartie(listDoc[k], "V. Fréquence par cellule");

            //print frequence par cellule
            List<FreqCell> StatsMTN = new ArrayList<>();

            out = sftpChannel.get(frequenceCellule);
            br = new BufferedReader(new InputStreamReader(out));
            String[] tabstat = null;
//
//        //prepae 
            PdfPTable tableFreqCell = new PdfPTable(14);
            int[] widths = {1, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
            tableFreqCell.setWidthPercentage(100);
            tableFreqCell.setWidths(widths);

            c2 = new PdfPCell(new Phrase("Total", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("Cellule", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("0h-2h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("2h-4h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("4h-6h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("6h-8h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("8h-10h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("10h-12h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("12h-14h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("14h-16h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("16h-18h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("18h-20h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("20h-22h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            c2 = new PdfPCell(new Phrase("22h-24h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCell.addCell(c2);
            i = 0;
//
            while ((line = br.readLine()) != null) {
//                System.out.println(line);

                try {
                    tabstat = line.split(",");
                    for (int n = 5; n <= 16; n++) {
                        if (tabstat[n].equals("null")) {
                            tabstat[n] = "-1";
                        }
                    }
                    String location;
                    if (tabstat[1].equals("null")) {
                        location = "Site inconnu";
                        tabstat[1] = "Site inconnu";
                        tabstat[2] = "";
                    } else {
                        location = tabstat[1] + " " + tabstat[2] + " Long: " + tabstat[3] + " Lat: " + tabstat[4] + " Azimut: -";
                    }
                    tableFreqCell = po.PrintAddFreqCelluleOrange(listDoc[k], tabstat, tableFreqCell, i + 1);
                    FreqCell freq = new FreqCell(Integer.parseInt(tabstat[0]), location, Integer.parseInt(tabstat[5]), Integer.parseInt(tabstat[6]), Integer.parseInt(tabstat[7]), Integer.parseInt(tabstat[8]), Integer.parseInt(tabstat[9]), Integer.parseInt(tabstat[10]), Integer.parseInt(tabstat[11]), Integer.parseInt(tabstat[12]), Integer.parseInt(tabstat[13]), Integer.parseInt(tabstat[14]), Integer.parseInt(tabstat[15]), Integer.parseInt(tabstat[16]));
//                    StatistiqueAppels listid = new StatistiqueAppels(tabstat[2], Integer.parseInt(tabstat[0]), tabstat[1]);
                    StatsMTN.add(freq);
                    i++;
                } catch (BadElementException | IOException e) {
                }

            }
            listDoc[k].add(tableFreqCell);
//            StatsMTN.sort(Comparator.comparingInt(StatistiqueAppels::getOccurence));

////        Collections.sort(arraylist, listid.get);
//
            System.out.println("Write CSV file: Stat 1");
            CsvFileWriterORANGEMultiple.writeCsvFileFrequenceCelluleOrange(num, StatsMTN, this.dateRequisition);

            TitrePartie(listDoc[k], "VI. Fréquence par correspondant");
////        //print statistiques
            List<FreqCorrespondant> freqCorrespList = new ArrayList<>();

            out = sftpChannel.get(frequenceCorrespondance);
            br = new BufferedReader(new InputStreamReader(out));
            String[] tab;
//
//        //prepae 
            PdfPTable tableFreqCorresp = new PdfPTable(17);
            int[] widthsFreqCorresp = {1, 2, 2, 3, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
            tableFreqCorresp.setWidthPercentage(100);
            tableFreqCorresp.setWidths(widthsFreqCorresp);

            c2 = new PdfPCell(new Phrase("Total", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("Total Entrant", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("Total Sortant", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("Téléphone", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("Identité", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("0h-2h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("2h-4h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("4h-6h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("6h-8h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("8h-10h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("10h-12h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("12h-14h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("14h-16h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("16h-18h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("18h-20h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("20h-22h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);
            c2 = new PdfPCell(new Phrase("22h-24h", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableFreqCorresp.addCell(c2);

            i = 0;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);

                try {
                    tab = line.split(",");
                    tableFreqCorresp = po.PrintAddFreqCorrespondantOrange(listDoc[k], tab, tableFreqCorresp, i + 1);
//                    StatistiqueMTNLieux11 listid = new StatistiqueMTNLieux11(tab[1], Integer.parseInt(tabstatLieux[0]));
                    String ident;
                    if (tab[4].equals("null")) {
                        ident = "Inconnu";
                    } else {
                        ident = tab[4];
                    }

                    for (int j = 9; j <= 20; j++) {
                        if (tab[j].equals("null")) {
                            tab[j] = "-1";
                        }
                    }
                    for (int j = 0; j <= 2; j++) {
                        if (tab[j].equals("null")) {
                            tab[j] = "-1";
                        }
                    }
                    FreqCorrespondant freq = new FreqCorrespondant(Integer.parseInt(tab[0]), Integer.parseInt(tab[1]), Integer.parseInt(tab[2]), tab[3], ident, Integer.parseInt(tab[9]), Integer.parseInt(tab[10]), Integer.parseInt(tab[11]), Integer.parseInt(tab[12]), Integer.parseInt(tab[13]), Integer.parseInt(tab[14]), Integer.parseInt(tab[15]), Integer.parseInt(tab[16]), Integer.parseInt(tab[17]), Integer.parseInt(tab[18]), Integer.parseInt(tab[19]), Integer.parseInt(tab[20]));
                    freqCorrespList.add(freq);
                    i++;
                } catch (BadElementException | IOException e) {
                }

            }
            listDoc[k].add(tableFreqCorresp);

//            StatsMTNlieux.sort(Comparator.comparingInt(StatistiqueMTNLieux11::getOccurence));
            System.out.println("Write CSV file: Freq Corresp.");
            CsvFileWriterORANGEMultiple.writeCsvFileFreqCorrespondantOrange(num, freqCorrespList, this.dateRequisition);

            br.close();
            k++;
        }
    }

    public String ConvertirDuree(String value) {
        int input = Integer.parseInt(value.trim());

        int hours = input / 3600;
        int minutes = (input % 3600) / 60;
        int seconds = (input % 3600) % 60;

        return hours + ":" + minutes + ":" + seconds;
    }
}
