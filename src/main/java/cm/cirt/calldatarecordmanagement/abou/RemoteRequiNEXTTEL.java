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
import cm.cirt.calldatarecordmanagement.abou.csv.CsvFileWriterNEXTTEL;
import cm.cirt.calldatarecordmanagement.abou.csv.CsvFileWriterNEXTTELMultiple;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.FreqCell;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.FreqCorrespondant;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.FreqDureeAppel;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.FreqImei;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.SharedImei;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.StatistiqueLieux;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.StatistiqueAppels;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.identificationNEXTTEL;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.listing;
import static cm.cirt.calldatarecordmanagement.abou.print.PrintMTN.TitrePartie;
import static cm.cirt.calldatarecordmanagement.abou.print.PrintMTN.smallBold;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cm.cirt.calldatarecordmanagement.abou.print.PrintNEXTTEL;
import cm.cirt.calldatarecordmanagement.abou.print.PrintNEXTTELMultiple;
import cm.cirt.calldatarecordmanagement.metier.Variables111;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author @ABOUBECKER 88
 */
public class RemoteRequiNEXTTEL {

    private String dateRequisition;

    public RemoteRequiNEXTTEL(String dateRequisition) {
        this.dateRequisition = dateRequisition;
    }

    public RemoteRequiNEXTTEL() {
    }

    public String getDateRequisition() {
        return dateRequisition;
    }

    public void setDateRequisition(String dateRequisition) {
        this.dateRequisition = dateRequisition;
    }

    public void remoteFind_NEXTTEL(String[] args, Document document, PrintNEXTTEL pn, String identification) throws SftpException, DocumentException {

        JSch jsch = new JSch();

//        All0peratorRequisition Allreq = new All0peratorRequisition();
//        Allreq.setVisible(true);
        String command = "sh requisitionNEXTTEL.sh " + args[0] + " " + args[1] + " " + args[2];
//        String command = "pwd";
        System.out.println("command :" + command);
        Session session;
        try {
            //Open a Session to remote SSH server and Connect.
            //Set User and IP of the remote host and SSH port.
            session = jsch.getSession(Variables111.NEXTTEL_USER, Variables111.NEXTTEL_HOST, 22);
            //When we do SSH to a remote host for the 1st time or if key at the remote host 
            //Changes, we will be prompted to confirm the authenticity of remote host. 
            //This check feature is controlled by StrictHostKeyChecking ssh parameter. 
            //By default StrictHostKeyChecking  is set to yes as a security measure.
            session.setConfig("StrictHostKeyChecking", "no");
            //Set Password
String privateKeyPath = Variables.getIdRsaPath(); // Replace with your actual path
jsch.addIdentity(privateKeyPath);
            System.out.println("1");
            //session.setPassword(Variables111.NEXTTEL_PASSWORD);

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
            this.lireficNEXTTEL(args[0], session, document, pn, identification);

            //Disconnect the Session
            session.disconnect();

        } catch (JSchException | IOException e) {
        }

    }

    public void remoteFind_NEXTTEL_Multiple(String[] args, Document[] listDoc, PrintNEXTTELMultiple pn, String identification, int nbNum, String[] ListeNum) throws SftpException, DocumentException {

        JSch jsch = new JSch();

//        All0peratorRequisition Allreq = new All0peratorRequisition();
//        Allreq.setVisible(true);
//        String command = "sh requisitionNEXTTEL.sh " + args[0] + " " + args[1] + " " + args[2];
        String command;
        if (Variables111.ACCOUNT_TYPE.equals("LOCAL")) {
            command = "/home/traffic_data/nexttel/operations/cdr/requisitionNexttel_Multiple_Optimized.sh " + args[0] + " " + args[1] + " " + args[2] + " " + dateRequisition;
        } else {
            command = "/home/data/nexttel/operations/cdr/requisitionNexttel_Multiple_Optimized.sh " + args[0] + " " + args[1] + " " + args[2] + " " + dateRequisition;
        }

//        String command = "pwd";
        System.out.println("command :" + command);
        Session session;
        try {
            //Open a Session to remote SSH server and Connect.
            //Set User and IP of the remote host and SSH port.
            session = jsch.getSession(Variables111.NEXTTEL_USER, Variables111.NEXTTEL_HOST, 22);
            //When we do SSH to a remote host for the 1st time or if key at the remote host 
            //Changes, we will be prompted to confirm the authenticity of remote host. 
            //This check feature is controlled by StrictHostKeyChecking ssh parameter. 
            //By default StrictHostKeyChecking  is set to yes as a security measure.
            session.setConfig("StrictHostKeyChecking", "no");
            //Set Password
String privateKeyPath = Variables.getIdRsaPath(); // Replace with your actual path
jsch.addIdentity(privateKeyPath);
            System.out.println("1");
            //session.setPassword(Variables111.NEXTTEL_PASSWORD);

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
            this.lireficNEXTTEL_Multiple(args[0], session, listDoc, pn, identification, nbNum, ListeNum);

            //Disconnect the Session
            session.disconnect();

        } catch (JSchException | IOException e) {
        }

    }

