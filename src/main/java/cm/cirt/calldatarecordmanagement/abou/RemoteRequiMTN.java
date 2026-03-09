/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.abou;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import cm.cirt.calldatarecordmanagement.abou.csv.CsvFileWriterMTN;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.StatistiqueAppels;
import cm.cirt.calldatarecordmanagement.abou.cm.backup.StatistiqueMTNLieux11;
import cm.cirt.calldatarecordmanagement.abou.csv.CsvFileWriterMTNMultiple;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.FreqCell;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.FreqCorrespondant;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.FreqDureeAppel;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.FreqImei;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.SharedImei;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.identificationMTN;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.listing;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import cm.cirt.calldatarecordmanagement.abou.print.PrintMTN;
import static cm.cirt.calldatarecordmanagement.abou.print.PrintMTN.TitrePartie;
import static cm.cirt.calldatarecordmanagement.abou.print.PrintMTN.smallBold;
import cm.cirt.calldatarecordmanagement.abou.print.PrintMTNMultiple;
import cm.cirt.calldatarecordmanagement.metier.Variables111;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antic
 */
public class RemoteRequiMTN {

    String dateRequisition;

    public RemoteRequiMTN() {
    }

    public RemoteRequiMTN(String dateRequisition) {
        this.dateRequisition = dateRequisition;
    }

    public String getDateRequisition() {
        return dateRequisition;
    }

    public void setDateRequisition(String dateRequisition) {
        this.dateRequisition = dateRequisition;
    }

    public void remoteFind_MTN(String[] args, Document document, PrintMTN pm, String identification) throws SftpException, BadElementException, DocumentException {

        JSch jsch = new JSch();

//        All0peratorRequisition Allreq = new All0peratorRequisition();
//        Allreq.setVisible(true);
        String command = "sh requisitionMTN.sh " + args[0] + " " + args[1] + " " + args[2];
//        String command = "pwd";
        System.out.println("command :" + command);
        Session session;
        try {
            //Open a Session to remote SSH server and Connect.
            //Set User and IP of the remote host and SSH port.
            session = jsch.getSession(Variables111.MTN_USER, Variables111.MTN_HOST, 22);
            //When we do SSH to a remote host for the 1st time or if key at the remote host 
            //Changes, we will be prompted to confirm the authenticity of remote host. 
            //This check feature is controlled by StrictHostKeyChecking ssh parameter. 
            //By default StrictHostKeyChecking  is set to yes as a security measure.
            session.setConfig("StrictHostKeyChecking", "no");
            //Set Password
String privateKeyPath = Variables.getIdRsaPath(); // Replace with your actual path
jsch.addIdentity(privateKeyPath);
            //session.setPassword(Variables111.MTN_PASSWORD);
            session.connect();

            //Create the execution channel over the session
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            //Set the command to execute on the channel and execute the command
            channelExec.setCommand(command);
            //ChannelExec.setCommand("sh myscript.sh Rajesh");
            channelExec.connect();

            //Get an InputStream from this channel and read messages, generated 
            //By the executing command, from the remote side.
            InputStream in = channelExec.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            //Command execution completed here.
            //Retrieve the exit status of the executed command
            int exitStatus = channelExec.getExitStatus();
            if (exitStatus > 0) {
                System.out.println("Remote script exec error! " + exitStatus);
            }

            //creation fichier
            this.lireficMTN(args[0], session, document, pm, identification);

            //Disconnect the Session
            session.disconnect();

        } catch (JSchException | IOException e) {
        }

    }

