/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.abou.csv;

/**
 *
 * @author aboubecker
 */
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.StatistiqueAppels;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.StatistiqueLieux;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.listing;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.identificationNEXTTEL;
import cm.cirt.calldatarecordmanagement.metier.Variables111;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;

/**
 * @ABOUBECKER 88
 *
 */
public class CsvFileWriterNEXTTEL {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String FILE_HEADERIdnumero = "Telephone,Noms&Prenoms,OperateurTelephonique ,IMEI,Adresse";
    private static final String FILE_HEADERListing = "NumeroAppelant,LocalisationNumeroAppelant,IMEINumeroAppelant,DateDebutAppel,DureeAppel,NumeroAppele";
    private static final String FILE_HEADERAbonne = "Numero,NomPrenom,DateNaissance,NumeroCNI,DateExpCNI";
    private static final String FILE_HEADERStatNumero = "N°,Numero de telephone,Occurence,Duree totale de communications";
    private static final String FILE_HEADERStatLocalisation = "N°,Localisation ,Occurence";

    public static void writeCsvFileNEXTTELIDNumero(String num, String[] data, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "Requisition_Identification_Numero" + num + ".csv";
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
            fileWriter.append(data[3] + " " + data[4]);
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("NEXTTEL");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(data[2]);
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(data[6]);
            fileWriter.append(NEW_LINE_SEPARATOR);

            System.out.println("CSV Identifiant Numero file was created successfully !!!");

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

    public static void writeCsvFileStatsNexttel(String num, List stats, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "Statistiques" + num + ".csv";

        System.out.println("Write CSV file stats:");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERStatNumero.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            System.out.println("dedans stats appels");
            int i = 0;
            for (Iterator it = stats.iterator(); it.hasNext();) {
                StatistiqueAppels stat = (StatistiqueAppels) it.next();
                fileWriter.append(String.valueOf(i));
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
            System.out.println("Error in CsvFileWriterNEXTTEL !!!");
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
            }

        }
    }

    public static void writeCsvFileStatsNexttelLieux(String num, List stats, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "Statistiques_Localisation" + num + ".csv";

        System.out.println("Write CSV file stats lieux:");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERStatLocalisation.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            System.out.println("dedans stats lieux");
            int i = 0;
            for (Iterator it = stats.iterator(); it.hasNext();) {
                StatistiqueLieux stat = (StatistiqueLieux) it.next();
                fileWriter.append(String.valueOf(i));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(stat.getLocalisation());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(stat.getOccurence()));
                fileWriter.append(NEW_LINE_SEPARATOR);
                i++;
            }
            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriterNEXTTEL !!!");
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
            }

        }
    }

    public static void writeCsvFileNEXTTELListing(String num, List listingsmtn, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "Requisition_Listing" + num + ".csv";
        System.out.println("Write CSV file:");
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERListing.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Iterator it = listingsmtn.iterator(); it.hasNext();) {
                listing listingnex = (listing) it.next();
                fileWriter.append(listingnex.getNumeroAppelant());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listingnex.getLocalisationNumeroAppelant());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listingnex.getIMEINumeroAppelant());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listingnex.getDateDebutAppel());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listingnex.getDureeAppel());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listingnex.getNumeroAppele());
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

    public static void writeCsvFileNEXTTELAbonne(String num, List abonnesnex, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "IdentificationAbonnees_" + num + ".csv";

        System.out.println("Write CSV file: dedans");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERAbonne);

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Iterator it = abonnesnex.iterator(); it.hasNext();) {
                identificationNEXTTEL idnex = (identificationNEXTTEL) it.next();
                fileWriter.append(idnex.getNumero());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(idnex.getNomPrenom());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(idnex.getDateNaissance());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(idnex.getNumeroCNI());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(idnex.getDateExpCNI());
                fileWriter.append(NEW_LINE_SEPARATOR);
//                System.out.println(idnex.getNumero() + " - " + idnex.getNomPrenom() + " - " + idnex.getDateNaissance() + " - " + idnex.getNumeroCNI() + " - " + idnex.getDateExpCNI());

            }

            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!");
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
            }

        }
    }
}