    public void remoteFind_NEXTTEL_Imei_Multiple(String[] args, Document[] listDoc, PrintNEXTTELMultiple pn, String identification, int nbNum, String[] ListeNum) throws SftpException, DocumentException {

        JSch jsch = new JSch();

//        All0peratorRequisition Allreq = new All0peratorRequisition();
//        Allreq.setVisible(true);
//        String command = "sh requisitionNEXTTEL.sh " + args[0] + " " + args[1] + " " + args[2];
        String command;
        if (Variables111.ACCOUNT_TYPE.equals("LOCAL")) {
            command = "/home/traffic_data/nexttel/operations/cdr/requisitionImeiNexttel_Multiple_Optimized.sh " + args[0] + " " + args[1] + " " + args[2] + " " + dateRequisition;
        } else {
            command = "/home/data/nexttel/operations/cdr/requisitionImeiNexttel_Multiple_Optimized.sh " + args[0] + " " + args[1] + " " + args[2] + " " + dateRequisition;
        }
        System.out.println("command :" + command);
        Session session;
        try {
            //Open a Session to remote SSH server and Connect.
            //Set User and IP of the remote host and SSH port.
            session = jsch.getSession(Variables111.NEXTTEL_USER, Variables111.NEXTTEL_HOST, 22);
            //When we do SSH to a remote host for the 1st time or if key at the remote host 
            //Changes, we will be prompted to confirm the authenticity of remote host. 
            //This check feature is controlled by StrictHostKeyChecking ssh parameter. 
            //By default StrictHostKeyChecking  is set to yes as a security measure.
            session.setConfig("StrictHostKeyChecking", "no");
            //Set Password
String privateKeyPath = Variables.getIdRsaPath(); // Replace with your actual path
jsch.addIdentity(privateKeyPath);
            System.out.println("1");
            //session.setPassword(Variables111.NEXTTEL_PASSWORD);

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
            this.lireficNEXTTEL_Imei_Multiple(args[0], session, listDoc, pn, identification, nbNum, ListeNum);

            //Disconnect the Session
            session.disconnect();

        } catch (JSchException | IOException e) {
        }

    }