    public void remoteFind_MTN_Multiple(String[] args, Document[] listDoc, PrintMTNMultiple pm, String identification, int nbNum, String[] ListeNum) throws SftpException, BadElementException, DocumentException {

        JSch jsch = new JSch();

//        All0peratorRequisition Allreq = new All0peratorRequisition();
//        Allreq.setVisible(true);
        String command = "/home/data/mtn/operations/cdr/requisitionMTN_Multiple_Optimized.sh " + args[0] + " " + args[1] + " " + args[2] + " " + dateRequisition;
//        String command = "pwd";
        System.out.println("command :" + command);
        Session session;
        try {
            //Open a Session to remote SSH server and Connect.
            //Set User and IP of the remote host and SSH port.
            session = jsch.getSession(Variables111.MTN_USER, Variables111.MTN_HOST, 22);
            //When we do SSH to a remote host for the 1st time or if key at the remote host 
            //Changes, we will be prompted to confirm the authenticity of remote host. 
            //This check feature is controlled by StrictHostKeyChecking ssh parameter. 
            //By default StrictHostKeyChecking  is set to yes as a security measure.
            session.setConfig("StrictHostKeyChecking", "no");
            //Set Password
String privateKeyPath = Variables.getIdRsaPath(); // Replace with your actual path
jsch.addIdentity(privateKeyPath);
            //session.setPassword(Variables111.MTN_PASSWORD);
            session.connect();

            //Create the execution channel over the session
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            //Set the command to execute on the channel and execute the command
            channelExec.setCommand(command);
            //ChannelExec.setCommand("sh myscript.sh Rajesh");
            channelExec.connect();

            //Get an InputStream from this channel and read messages, generated 
            //By the executing command, from the remote side.
            InputStream in = channelExec.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            //Command execution completed here.
            //Retrieve the exit status of the executed command
            int exitStatus = channelExec.getExitStatus();
            if (exitStatus > 0) {
                System.out.println("Remote script exec error! " + exitStatus);
            }

            //creation fichier
            this.lireficMTN_Multiple(args[0], session, listDoc, pm, identification, nbNum, ListeNum);

            //Disconnect the Session
            session.disconnect();

        } catch (JSchException | IOException e) {
        }

    }

    public void remoteFind_MTN_Imei_Multiple(String[] args, Document[] listDoc, PrintMTNMultiple pm, String identification, int nbNum, String[] ListeNum) throws SftpException, BadElementException, DocumentException {

        JSch jsch = new JSch();

//        All0peratorRequisition Allreq = new All0peratorRequisition();
//        Allreq.setVisible(true);
        String command = "/home/data/mtn/operations/cdr/requisitionImeiMTN_Multiple_Optimized.sh " + args[0] + " " + args[1] + " " + args[2] + " " + dateRequisition;
//        String command = "pwd";
        System.out.println("command :" + command);
        Session session;
        try {
            //Open a Session to remote SSH server and Connect.
            //Set User and IP of the remote host and SSH port.
            session = jsch.getSession(Variables111.MTN_USER, Variables111.MTN_HOST, 22);
            //When we do SSH to a remote host for the 1st time or if key at the remote host 
            //Changes, we will be prompted to confirm the authenticity of remote host. 
            //This check feature is controlled by StrictHostKeyChecking ssh parameter. 
            //By default StrictHostKeyChecking  is set to yes as a security measure.
            session.setConfig("StrictHostKeyChecking", "no");
            //Set Password
String privateKeyPath = Variables.getIdRsaPath(); // Replace with your actual path
jsch.addIdentity(privateKeyPath);
            //session.setPassword(Variables111.MTN_PASSWORD);
            session.connect();

            //Create the execution channel over the session
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            //Set the command to execute on the channel and execute the command
            channelExec.setCommand(command);
            //ChannelExec.setCommand("sh myscript.sh Rajesh");
            channelExec.connect();

            //Get an InputStream from this channel and read messages, generated 
            //By the executing command, from the remote side.
            InputStream in = channelExec.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            //Command execution completed here.
            //Retrieve the exit status of the executed command
            int exitStatus = channelExec.getExitStatus();
            if (exitStatus > 0) {
                System.out.println("Remote script exec error! " + exitStatus);
            }

            //creation fichier
            this.lireficMTN_Imei_Multiple(args[0], session, listDoc, pm, identification, nbNum, ListeNum);

            //Disconnect the Session
            session.disconnect();

        } catch (JSchException | IOException e) {
        }

    }

