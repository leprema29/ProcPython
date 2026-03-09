/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.abou.csv;

/**
 *
 * @author ABOUBECKER 88
 */
/**
 *
 * @author ABOUBECKER 88
 */
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.identificationMTN;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.StatistiqueAppels;
import cm.cirt.calldatarecordmanagement.abou.cm.backup.StatistiqueMTNLieux11;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.listing;
import cm.cirt.calldatarecordmanagement.metier.Variables111;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;

/**
 * @ABOUBECKER 88
 *
 */
public class CsvFileWriterMTN {

    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
     */
    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String FILE_HEADERIdnumero = "Telephone,Noms&Prenoms,OperateurTelephonique ,IMEI,Adresse";
    private static final String FILE_HEADERListing = "NumeroAppelant,LocalisationNumeroAppelant,IMEINumeroAppelant,DateDebutAppel,DureeAppel,NumeroAppele";
    private static final String FILE_HEADERAbonne = "Numero,NomPrenom,DateNaissance,NumeroCNI,DateExpCNI,Quartier";
    private static final String FILE_HEADERStatNumero = "N°,Numero de telephone,Occurence,Duree totale de communications";
    private static final String FILE_HEADERStatLocalisation = "N°,Localisation ,Occurence";

    public static void writeCsvFileMTNIDNumero(String num, String[] data, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "Requisition_IdNumero" + num + ".csv";
        System.out.println("Write CSV file id num:");
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERIdnumero.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(num);
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(data[3]);
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("MTN");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(data[1]);
            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append("");
//            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(data[6]);
            fileWriter.append(NEW_LINE_SEPARATOR);

            System.out.println("CSV Id Numero file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
            }

        }
    }

    public static void writeCsvFileMTNListing(String num, List listingsmtn, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "Requisition_Listing" + num + ".csv";
        System.out.println("Write CSV file listing:");
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERListing.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Iterator it = listingsmtn.iterator(); it.hasNext();) {
                listing listingmtn = (listing) it.next();
                fileWriter.append(listingmtn.getNumeroAppelant());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listingmtn.getLocalisationNumeroAppelant());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listingmtn.getIMEINumeroAppelant());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listingmtn.getDateDebutAppel());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listingmtn.getDureeAppel());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listingmtn.getNumeroAppele());
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
            }
        }

    }

    public static void writeCsvFileAbonneMTN(String num, List abonnesmtn, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "IdentificationAbonnees" + num + ".csv";

        System.out.println("Write CSV file Abonnees:");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERAbonne.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
//            System.out.println("dedans 1 ");
            for (Iterator it = abonnesmtn.iterator(); it.hasNext();) {
                identificationMTN idmtn = (identificationMTN) it.next();
                fileWriter.append(idmtn.getNumero());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(idmtn.getNomPrenom());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(idmtn.getDateNaissance());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(idmtn.getNumeroCNI());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(idmtn.getDateExpCNI());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(idmtn.getQuartier());
//                fileWriter.append(COMMA_DELIMITER);
//                fileWriter.append(idmtn.getNationalite());
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
            }

        }
    }

    public static void writeCsvFileStatsMTN(String num, List stats, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "Statistiques" + num + ".csv";

        System.out.println("Write CSV file stats:");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERStatNumero.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            System.out.println("dedans stats ");
            int i = 0;
            for (Iterator it = stats.iterator(); it.hasNext();) {
                StatistiqueAppels stat = (StatistiqueAppels) it.next();
                fileWriter.append(String.valueOf(i + 1));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(stat.getNumeroAppelant());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(stat.getOccurence()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(stat.getDureeAppel());
                fileWriter.append(NEW_LINE_SEPARATOR);
                i++;
            }
            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
            }

        }
    }

    public static void writeCsvFileStatsMTNLieux(String num, List stats, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "Statistiques_Localisation" + num + ".csv";

        System.out.println("Write CSV file stats lieux:");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERStatLocalisation.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            System.out.println("dedans stats ");
            int i = 0;
            for (Iterator it = stats.iterator(); it.hasNext();) {
                StatistiqueMTNLieux11 stat = (StatistiqueMTNLieux11) it.next();
                fileWriter.append(String.valueOf(i + 1));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(stat.getLocalisation());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(stat.getOccurence()));
                fileWriter.append(NEW_LINE_SEPARATOR);
                i++;
            }
            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
            }

        }
    }

    public static void writeCsvFileMTN(String fileName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