    public void lireficNEXTTEL(String num, Session session, Document document, PrintNEXTTEL pn, String identification) throws JSchException, SftpException, IOException, BadElementException, DocumentException {

        System.out.println("Creating SFTP Channel NEXTTEL");
        ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
        sftpChannel.connect();
        System.out.println("SFTP Channel created.");
        InputStream out = null;
        String identite_numero = "/root/" + num + "/identite_numero.txt";
        String listing = "/root/" + num + "/listing_final";
        String identiteAbonne = "/root/" + num + "/IdentificationAbonneesfinal.txt";
        String stat = "/root/" + num + "/NBnumero.txt";
        String statlieux = "/root/" + num + "/NBSites.txt";
        out = sftpChannel.get(identite_numero);

        BufferedReader br = new BufferedReader(new InputStreamReader(out));
        String line;
        String[] inputNumber = null;

        while ((line = br.readLine()) != null) {
//            System.out.println(line);
            inputNumber = line.split(",", -1);
        }

        if ("true".equals(identification)) {
            document = pn.PrintIDAbonneNEXTTEL(num, document, inputNumber);
        }

//        if(inputNumber.)
        //create csv
        CsvFileWriterNEXTTEL.writeCsvFileNEXTTELIDNumero(num, inputNumber, this.dateRequisition);

        out = sftpChannel.get(listing);
        br = new BufferedReader(new InputStreamReader(out));
        String[] inputcall = null;

        //prepare
        //Create a new list of student objects
        List listingsNEXTTEL = new ArrayList();

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
            inputcall = line.split(",", -1);
            if (i != 0) {

                //convertir CDR_TYPE,CALL_TIME,DURATION,CALLING_NUMBER,CALLING_IMSI,CALLING_IMEI,CALLING_LOCATION,CALLED_NUMBER,CALLED_DIRECTION
                if (!"".equals(inputcall[2]) && !"DURATION".equals(inputcall[2])) {
                    inputcall[2] = ConvertirDuree(inputcall[2]);
                }
                if (!"".equals(inputcall[1]) && !"CALL_TIME".equals(inputcall[1])) {
                    inputcall[1] = ConvertDate(inputcall[1]);
                }
                if ("2".equals(inputcall[0])) {
                    inputcall[2] = "SMS";
                }
                table1 = pn.PrintAddListingNEXTTEL(document, inputcall, table1);
//                1,2018/07/15 12:46:54,48,663763562,624042729631832,354841080980630,Benoue-Garoua I,663770569,IN
//                "NumeroAppelant,LocalisationNumeroAppelant,IMEINumeroAppelant,DateDebutAppel,DureeAppel,NumeroAppele";
                listing listingnex = new listing(inputcall[3], inputcall[6], inputcall[5], inputcall[1], inputcall[2], inputcall[7]);
                listingsNEXTTEL.add(listingnex);
            }
            i++;
        }
        document.add(table1);

        //ecrire csv file
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();

        System.out.println("date " + date);
        CsvFileWriterNEXTTEL.writeCsvFileNEXTTELListing(num, listingsNEXTTEL, this.dateRequisition);

        //print Abonné
        List abonnesNEXTTEL = new ArrayList();

        out = sftpChannel.get(identiteAbonne);
        br = new BufferedReader(new InputStreamReader(out));
        String[] IdAbonne = null;

