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
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.FreqCell;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.FreqCorrespondant;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.FreqDureeAppel;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.FreqImei;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.SharedImei;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.StatistiqueAppels;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.StatistiqueLieux;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.listing;
import cm.cirt.calldatarecordmanagement.abou.csv.format.entities.identificationNEXTTEL;
import cm.cirt.calldatarecordmanagement.metier.Variables111;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Iterator;

/**
 * @ABOUBECKER 88
 *
 */
public class CsvFileWriterNEXTTELMultiple {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String FILE_HEADERIdnumero = "Telephone,Noms&Prenoms,OperateurTelephonique ,IMEI,Adresse";
    private static final String FILE_HEADERListing = "NumeroAppelant,LocalisationNumeroAppelant,IMEINumeroAppelant,DateDebutAppel,DureeAppel,NumeroAppele";
    private static final String FILE_HEADERAbonne = "Numero,NomPrenom,DateNaissance,NumeroCNI,DateExpCNI";
    private static final String FILE_HEADERFreqCellule = "Total,Cellule,0h-2h,2h-4h,4h-6h,6h-8h,8h-10h,10h-12h,12h-14h,14h-16h,16h-18h,18h-20h,20h-22h,22h-24h";
    private static final String FILE_HEADERFreqCorrespondant = "Total,TotalEntrant,TotalSortant,Telephone,Identite,0h-2h,2h-4h,4h-6h,6h-8h,8h-10h,10h-12h,12h-14h,14h-16h,16h-18h,18h-20h,20h-22h,22h-24h";
    private static final String FILE_HEADERFreqDureeAppel = "Numero,Identite,DureeAppel,NombreMessages";
    private static final String FILE_HEADERFreqImei = "Total,Imei,PremiereUtilisation,DerniereUtilisation";
    private static final String FILE_HEADERShareImei = "Numero,Imei,Identite,Occurrence,PremiereUtilisation,DerniereUtilisation";

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
            fileWriter.append(data[2]);
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("NEXTTEL");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(data[4]);
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
public static void writeCsvFileFrequenceCelluleNexttel(String num, List<FreqCell> stats, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "FrequenceCellule" + num + ".csv";

        System.out.println("Write CSV file stats:");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERFreqCellule);

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            System.out.println("dedans stats ");
            int i = 0;
            for (FreqCell freq : stats) {
                fileWriter.append(String.valueOf(freq.getTotal()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(freq.getCellule());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getZeroDeux()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getDeuxQuatre()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getQuatreSix()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getSixHuit()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getHuitDix()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getDixDouze()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getDouzeQuatorze()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getQuatorzeSeize()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getSeizeDixhuit()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getDixhuitVingt()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getVingtVingtdeux()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getVingtdeuxVingtquatre()).replace("-1", ""));                
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

    public static void writeCsvFileFreqCorrespondantNexttel(String num, List<FreqCorrespondant> stats, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "FrequenceCorrespondant" + num + ".csv";

        System.out.println("Write CSV file stats lieux:");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERFreqCorrespondant);

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            System.out.println("dedans freq corresp. ");
            int i = 0;
            for (FreqCorrespondant freq : stats) {
                fileWriter.append(String.valueOf(freq.getTotal()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getTotalEntrant()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getTotalSortant()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(freq.getTelephone());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(freq.getIdentite());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getZeroDeux()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getDeuxQuatre()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getQuatreSix()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getSixHuit()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getHuitDix()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getDixDouze()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getDouzeQuatorze()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getQuatorzeSeize()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getSeizeDixhuit()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getDixhuitVingt()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getVingtVingtdeux()).replace("-1", ""));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getVingtdeuxVingtquatre()).replace("-1", ""));                
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
    
    public static void writeCsvFileFreqDureeAppelNexttel(String num, List<FreqDureeAppel> stats, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "FrequenceDureeAppel" + num + ".csv";

        System.out.println("Write CSV file frequence duree appel:");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERFreqDureeAppel);

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            System.out.println("dedans freq duree appel ");
            int i = 0;
            for (FreqDureeAppel freq : stats) {
                fileWriter.append(String.valueOf(freq.getNumero()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getIdentite()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getDureeAppel()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getNombreMessage()));                
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

    public static void writeCsvFileFreqImeiNexttel(String num, List<FreqImei> stats, String dateRequisition) {

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "FrequenceImei" + num + ".csv";

        System.out.println("Write CSV file frequence Imei:");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERFreqImei);

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            System.out.println("dedans freq Imei ");
            int i = 0;
            for (FreqImei freq : stats) {
                fileWriter.append(String.valueOf(freq.getTotal()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(freq.getImei()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(df.format(freq.getFirtUse())));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(df.format(freq.getLastUse())));                
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

    public static void writeCsvFileSharedImeiNexttel(String num, List<SharedImei> shared, String dateRequisition) {

        String fileName = Variables111.DESTINATION_DOSSIERS + dateRequisition + "/" + num + "/" + "sharedImei" + num + ".csv";

        System.out.println("Write CSV file shared Imei:");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADERShareImei);

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            System.out.println("dedans freq Imei ");
            int i = 0;
            for (SharedImei si : shared) {
                fileWriter.append(String.valueOf(si.getNumero()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(si.getImei()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(si.getIdentite()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(si.getOccurrence()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(si.getFirstUse()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(si.getLastUse()));                
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
