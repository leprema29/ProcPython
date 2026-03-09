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
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.listingOrangeSMS;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.listing;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.StatistiqueLieux;
import cm.cirt.calldatarecordmanagement.abou.cm.backup.StatistiqueORANGEfrequence1111;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.identificationORANGE;
import cm.cirt.calldatarecordmanagement.metier.Variables111;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;

/**
 * @ABOUBECKER 88
 *
 */
public class CsvFileWriterORANGE {
//Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String FILE_HEADERIdnumero = "Telephone,Noms&Prenoms,OperateurTelephonique ,IMEI,Adresse";
    private static final String FILE_HEADERListing = "NumeroAppelant,LocalisationNumeroAppelant,IMEINumeroAppelant,DateDebutAppel,DureeAppel,NumeroAppele";
    private static final String FILE_HEADERAbonne = "Numero,NomPrenom,DateNaissance,NumeroCNI,DateExpCNI,Quartier";
    private static final String FILE_HEADERStatNumero = "N°,Numero de telephone,Occurence,Duree totale de communications";
    private static final String FILE_HEADERStatLocalisation = "N°,Localisation ,Occurence";
    private static final String FILE_HEADEROrangeSMS = "NumeroEnvoi,LocalisationNumeroDest,IMEINumeroDest,DateSMS,NumeroDest";


    public static void writeCsvFileORANGEIDNumero(String num, String[] data, String dateRequisition) {

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
            fileWriter.append(data[3]);
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("ORANGE");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(data[2]);
//            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append("");
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

    public static void writeCsvFileOrangeListingEmis(String num, List listingsOrEmis, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "Requisition_Listing_Emis" + num + ".csv";
        System.out.println("Write CSV file listing:");
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERListing.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Iterator it = listingsOrEmis.iterator(); it.hasNext();) {
                listing listingor = (listing) it.next();
                fileWriter.append(listingor.getNumeroAppelant());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listingor.getLocalisationNumeroAppelant());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listingor.getIMEINumeroAppelant());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listingor.getDateDebutAppel());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listingor.getDureeAppel());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(listingor.getNumeroAppele());
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


    public static void writeCsvFileOrangeListingSMS(String num, List listingsms, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "Requisition_Listing_SMS" + num + ".csv";
        System.out.println("Write CSV file listing SMS:");
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADEROrangeSMS.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Iterator it = listingsms.iterator(); it.hasNext();) {
                listingOrangeSMS sms = (listingOrangeSMS) it.next();
                fileWriter.append(sms.getNumeroEnvoi());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(sms.getLocalisationNumeroDest());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(sms.getIMEINumeroDest());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(sms.getDateSMS());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(sms.getNumeroDest());
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

    public static void writeCsvFileAbonneOrange(String num, List abonnesOr, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "IdentificationAbonnees_" + num + ".csv";

        System.out.println("Write CSV file Abonnees orange:");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERAbonne.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
//            System.out.println("dedans 1 ");
            for (Iterator it = abonnesOr.iterator(); it.hasNext();) {
                identificationORANGE idor = (identificationORANGE) it.next();
                fileWriter.append(idor.getNumero());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(idor.getNomPrenom());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(idor.getDateNaissance());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(idor.getNumeroCNI());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(idor.getDateExpCNI());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(idor.getQuartier());
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

    
    
      public static void writeCsvFileStatsFreORANGE(String num, List stats, String dateRequisition) {

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
                StatistiqueORANGEfrequence1111 stat = (StatistiqueORANGEfrequence1111) it.next();
                fileWriter.append(String.valueOf(i+1));
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

    public static void writeCsvFileStatsORANGELieux(String num, List stats, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "Statistiques_Localisation" + num + ".csv";

        System.out.println("Write CSV file stats lieux:");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERStatLocalisation.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            System.out.println("dedans stats locations");
            int i = 0;
            for (Iterator it = stats.iterator(); it.hasNext();) {
                StatistiqueLieux stat = (StatistiqueLieux) it.next();
                fileWriter.append(String.valueOf(i+1));
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