        //prepare  "III. Identification des numeros");
        PdfPTable tableAbonne = new PdfPTable(5);
        tableAbonne.setWidthPercentage(100);
        c2 = new PdfPCell(new Phrase("Numero", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);
        c2 = new PdfPCell(new Phrase("Nom & Prenoms", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);
        c2 = new PdfPCell(new Phrase("Date de Naissance", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);
        c2 = new PdfPCell(new Phrase("Numero CNI", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);
        c2 = new PdfPCell(new Phrase("Date Expiration CNI", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableAbonne.addCell(c2);

        while ((line = br.readLine()) != null) {
//            System.out.println(line);
            //685423334,MODY,ADEYEMI,18/01/52,165886094,27/02/23
            try {
                IdAbonne = line.split(",", -1);

                tableAbonne = pn.PrintAddIdAbonneNEXTTEL(document, IdAbonne, tableAbonne);
                identificationNEXTTEL listid = new identificationNEXTTEL(IdAbonne[0], IdAbonne[1] + " " + IdAbonne[2], IdAbonne[3], IdAbonne[4], IdAbonne[5]);
                abonnesNEXTTEL.add(listid);
            } catch (BadElementException | IOException e) {
            }

        }
        if ("true".equals(identification)) {
            TitrePartie(document, "III. Identification des numeros  ");
            System.out.println("III. Identification des numeros  ");
            document.add(tableAbonne);
        }
        System.out.println("Write CSV file: Abonne");
//        if (abonnesNEXTTEL.isEmpty())
//        {
        CsvFileWriterNEXTTEL.writeCsvFileNEXTTELAbonne(num, abonnesNEXTTEL, this.dateRequisition);
//        }else        {

//        }
//
        TitrePartie(document, "IV. Statistiques");

        TitrePartie(document, "IV.1 Statistique sur la frequence des numeros");
        //print statistiques
        List StatsNEXTTEL = new ArrayList();
//
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
        c2 = new PdfPCell(new Phrase("Occurence", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablestat.addCell(c2);
        c2 = new PdfPCell(new Phrase("Duree totale de communications ", smallBold));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablestat.addCell(c2);
        i = 0;

        while ((line = br.readLine()) != null) {
//            System.out.println(line);

            try {
                tabstat = line.split(",", -1);
                if (!"".equals(tabstat[1])) {
                    tabstat[1] = ConvertirDuree(tabstat[1]);
//                    inputcall[3] = duree;
                }

                tablestat = pn.PrintAddStatsNEXTTEL(document, tabstat, tablestat, i);
                StatistiqueAppels listid = new StatistiqueAppels(tabstat[2], Integer.valueOf(tabstat[0]), tabstat[1]);
                StatsNEXTTEL.add(listid);
                i++;
            } catch (BadElementException | IOException e) {
            }

        }
        document.add(tablestat);
//
        System.out.println("Write CSV file: Stat 1");
        CsvFileWriterNEXTTEL.writeCsvFileStatsNexttel(num, StatsNEXTTEL, this.dateRequisition);

        TitrePartie(document, "IV.2 Statistique sur la frequence des lieux d'appels");

        //print statistiques
        List StatsNEXTTELlieux = new ArrayList();

        out = sftpChannel.get(statlieux);
        br = new BufferedReader(new InputStreamReader(out));
        String[] tabstatLieux = null;

        //prepae 
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
                tablestatlieux = pn.PrintAddStatsNEXTTELLieux(document, tabstatLieux, tablestatlieux, i);
                StatistiqueLieux listid = new StatistiqueLieux(tabstatLieux[1], Integer.valueOf(tabstatLieux[0]));
                StatsNEXTTELlieux.add(listid);
                i++;
            } catch (BadElementException | IOException e) {
            }

        }
        document.add(tablestatlieux);

        System.out.println("Write CSV file: Stat");
        CsvFileWriterNEXTTEL.writeCsvFileStatsNexttelLieux(num, StatsNEXTTELlieux, this.dateRequisition);

        br.close();
    }

    public void lireficNEXTTEL_Multiple(String num, Session session, Document[] listDoc, PrintNEXTTELMultiple pn, String identification, int nbNum, String[] ListeNum) throws JSchException, SftpException, IOException, BadElementException, DocumentException {

        int k = 0;
        while (k < nbNum) {
            System.out.println(k);
            num = ListeNum[k];
            System.out.println("Creating SFTP Channel NEXTTEL");
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            System.out.println("SFTP Channel created.");
            InputStream out = null;
            String basePath;
            if (Variables111.ACCOUNT_TYPE.equals("YAYAP")) {
                basePath = "/home/data/mtn/operations/cdr/"; // Pour le SED YAYAP
            } else {
                basePath = "/root/"; // Pour l'ARMP
                System.out.println("ARMP");
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

            while ((line = br.readLine()) != null) {
//            System.out.println(line);
                inputNumber = line.split(",", -1);
            }

            if ("true".equals(identification)) {
                listDoc[k] = pn.PrintIDAbonneNEXTTEL(num, listDoc[k], inputNumber);
            }

//        if(inputNumber.)
            //create csv
            CsvFileWriterNEXTTELMultiple.writeCsvFileNEXTTELIDNumero(num, inputNumber, this.dateRequisition);

            out = sftpChannel.get(listing);
            br = new BufferedReader(new InputStreamReader(out));
            String[] inputcall = null;

            //prepare
            //Create a new list of student objects
            List listingsNEXTTEL = new ArrayList();

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
//            System.out.println(line);
                inputcall = line.split(",", -1);
                if (i != 0) {

                    //convertir CDR_TYPE,CALL_TIME,DURATION,CALLING_NUMBER,CALLING_IMSI,CALLING_IMEI,CALLING_LOCATION,CALLED_NUMBER,CALLED_DIRECTION
                    if (!"".equals(inputcall[2]) && !"DURATION".equals(inputcall[2])) {
                        inputcall[2] = ConvertirDuree(inputcall[2]);
                    }
                    if (!"".equals(inputcall[1]) && !"CALL_TIME".equals(inputcall[1])) {
                        inputcall[1] = ConvertDate(inputcall[1]);
                    }
                    if ("2".equals(inputcall[0])) {
                        inputcall[2] = "SMS";
                    }
                    table1 = pn.PrintAddListingNEXTTEL(listDoc[k], inputcall, table1);
//                1,2018/07/15 12:46:54,48,663763562,624042729631832,354841080980630,Benoue-Garoua I,663770569,IN
//                "NumeroAppelant,LocalisationNumeroAppelant,IMEINumeroAppelant,DateDebutAppel,DureeAppel,NumeroAppele";
                    listing listingnex = new listing(inputcall[3], inputcall[6], inputcall[5], inputcall[1], inputcall[2], inputcall[7]);
                    listingsNEXTTEL.add(listingnex);
                }
                i++;
            }
            listDoc[k].add(table1);

            //ecrire csv file
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date date = new Date();

            System.out.println("date " + date);
            CsvFileWriterNEXTTELMultiple.writeCsvFileNEXTTELListing(num, listingsNEXTTEL, this.dateRequisition);

            //print Abonné
            List abonnesNEXTTEL = new ArrayList();

            out = sftpChannel.get(identiteAbonne);
            br = new BufferedReader(new InputStreamReader(out));
            String[] IdAbonne = null;

            //prepare  "III. Identification des numeros");
            PdfPTable tableAbonne = new PdfPTable(5);
            tableAbonne.setWidthPercentage(100);
            c2 = new PdfPCell(new Phrase("Numero", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Nom & Prenoms", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date de Naissance", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Numero CNI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date Expiration CNI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);

            while ((line = br.readLine()) != null) {
//            System.out.println(line);
                //685423334,MODY,ADEYEMI,18/01/52,165886094,27/02/23
                try {
                    IdAbonne = line.split(",", -1);
                    if (IdAbonne.length >= 5) {
                        tableAbonne = pn.PrintAddIdAbonneNEXTTEL(listDoc[k], IdAbonne, tableAbonne);
                        identificationNEXTTEL listid = new identificationNEXTTEL(IdAbonne[0], IdAbonne[1], IdAbonne[2], IdAbonne[3], IdAbonne[4]);
                        abonnesNEXTTEL.add(listid);
                    }
                } catch (BadElementException | IOException e) {
                }

            }
            if ("true".equals(identification)) {
                TitrePartie(listDoc[k], "III. Identification des numeros  ");
                System.out.println("III. Identification des numeros  ");
                listDoc[k].add(tableAbonne);
            }
            System.out.println("Write CSV file: Abonne");
//        if (abonnesNEXTTEL.isEmpty())
//        {
            CsvFileWriterNEXTTELMultiple.writeCsvFileNEXTTELAbonne(num, abonnesNEXTTEL, this.dateRequisition);
//        }else        {

//        }
//
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
                    for (int n = 2; n <= 13; n++) {
                        if (tabstat[n].equals("null")) {
                            tabstat[n] = "-1";
                        }
                    }
                    String location;
                    if (tabstat[1].equals("---")) {
                        location = "Site inconnu";
                        tabstat[1] = "Site inconnu";
                    } else {
                        location = tabstat[1];
                    }
                    tableFreqCell = pn.PrintAddFreqCelluleNEXTTEL(listDoc[k], tabstat, tableFreqCell, i + 1);
                    FreqCell freq = new FreqCell(Integer.parseInt(tabstat[0]), location, Integer.parseInt(tabstat[2]), Integer.parseInt(tabstat[3]), Integer.parseInt(tabstat[4]), Integer.parseInt(tabstat[5]), Integer.parseInt(tabstat[6]), Integer.parseInt(tabstat[7]), Integer.parseInt(tabstat[8]), Integer.parseInt(tabstat[9]), Integer.parseInt(tabstat[10]), Integer.parseInt(tabstat[11]), Integer.parseInt(tabstat[12]), Integer.parseInt(tabstat[13]));
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
            CsvFileWriterNEXTTELMultiple.writeCsvFileFrequenceCelluleNexttel(num, StatsMTN, this.dateRequisition);

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
                    tableFreqCorresp = pn.PrintAddFreqCorrespondantNEXTTEL(listDoc[k], tab, tableFreqCorresp, i + 1);
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
            CsvFileWriterNEXTTELMultiple.writeCsvFileFreqCorrespondantNexttel(num, freqCorrespList, this.dateRequisition);

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
                    tableFreqDureeAppel = pn.PrintAddFreqDureeAppelNEXTTEL(listDoc[k], tab, tableFreqDureeAppel, i + 1);
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
            CsvFileWriterNEXTTELMultiple.writeCsvFileFreqDureeAppelNexttel(num, freqDureeAppelList, this.dateRequisition);

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
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            i = 0;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);

                try {
                    tab = line.split(",");
                    tableFreqImei = pn.PrintAddFreqImeiNEXTTEL(listDoc[k], tab, tableFreqImei, i + 1);
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
            CsvFileWriterNEXTTELMultiple.writeCsvFileFreqImeiNexttel(num, freqImeiList, this.dateRequisition);

            br.close();
            k++;
        }
    }

    public void lireficNEXTTEL_Imei_Multiple(String num, Session session, Document[] listDoc, PrintNEXTTELMultiple pn, String identification, int nbNum, String[] ListeNum) throws JSchException, SftpException, IOException, BadElementException, DocumentException {

        int k = 0;
        while (k < nbNum) {
            System.out.println(k);
            num = ListeNum[k];
            System.out.println("Creating SFTP Channel NEXTTEL");
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

            List imeiPartageNexttel = new ArrayList();
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
                    tableImeiPartage = pn.PrintAddImeiPartageNexttel(listDoc[k], inputNumber, tableImeiPartage);

                    String identite = inputNumber[2] + "(CNI: " + inputNumber[4] + " Date nais: " + inputNumber[3] + " Adresse: " + inputNumber[6] + ")";
                    if (identite.contains("null")) {
                        identite = "inconnu";
                    }
                    SharedImei sharedImei = new SharedImei(inputNumber[0], inputNumber[1], identite, inputNumber[7], inputNumber[8], inputNumber[9]);
                    imeiPartageNexttel.add(sharedImei);
                }
                ii++;
            }

            listDoc[k].add(tableImeiPartage);

            //create csv
            CsvFileWriterNEXTTELMultiple.writeCsvFileSharedImeiNexttel(num, imeiPartageNexttel, this.dateRequisition);

            out = sftpChannel.get(listing);
            br = new BufferedReader(new InputStreamReader(out));
            String[] inputcall = null;

            //prepare
            //Create a new list of student objects
            List listingsNEXTTEL = new ArrayList();

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
//            System.out.println(line);
                inputcall = line.split(",", -1);
                if (i != 0) {

                    //convertir CDR_TYPE,CALL_TIME,DURATION,CALLING_NUMBER,CALLING_IMSI,CALLING_IMEI,CALLING_LOCATION,CALLED_NUMBER,CALLED_DIRECTION
                    if (!"".equals(inputcall[2]) && !"DURATION".equals(inputcall[2])) {
                        inputcall[2] = ConvertirDuree(inputcall[2]);
                    }
                    if (!"".equals(inputcall[1]) && !"CALL_TIME".equals(inputcall[1])) {
                        inputcall[1] = ConvertDate(inputcall[1]);
                    }
                    if ("2".equals(inputcall[0])) {
                        inputcall[2] = "SMS";
                    }
                    table1 = pn.PrintAddListingNEXTTEL(listDoc[k], inputcall, table1);
//                1,2018/07/15 12:46:54,48,663763562,624042729631832,354841080980630,Benoue-Garoua I,663770569,IN
//                "NumeroAppelant,LocalisationNumeroAppelant,IMEINumeroAppelant,DateDebutAppel,DureeAppel,NumeroAppele";
                    listing listingnex = new listing(inputcall[3], inputcall[6], inputcall[5], inputcall[1], inputcall[2], inputcall[7]);
                    listingsNEXTTEL.add(listingnex);
                }
                i++;
            }
            listDoc[k].add(table1);

            //ecrire csv file
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date date = new Date();

            System.out.println("date " + date);
            CsvFileWriterNEXTTELMultiple.writeCsvFileNEXTTELListing(num, listingsNEXTTEL, this.dateRequisition);

            //print Abonné
            List abonnesNEXTTEL = new ArrayList();

            out = sftpChannel.get(identiteAbonne);
            br = new BufferedReader(new InputStreamReader(out));
            String[] IdAbonne = null;

            //prepare  "III. Identification des numeros");
            PdfPTable tableAbonne = new PdfPTable(5);
            tableAbonne.setWidthPercentage(100);
            c2 = new PdfPCell(new Phrase("Numero", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Nom & Prenoms", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date de Naissance", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Numero CNI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);
            c2 = new PdfPCell(new Phrase("Date Expiration CNI", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAbonne.addCell(c2);

            while ((line = br.readLine()) != null) {
//            System.out.println(line);
                //685423334,MODY,ADEYEMI,18/01/52,165886094,27/02/23
                try {
                    IdAbonne = line.split(",", -1);
                    if (IdAbonne.length >= 5) {
                        tableAbonne = pn.PrintAddIdAbonneNEXTTEL(listDoc[k], IdAbonne, tableAbonne);
                        identificationNEXTTEL listid = new identificationNEXTTEL(IdAbonne[0], IdAbonne[1], IdAbonne[2], IdAbonne[3], IdAbonne[4]);
                        abonnesNEXTTEL.add(listid);
                    }
                } catch (BadElementException | IOException e) {
                }

            }
            if ("true".equals(identification)) {
                TitrePartie(listDoc[k], "III. Identification des numeros  ");
                System.out.println("III. Identification des numeros  ");
                listDoc[k].add(tableAbonne);
            }
            System.out.println("Write CSV file: Abonne");
//        if (abonnesNEXTTEL.isEmpty())
//        {
            CsvFileWriterNEXTTELMultiple.writeCsvFileNEXTTELAbonne(num, abonnesNEXTTEL, this.dateRequisition);
//        }else        {

//        }
//
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
                    for (int n = 2; n <= 13; n++) {
                        if (tabstat[n].equals("null")) {
                            tabstat[n] = "-1";
                        }
                    }
                    String location;
                    if (tabstat[1].equals("---")) {
                        location = "Site inconnu";
                        tabstat[1] = "Site inconnu";
                    } else {
                        location = tabstat[1];
                    }
                    tableFreqCell = pn.PrintAddFreqCelluleNEXTTEL(listDoc[k], tabstat, tableFreqCell, i + 1);
                    FreqCell freq = new FreqCell(Integer.parseInt(tabstat[0]), location, Integer.parseInt(tabstat[2]), Integer.parseInt(tabstat[3]), Integer.parseInt(tabstat[4]), Integer.parseInt(tabstat[5]), Integer.parseInt(tabstat[6]), Integer.parseInt(tabstat[7]), Integer.parseInt(tabstat[8]), Integer.parseInt(tabstat[9]), Integer.parseInt(tabstat[10]), Integer.parseInt(tabstat[11]), Integer.parseInt(tabstat[12]), Integer.parseInt(tabstat[13]));
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
            CsvFileWriterNEXTTELMultiple.writeCsvFileFrequenceCelluleNexttel(num, StatsMTN, this.dateRequisition);

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
                    tableFreqCorresp = pn.PrintAddFreqCorrespondantNEXTTEL(listDoc[k], tab, tableFreqCorresp, i + 1);
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
            CsvFileWriterNEXTTELMultiple.writeCsvFileFreqCorrespondantNexttel(num, freqCorrespList, this.dateRequisition);

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

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        SimpleDateFormat formatterPDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String datePDF = null;
        try {

            Date date = formatter.parse(value);

            datePDF = formatterPDF.format(date);
//            System.out.println(date);
//            System.out.println(formatterPDF.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return datePDF;
    }
}