    public void lireficMTN(String num, Session session, Document document, PrintMTN pm, String identification) throws JSchException, SftpException, IOException, BadElementException, DocumentException {

        System.out.println("Creating SFTP Channel. MTN");
        ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
        sftpChannel.connect();
        System.out.println("SFTP Channel created.");
        InputStream out = null;
        String identite_numero = "/root/" + num + "/identite_numero.txt";
//       fichier inter;ediqie String listing = "/root/" + num + "/listing_final";
        String listing = "/root/" + num + "/listing_final";
        String identiteAbonne = "/root/" + num + "/IdentificationAbonneesfinal.txt";
        String stat = "/root/" + num + "/NBnumero.txt";
        String statlieux = "/root/" + num + "/NBSites.txt";

//        create class
        out = sftpChannel.get(identite_numero);
        BufferedReader br = new BufferedReader(new InputStreamReader(out));
        String line;
        String[] inputNumber = null;
        System.out.println("1");
        while ((line = br.readLine()) != null) {
//            System.out.println(line);
            inputNumber = line.split(",");
        }

        if ("true".equals(identification)) {
            document = pm.PrintIDAbonneMTN(num, document, inputNumber);
//            document = pm.PrintIDCellulleMTN(num, document, inputNumber);
        }

        //create csv
        CsvFileWriterMTN.writeCsvFileMTNIDNumero(num, inputNumber, this.dateRequisition);

        out = sftpChannel.get(listing);
        br = new BufferedReader(new InputStreamReader(out));
        String[] inputcall = null;

// verifier si le fichier exite 
//        if (out.length() == 0) {
//            System.out.println("File is empty after creating it...");
//        } else {
//            System.out.println("File is not empty after creation...");
//        }
        //prepare
        //Create a new list of student objects
        List listingsMTN = new ArrayList();

        if ("true".equals(identification)) {
            TitrePartie(document, "II. Listing  ");
        } else {
            TitrePartie(document, "I. Listing  ");
        }

        PdfPTable table1 = new PdfPTable(6);
        table1.setWidthPercentage(100);
        PdfPCell c2 = new PdfPCell(new Phrase("Numero appellant", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1.addCell(c2);
        c2 = new PdfPCell(new Phrase("Localisation numero appelant", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1.addCell(c2);
        c2 = new PdfPCell(new Phrase("IMEI", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1.addCell(c2);
        c2 = new PdfPCell(new Phrase("Date debut appel", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1.addCell(c2);
        c2 = new PdfPCell(new Phrase("Duree de l'appel", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1.addCell(c2);
        c2 = new PdfPCell(new Phrase("Numero appelé", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1.addCell(c2);
        int i = 0;
        String duree = null;
        while ((line = br.readLine()) != null) {
//            System.out.println(line);
            inputcall = line.split(",");
//            if (i != 0) {
            //convertir 
            if (!"".equals(inputcall[3]) && (!"CALL_DURATION".equals(inputcall[3]))) {
                inputcall[3] = ConvertirDuree(inputcall[3]);
//                    inputcall[3] = duree;
            } else if ("".equals(inputcall[3])) {
                inputcall[3] = "SMS";
            }
            if (!"".equals(inputcall[2]) && (!"START_TIME".equals(inputcall[2]))) {
                inputcall[2] = ConvertDate(inputcall[2]);
//                    inputcall[3] = duree;
            }
            table1 = pm.PrintAddListingMTN(document, inputcall, table1);

            listing listingmtn = new listing(inputcall[0], inputcall[5] + " " + inputcall[6], inputcall[1], inputcall[2], inputcall[3], inputcall[4]);
            listingsMTN.add(listingmtn);
//            }
            i++;
        }

        document.add(table1);

        //ecrire csv file
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();

        System.out.println("date " + date);
        CsvFileWriterMTN.writeCsvFileMTNListing(num, listingsMTN, this.dateRequisition);

//print Abonné
        List abonnesMTN = new ArrayList();

        out = sftpChannel.get(identiteAbonne);
        br = new BufferedReader(new InputStreamReader(out));
        String[] IdAbonne = null;

        //prepae  "III. Identification des numeros  ");
        PdfPTable tableAbonne = new PdfPTable(7);
        tableAbonne.setWidthPercentage(100);
        c2 = new PdfPCell(new Phrase("Numéro", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);
        c2 = new PdfPCell(new Phrase("Nom & Prenom", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);
        c2 = new PdfPCell(new Phrase("Date de naissance", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);
        c2 = new PdfPCell(new Phrase("Numero CNI", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);
        c2 = new PdfPCell(new Phrase("Date expiration CNI", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);
        c2 = new PdfPCell(new Phrase("Quartier", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);
        c2 = new PdfPCell(new Phrase("Nationalite", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);

        while ((line = br.readLine()) != null) {
//            System.out.println(line);

            try {
                IdAbonne = line.split(",");
                tableAbonne = pm.PrintAddIdAbonneMTN(document, IdAbonne, tableAbonne);
                identificationMTN listid = new identificationMTN(IdAbonne[0], IdAbonne[3], IdAbonne[4], IdAbonne[1], IdAbonne[5], IdAbonne[6], IdAbonne[2]);
                abonnesMTN.add(listid);
            } catch (Exception e) {
            }

        }
        if ("true".equals(identification)) {
            TitrePartie(document, "III. Identification des numeros  ");

            document.add(tableAbonne);
        }
        System.out.println("Write CSV file: Abonne");
        CsvFileWriterMTN.writeCsvFileAbonneMTN(num, abonnesMTN, this.dateRequisition);

        TitrePartie(document, "IV. Statistiques");
//

        TitrePartie(document, "IV.1 Statistique sur la frequence des numeros");
        //print statistiques
        List StatsMTN = new ArrayList();

        out = sftpChannel.get(stat);
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
        c2 = new PdfPCell(new Phrase("Occurence(Appels&SMS)", smallBold));
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
                    duree = ConvertirDuree(tabstat[1]);
                    tabstat[1] = duree;
                }
                tablestat = pm.PrintAddStatsMTN(document, tabstat, tablestat, i + 1);
                StatistiqueAppels listid = new StatistiqueAppels(tabstat[2], Integer.parseInt(tabstat[0]), tabstat[1]);
                StatsMTN.add(listid);
                i++;
            } catch (BadElementException | IOException e) {
            }

        }
        document.add(tablestat);
        StatsMTN.sort(Comparator.comparingInt(StatistiqueAppels::getOccurence));

////        Collections.sort(arraylist, listid.get);
//
        System.out.println("Write CSV file: Stat 1");
        CsvFileWriterMTN.writeCsvFileStatsMTN(num, StatsMTN, this.dateRequisition);

        TitrePartie(document, "IV.2 Statistique sur la frequence des Lieux d'appels");
////        //print statistiques
        List StatsMTNlieux = new ArrayList();

        out = sftpChannel.get(statlieux);
        br = new BufferedReader(new InputStreamReader(out));
        String[] tabstatLieux;
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
                tabstatLieux = line.split(",");
                tablestatlieux = pm.PrintAddStatsMTNLieux(document, tabstatLieux, tablestatlieux, i + 1);
                StatistiqueMTNLieux11 listid = new StatistiqueMTNLieux11(tabstatLieux[1], Integer.parseInt(tabstatLieux[0]));
                StatsMTNlieux.add(listid);
                i++;
            } catch (BadElementException | IOException e) {
            }

        }
        document.add(tablestatlieux);

        StatsMTNlieux.sort(Comparator.comparingInt(StatistiqueMTNLieux11::getOccurence));
        System.out.println("Write CSV file: Stat");
        CsvFileWriterMTN.writeCsvFileStatsMTNLieux(num, StatsMTNlieux, this.dateRequisition);

        br.close();
    }

    public void lireficMTN_Multiple(String num, Session session, Document[] listDoc, PrintMTNMultiple pm, String identification, int nbNum, String[] ListeNum) throws JSchException, SftpException, IOException, BadElementException, DocumentException {

        int k = 0;

        while (k < nbNum) {
            System.out.println(k);
            num = ListeNum[k];
            System.out.println(" le numero de sortie est : " + num);
            System.out.println("Creating SFTP Channel. MTN");
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
            String listing = basePath + this.dateRequisition + "/" + num + "/listing_final";
            String identiteAbonne = basePath + this.dateRequisition + "/" + num + "/IdentificationAbonneesfinal.txt";
            String frequenceCellule = basePath + this.dateRequisition + "/" + num + "/frequenceCellule.txt";
            String frequenceCorrespondance = basePath + this.dateRequisition + "/" + num + "/frequenceCorrespondance.txt";
            String frequenceDureeAppel = basePath + this.dateRequisition + "/" + num + "/frequenceDureeAppel.txt";
            String frequenceImei = basePath + this.dateRequisition + "/" + num + "/frequenceImei.txt";

            out = sftpChannel.get(identite_numero);
            BufferedReader br = new BufferedReader(new InputStreamReader(out));
            String line;
            String[] inputNumber = null;
            System.out.println("1");
            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                inputNumber = line.split(",");
            }

            if ("true".equals(identification)) {
                listDoc[k] = pm.PrintIDAbonneMTN(num, listDoc[k], inputNumber);
//            document = pm.PrintIDCellulleMTN(num, document, inputNumber);
            }

            //create csv
            CsvFileWriterMTNMultiple.writeCsvFileMTNIDNumero(num, inputNumber, this.dateRequisition);

            out = sftpChannel.get(listing);
            br = new BufferedReader(new InputStreamReader(out));
            String[] inputcall = null;

// verifier si le fichier exite 
//        if (out.length() == 0) {
//            System.out.println("File is empty after creating it...");
//        } else {
//            System.out.println("File is not empty after creation...");
//        }
            //prepare
            //Create a new list of student objects
            List listingsMTN = new ArrayList();

            if ("true".equals(identification)) {
                TitrePartie(listDoc[k], "II. Listing  ");
            } else {
                TitrePartie(listDoc[k], "I. Listing  ");
            }

            PdfPTable table1 = new PdfPTable(6);
            int[] widthsListing = {1, 2, 1, 1, 1, 1};
            table1.setWidths(widthsListing);
            table1.setWidthPercentage(100);
            PdfPCell c2 = new PdfPCell(new Phrase("Numero appellant", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("Localisation numero appelant", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("IMEI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date debut appel", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("Duree de l'appel", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("Numero appelé", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            int i = 1;
            String duree = null;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                inputcall = line.split(",");
                if (i != 0) {
                    //convertir 
                    if (!"".equals(inputcall[9]) && (!"CALL_DURATION".equals(inputcall[9]))) {
                        inputcall[9] = ConvertirDuree(inputcall[9]);
//                    inputcall[3] = duree;
                    } else if ("".equals(inputcall[9])) {
                        inputcall[9] = "SMS";
                    }
                    if (!"".equals(inputcall[8]) && (!"START_TIME".equals(inputcall[8]))) {
                        inputcall[8] = ConvertDate(inputcall[8]);
//                    inputcall[3] = duree;
                    }
                    table1 = pm.PrintAddListingMTN(listDoc[k], inputcall, table1);

//                    String NumeroAppelant,String LocalisationNumeroAppelant,String IMEINumeroAppelant,String DateDebutAppel,String DureeAppel,String NumeroAppele
                    String localisation = inputcall[2] + " " + inputcall[3] + "(Cell: " + inputcall[1] + " Long: " + inputcall[4] + " Lat: " + inputcall[5] + " Azimut: " + inputcall[6] + ")";
                    if (localisation.contains("null")) {
                        localisation = "--";
                    }
                    listing listingmtn = new listing(inputcall[0], localisation, inputcall[7], inputcall[8], inputcall[9], inputcall[10]);
                    listingsMTN.add(listingmtn);
                }
                i++;
            }

            listDoc[k].add(table1);

            //ecrire csv file
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date date = new Date();

            System.out.println("date " + date);
            CsvFileWriterMTNMultiple.writeCsvFileMTNListing(num, listingsMTN, this.dateRequisition);

//print Abonné
            List abonnesMTN = new ArrayList();

            out = sftpChannel.get(identiteAbonne);
            br = new BufferedReader(new InputStreamReader(out));
            String[] IdAbonne = null;

            //prepae  "III. Identification des numeros  ");
            PdfPTable tableAbonne = new PdfPTable(7);
            tableAbonne.setWidthPercentage(100);
            c2 = new PdfPCell(new Phrase("Numéro", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Nom & Prenom", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date de naissance", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Numero CNI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date expiration CNI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Adresse", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Nationalite", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);

            while ((line = br.readLine()) != null) {
//                System.out.println(line);

                try {
                    IdAbonne = line.split(",");
                    if (IdAbonne.length >= 6) {
                        tableAbonne = pm.PrintAddIdAbonneMTN(listDoc[k], IdAbonne, tableAbonne);
                        identificationMTN listid = new identificationMTN(IdAbonne[0], IdAbonne[1], IdAbonne[2], IdAbonne[3], IdAbonne[4], IdAbonne[5], "");
                        abonnesMTN.add(listid);
                    }
                } catch (Exception e) {
                }

            }
            if ("true".equals(identification)) {
                TitrePartie(listDoc[k], "III. Identification des numeros  ");

                listDoc[k].add(tableAbonne);
            }
            System.out.println("Write CSV file: Abonne");
            CsvFileWriterMTNMultiple.writeCsvFileAbonneMTN(num, abonnesMTN, this.dateRequisition);

            TitrePartie(listDoc[k], "IV. Fréquence par cellule");

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
                    for (int n = 6; n <= 17; n++) {
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
                        location = tabstat[1] + " " + tabstat[2] + " Long: " + tabstat[3] + " Lat: " + tabstat[4] + " Azimut: " + tabstat[5];
                    }
                    tableFreqCell = pm.PrintAddFreqCelluleMTN(listDoc[k], tabstat, tableFreqCell, i + 1);
                    FreqCell freq = new FreqCell(Integer.parseInt(tabstat[0]), location, Integer.parseInt(tabstat[6]), Integer.parseInt(tabstat[7]), Integer.parseInt(tabstat[8]), Integer.parseInt(tabstat[9]), Integer.parseInt(tabstat[10]), Integer.parseInt(tabstat[11]), Integer.parseInt(tabstat[12]), Integer.parseInt(tabstat[13]), Integer.parseInt(tabstat[14]), Integer.parseInt(tabstat[15]), Integer.parseInt(tabstat[16]), Integer.parseInt(tabstat[17]));
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
            CsvFileWriterMTNMultiple.writeCsvFileFrequenceCelluleMTN(num, StatsMTN, this.dateRequisition);

            TitrePartie(listDoc[k], "V. Fréquence par correspondant");
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
                    tableFreqCorresp = pm.PrintAddFreqCorrespondantMTN(listDoc[k], tab, tableFreqCorresp, i + 1);
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
            CsvFileWriterMTNMultiple.writeCsvFileFreqCorrespondantMTN(num, freqCorrespList, this.dateRequisition);

            TitrePartie(listDoc[k], "VI. Fréquence par durée d'appel");
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
                    tableFreqDureeAppel = pm.PrintAddFreqDureeAppelMTN(listDoc[k], tab, tableFreqDureeAppel, i + 1);
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
            CsvFileWriterMTNMultiple.writeCsvFileFreqDureeAppelMTN(num, freqDureeAppelList, this.dateRequisition);

            TitrePartie(listDoc[k], "VII. Fréquence par IMEI");
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
                    tableFreqImei = pm.PrintAddFreqImeiMTN(listDoc[k], tab, tableFreqImei, i + 1);
                    Date dateDebut = null;
                    Date dateFin = null;
                    try {
                        dateDebut = df.parse(tab[2]);
                        dateFin = df.parse(tab[3]);
                    } catch (ParseException ex) {
                        Logger.getLogger(RemoteRequiMTN.class.getName()).log(Level.SEVERE, null, ex);
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
            CsvFileWriterMTNMultiple.writeCsvFileFreqImeiMTN(num, freqImeiList, this.dateRequisition);

            br.close();
            k++;
        }

    }

    public void lireficMTN_Imei_Multiple(String num, Session session, Document[] listDoc, PrintMTNMultiple pm, String identification, int nbNum, String[] ListeNum) throws JSchException, SftpException, IOException, BadElementException, DocumentException {

        int k = 0;

        while (k < nbNum) {
            System.out.println(k);
            num = ListeNum[k];
            System.out.println(" le numero de sortie est : " + num);
            System.out.println("Creating SFTP Channel. MTN");
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
            String listing = basePath + this.dateRequisition + "/" + num + "/listing_final";
            String identiteAbonne = basePath + this.dateRequisition + "/" + num + "/IdentificationAbonneesfinal.txt";
            String frequenceCellule = basePath + this.dateRequisition + "/" + num + "/frequenceCellule.txt";
            String frequenceCorrespondance = basePath + this.dateRequisition + "/" + num + "/frequenceCorrespondant.txt";
//            String frequenceDureeAppel = basePath + this.dateRequisition + "/" + num + "/frequenceDureeAppel.txt";
//            String frequenceImei = basePath + this.dateRequisition + "/" + num + "/frequenceImei.txt";

          
            out = sftpChannel.get(imeiPartage);
            BufferedReader br = new BufferedReader(new InputStreamReader(out));
            String line;
            String[] inputNumber = null;

            List imeiPartageMTN = new ArrayList();
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
                    tableImeiPartage = pm.PrintAddImeiPartageMTN(listDoc[k], inputNumber, tableImeiPartage);

                    String identite = inputNumber[2] + "(CNI: " + inputNumber[4] + " Date nais: " + inputNumber[3] + " Adresse: " + inputNumber[6] + ")";
                    if (identite.contains("null")) {
                        identite = "inconnu";
                    }
                    SharedImei sharedImei = new SharedImei(inputNumber[0], inputNumber[1], identite, inputNumber[7], inputNumber[8], inputNumber[9]);
                    imeiPartageMTN.add(sharedImei);
                }
                ii++;
            }

            listDoc[k].add(tableImeiPartage);

            //create csv
            CsvFileWriterMTNMultiple.writeCsvFileSharedImeiMTN(num, imeiPartageMTN, this.dateRequisition);

            out = sftpChannel.get(listing);
            br = new BufferedReader(new InputStreamReader(out));
            String[] inputcall = null;

            List listingsMTN = new ArrayList();

            if ("true".equals(identification)) {
                TitrePartie(listDoc[k], "II. Listing  ");
            } else {
                TitrePartie(listDoc[k], "I. Listing  ");
            }

            PdfPTable table1 = new PdfPTable(6);
            int[] widthsListing = {1, 2, 1, 1, 1, 1};
            table1.setWidths(widthsListing);
            table1.setWidthPercentage(100);
            c2 = new PdfPCell(new Phrase("Numero appellant", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("Localisation numero appelant", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("IMEI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date debut appel", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("Duree de l'appel", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            c2 = new PdfPCell(new Phrase("Numero appelé", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(c2);
            int i = 1;
            String duree = null;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                inputcall = line.split(",");
                if (i != 0) {
                    //convertir 
                    if (!"".equals(inputcall[9]) && (!"CALL_DURATION".equals(inputcall[9]))) {
                        inputcall[9] = ConvertirDuree(inputcall[9]);
//                    inputcall[3] = duree;
                    } else if ("".equals(inputcall[9])) {
                        inputcall[9] = "SMS";
                    }
                    if (!"".equals(inputcall[8]) && (!"START_TIME".equals(inputcall[8]))) {
                        inputcall[8] = ConvertDate(inputcall[8]);
//                    inputcall[3] = duree;
                    }
                    table1 = pm.PrintAddListingMTN(listDoc[k], inputcall, table1);

//                    String NumeroAppelant,String LocalisationNumeroAppelant,String IMEINumeroAppelant,String DateDebutAppel,String DureeAppel,String NumeroAppele
                    String localisation = inputcall[2] + " " + inputcall[3] + "(Cell: " + inputcall[1] + " Long: " + inputcall[4] + " Lat: " + inputcall[5] + " Azimut: " + inputcall[6] + ")";
                    if (localisation.contains("null")) {
                        localisation = "--";
                    }
                    listing listingmtn = new listing(inputcall[0], localisation, inputcall[7], inputcall[8], inputcall[9], inputcall[10]);
                    listingsMTN.add(listingmtn);
                }
                i++;
            }

            listDoc[k].add(table1);

            //ecrire csv file
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date date = new Date();

            System.out.println("date " + date);
            CsvFileWriterMTNMultiple.writeCsvFileMTNListing(num, listingsMTN, this.dateRequisition);

//print Abonné
            List abonnesMTN = new ArrayList();

            out = sftpChannel.get(identiteAbonne);
            br = new BufferedReader(new InputStreamReader(out));
            String[] IdAbonne = null;

            //prepae  "III. Identification des numeros  ");
            PdfPTable tableAbonne = new PdfPTable(7);
            tableAbonne.setWidthPercentage(100);
            c2 = new PdfPCell(new Phrase("Numéro", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Nom & Prenom", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date de naissance", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Numero CNI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date expiration CNI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Adresse", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Nationalite", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);

            while ((line = br.readLine()) != null) {
//                System.out.println(line);

                try {
                    IdAbonne = line.split(",");
                    if (IdAbonne.length >= 6) {
                        tableAbonne = pm.PrintAddIdAbonneMTN(listDoc[k], IdAbonne, tableAbonne);
                        identificationMTN listid = new identificationMTN(IdAbonne[0], IdAbonne[1], IdAbonne[2], IdAbonne[3], IdAbonne[4], IdAbonne[5], "");
                        abonnesMTN.add(listid);
                    }
                } catch (Exception e) {
                }

            }
            if ("true".equals(identification)) {
                TitrePartie(listDoc[k], "III. Identification des numeros  ");

                listDoc[k].add(tableAbonne);
            }
            System.out.println("Write CSV file: Abonne");
            CsvFileWriterMTNMultiple.writeCsvFileAbonneMTN(num, abonnesMTN, this.dateRequisition);

            TitrePartie(listDoc[k], "IV. Fréquence par cellule");

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
                    for (int n = 6; n <= 17; n++) {
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
                        location = tabstat[1] + " " + tabstat[2] + " Long: " + tabstat[3] + " Lat: " + tabstat[4] + " Azimut: " + tabstat[5];
                    }
                    tableFreqCell = pm.PrintAddFreqCelluleMTN(listDoc[k], tabstat, tableFreqCell, i + 1);
                    FreqCell freq = new FreqCell(Integer.parseInt(tabstat[0]), location, Integer.parseInt(tabstat[6]), Integer.parseInt(tabstat[7]), Integer.parseInt(tabstat[8]), Integer.parseInt(tabstat[9]), Integer.parseInt(tabstat[10]), Integer.parseInt(tabstat[11]), Integer.parseInt(tabstat[12]), Integer.parseInt(tabstat[13]), Integer.parseInt(tabstat[14]), Integer.parseInt(tabstat[15]), Integer.parseInt(tabstat[16]), Integer.parseInt(tabstat[17]));
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
            CsvFileWriterMTNMultiple.writeCsvFileFrequenceCelluleMTN(num, StatsMTN, this.dateRequisition);

            TitrePartie(listDoc[k], "V. Fréquence par correspondant");
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
                    tableFreqCorresp = pm.PrintAddFreqCorrespondantMTN(listDoc[k], tab, tableFreqCorresp, i + 1);
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
            CsvFileWriterMTNMultiple.writeCsvFileFreqCorrespondantMTN(num, freqCorrespList, this.dateRequisition);

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

    public String ConvertDate(String value) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat formatterPDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

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
