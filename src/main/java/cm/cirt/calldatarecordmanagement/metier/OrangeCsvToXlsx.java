/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.metier;

/**
 *
 * @author Harry Wanki
 */
import java.io.*;
import java.util.Map;
import org.apache.poi.hssf.util.CellRangeAddress;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

public class OrangeCsvToXlsx {

    private String location;
    private CellStyle style;
    private CellStyle headerStyle;
    private XSSFWorkbook wb;
    private String folder;
    private String dateRequisition;

    public OrangeCsvToXlsx(String folder, String dateRequisition) {
        this.folder = folder;
        this.wb = new XSSFWorkbook();
        this.location = Variables111.DESTINATION_DOSSIERS;
        this.dateRequisition = dateRequisition;
    }

    public void generateListingAppelSheet(String csvFilePath) {
        style = wb.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);

        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Calibri");
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        font.setItalic(false);

        headerStyle = wb.createCellStyle();
        headerStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setFont(font);
        try {
            ICsvMapReader csvMapReader = new CsvMapReader(new FileReader(
                    csvFilePath), CsvPreference.EXCEL_PREFERENCE);
            final String[] headers = csvMapReader.getHeader(true);
            Map csvRow;
            Sheet sheet = wb.createSheet("Listing Appel");

            int rowNo = 0;
            createExcelHeaderListingAppel(sheet, rowNo);

            while ((csvRow = csvMapReader.read(headers)) != null) {
                rowNo++;
                Row excelRow = sheet.createRow(rowNo);
                createExcelRowListingAppel(csvRow, excelRow);
            }

            for (int i = 1; i <= rowNo; i++) {
                sheet.getRow(i).getCell(0).setCellStyle(style);
                sheet.getRow(i).getCell(1).setCellStyle(style);
                sheet.getRow(i).getCell(2).setCellStyle(style);
                sheet.getRow(i).getCell(3).setCellStyle(style);
                sheet.getRow(i).getCell(4).setCellStyle(style);
                sheet.getRow(i).getCell(5).setCellStyle(style);
            }

            sheet.getRow(0).getCell(0).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(1).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(2).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(3).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(4).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(5).setCellStyle(headerStyle);

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);

            Cell firstCell = sheet.getRow(0).getCell(0);
            Cell lastCell = sheet.getRow(rowNo).getCell(5);
            sheet.setAutoFilter(new CellRangeAddress(0, rowNo, 0, 5));
//            sheet.setAutoFilter(new CellRangeAddress(firstCell.getRow(), lastCell.getRow(), 0, lastCell.getCol()));
            csvMapReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generateListingSmsSheet(String csvFilePath) {
        style = wb.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);

        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Calibri");
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        font.setItalic(false);

        headerStyle = wb.createCellStyle();
        headerStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setFont(font);
        try {
            ICsvMapReader csvMapReader = new CsvMapReader(new FileReader(
                    csvFilePath), CsvPreference.EXCEL_PREFERENCE);
            final String[] headers = csvMapReader.getHeader(true);
            Map csvRow;
            Sheet sheet = wb.createSheet("Listing SMS");

            int rowNo = 0;
            createExcelHeaderListingSms(sheet, rowNo);

            while ((csvRow = csvMapReader.read(headers)) != null) {
                rowNo++;
                Row excelRow = sheet.createRow(rowNo);
                createExcelRowListingSms(csvRow, excelRow);
            }

            for (int i = 1; i <= rowNo; i++) {
                sheet.getRow(i).getCell(0).setCellStyle(style);
                sheet.getRow(i).getCell(1).setCellStyle(style);
                sheet.getRow(i).getCell(2).setCellStyle(style);
                sheet.getRow(i).getCell(3).setCellStyle(style);
                sheet.getRow(i).getCell(4).setCellStyle(style);
            }

            sheet.getRow(0).getCell(0).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(1).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(2).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(3).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(4).setCellStyle(headerStyle);

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);

            Cell firstCell = sheet.getRow(0).getCell(0);
            Cell lastCell = sheet.getRow(rowNo).getCell(4);
            sheet.setAutoFilter(new CellRangeAddress(0, rowNo, 0, 4));
//            sheet.setAutoFilter(new CellRangeAddress(firstCell.getRow(), lastCell.getRow(), 0, lastCell.getCol()));
            csvMapReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generatesStatistiqueSheet(String csvFilePath) {
        style = wb.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);

        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Calibri");
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        font.setItalic(false);

        headerStyle = wb.createCellStyle();
        headerStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setFont(font);
        try {
            ICsvMapReader csvMapReader = new CsvMapReader(new FileReader(
                    csvFilePath), CsvPreference.EXCEL_PREFERENCE);
            final String[] headers = csvMapReader.getHeader(true);
            Map csvRow;
            Sheet sheet = wb.createSheet("Statistique Appel");

            int rowNo = 0;
            createExcelHeaderStatistique(sheet, rowNo);

            while ((csvRow = csvMapReader.read(headers)) != null) {
                rowNo++;
                Row excelRow = sheet.createRow(rowNo);
                createExcelRowStatistique(csvRow, excelRow);
            }

            for (int i = 1; i <= rowNo; i++) {
                sheet.getRow(i).getCell(0).setCellStyle(style);
                sheet.getRow(i).getCell(1).setCellStyle(style);
                sheet.getRow(i).getCell(2).setCellStyle(style);
                sheet.getRow(i).getCell(3).setCellStyle(style);
            }

            sheet.getRow(0).getCell(0).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(1).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(2).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(3).setCellStyle(headerStyle);

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);

            sheet.setAutoFilter(new CellRangeAddress(0, rowNo, 0, 3));
            csvMapReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generateStatistiqueLocalisationSheet(String csvFilePath) {
        style = wb.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);

        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Calibri");
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        font.setItalic(false);

        headerStyle = wb.createCellStyle();
        headerStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setFont(font);
        try {
            ICsvMapReader csvMapReader = new CsvMapReader(new FileReader(
                    csvFilePath), CsvPreference.EXCEL_PREFERENCE);
            final String[] headers = csvMapReader.getHeader(true);
            Map csvRow;
            Sheet sheet = wb.createSheet("Statistique Localisation");

            int rowNo = 0;
            createExcelHeaderStatistiqueLocalisation(sheet, rowNo);

            while ((csvRow = csvMapReader.read(headers)) != null) {
                rowNo++;
                Row excelRow = sheet.createRow(rowNo);
                createExcelRowStatistiqueLocalisation(csvRow, excelRow);
            }

            for (int i = 1; i <= rowNo; i++) {
                sheet.getRow(i).getCell(0).setCellStyle(style);
                sheet.getRow(i).getCell(1).setCellStyle(style);
                sheet.getRow(i).getCell(2).setCellStyle(style);
            }

            sheet.getRow(0).getCell(0).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(1).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(2).setCellStyle(headerStyle);

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);

            sheet.setAutoFilter(new CellRangeAddress(0, rowNo, 0, 2));
            csvMapReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generateIdentificationSheet(String csvFilePath) {
        style = wb.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);

        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Calibri");
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        font.setItalic(false);

        headerStyle = wb.createCellStyle();
        headerStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setFont(font);
        try {
            ICsvMapReader csvMapReader = new CsvMapReader(new FileReader(
                    csvFilePath), CsvPreference.EXCEL_PREFERENCE);
            final String[] headers = csvMapReader.getHeader(true);
            Map csvRow;
            Sheet sheet = wb.createSheet("Identification des abonnés");

            int rowNo = 0;
            createExcelHeaderIdentification(sheet, rowNo);

            while ((csvRow = csvMapReader.read(headers)) != null) {
                rowNo++;
                Row excelRow = sheet.createRow(rowNo);
                createExcelRowIdentification(csvRow, excelRow);
            }

            for (int i = 1; i <= rowNo; i++) {
                sheet.getRow(i).getCell(0).setCellStyle(style);
                sheet.getRow(i).getCell(1).setCellStyle(style);
                sheet.getRow(i).getCell(2).setCellStyle(style);
                sheet.getRow(i).getCell(3).setCellStyle(style);
                sheet.getRow(i).getCell(4).setCellStyle(style);
                sheet.getRow(i).getCell(5).setCellStyle(style);
//                sheet.getRow(i).getCell(6).setCellStyle(style);
            }

            sheet.getRow(0).getCell(0).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(1).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(2).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(3).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(4).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(5).setCellStyle(headerStyle);
//            sheet.getRow(0).getCell(6).setCellStyle(headerStyle);

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
//            sheet.autoSizeColumn(6);

            sheet.setAutoFilter(new CellRangeAddress(0, rowNo, 0, 5));
            csvMapReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generateAbonneSheet(String csvFilePath) {
        style = wb.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);

        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Calibri");
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        font.setItalic(false);

        headerStyle = wb.createCellStyle();
        headerStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setFont(font);
        try {
            ICsvMapReader csvMapReader = new CsvMapReader(new FileReader(
                    csvFilePath), CsvPreference.EXCEL_PREFERENCE);
            final String[] headers = csvMapReader.getHeader(true);
            Map csvRow;
            Sheet sheet = wb.createSheet("Abonné");

            int rowNo = 0;
            createExcelHeaderAbonne(sheet, rowNo);

            while ((csvRow = csvMapReader.read(headers)) != null) {
                rowNo++;
                Row excelRow = sheet.createRow(rowNo);
                createExcelRowAbonne(csvRow, excelRow);
            }

            for (int i = 1; i <= rowNo; i++) {
                sheet.getRow(i).getCell(0).setCellStyle(style);
                sheet.getRow(i).getCell(1).setCellStyle(style);
                sheet.getRow(i).getCell(2).setCellStyle(style);
                sheet.getRow(i).getCell(3).setCellStyle(style);
                sheet.getRow(i).getCell(4).setCellStyle(style);
            }

            sheet.getRow(0).getCell(0).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(1).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(2).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(3).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(4).setCellStyle(headerStyle);

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);

            sheet.setAutoFilter(new CellRangeAddress(0, rowNo, 0, 4));
            csvMapReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generate() {
        File directory = new File(location + dateRequisition + "/" + folder + "/");
        File[] files = directory.listFiles();
        FileOutputStream fileOut;
        File fileListingAppel = null;
        File fileListingSms = null;
        File fileAbonne = null;
        File fileStat = null;
        File fileStatLoc = null;
        File fileId = null;

        try {
            fileOut = new FileOutputStream(location + dateRequisition + "/" + folder + "/" + folder + ".xlsx");
            for (File file : files) {
                if (file.getName().startsWith("Requisition_Identification_Numero")) {
                    fileAbonne = file;
                } else if (file.getName().startsWith("Requisition_Listing_Emis")) {
                    fileListingAppel = file;
                } else if (file.getName().startsWith("Statistiques" + folder)) {
                    fileStat = file;
                } else if (file.getName().startsWith("Statistiques_Localisation")) {
                    fileStatLoc = file;
                } else if (file.getName().startsWith("IdentificationAbonnees")) {
                    fileId = file;
                } else if (file.getName().startsWith("Requisition_Listing_SMS")) {
                    fileListingSms = file;
                }
            }
            if (fileAbonne != null) {
                generateAbonneSheet(fileAbonne.getAbsolutePath());
            }
            if (fileListingAppel != null) {
                generateListingAppelSheet(fileListingAppel.getAbsolutePath());
            }
            if (fileListingSms != null) {
                generateListingSmsSheet(fileListingSms.getAbsolutePath());
            }
            if (fileStat != null) {
                generatesStatistiqueSheet(fileStat.getAbsolutePath());
            }
            if (fileStatLoc != null) {
                generateStatistiqueLocalisationSheet(fileStatLoc.getAbsolutePath());
            }
            if (fileId != null) {
                generateIdentificationSheet(fileId.getAbsolutePath());
            }
            wb.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void createExcelHeaderListingAppel(Sheet sheet, int rowNo) {
        Row excelRow = sheet.createRow(rowNo);
        excelRow.createCell(0).setCellValue("Numéro Appelant");
        excelRow.createCell(1).setCellValue("Localisation numéro appelant");
        excelRow.createCell(2).setCellValue("IMEI numéro appelant");
        excelRow.createCell(3).setCellValue("Date Début appel");
        excelRow.createCell(4).setCellValue("Durée appel");
        excelRow.createCell(5).setCellValue("Numéro appelé");

    }
//NumeroAppelant,LocalisationNumeroAppelant,IMEINumeroAppelant,DateDebutAppel,DureeAppel,NumeroAppele

    public void createExcelRowListingAppel(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("NumeroAppelant"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("LocalisationNumeroAppelant"));
        excelRow.createCell(2).setCellValue((String) csvRow.get("IMEINumeroAppelant"));
        excelRow.createCell(3).setCellValue((String) csvRow.get("DateDebutAppel"));
        excelRow.createCell(4).setCellValue((String) csvRow.get("DureeAppel"));
        excelRow.createCell(5).setCellValue((String) csvRow.get("NumeroAppele"));

    }

    public void createExcelHeaderListingSms(Sheet sheet, int rowNo) {
        Row excelRow = sheet.createRow(rowNo);
        excelRow.createCell(0).setCellValue("Numéro émetteur");
        excelRow.createCell(1).setCellValue("Localisation numéro récepteur");
        excelRow.createCell(2).setCellValue("IMEI numéro récepteur");
        excelRow.createCell(3).setCellValue("Date SMS");
        excelRow.createCell(4).setCellValue("Numéro recepteur");

    }
// NumeroEnvoi,LocalisationNumeroDest,IMEINumeroDest,DateSMS,NumeroDest

    public void createExcelRowListingSms(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("NumeroEnvoi"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("LocalisationNumeroDest"));
        excelRow.createCell(2).setCellValue((String) csvRow.get("IMEINumeroDest"));
        excelRow.createCell(3).setCellValue((String) csvRow.get("DateSMS"));
        excelRow.createCell(4).setCellValue((String) csvRow.get("NumeroDest"));

    }

    public void createExcelHeaderStatistique(Sheet sheet, int rowNo) {
        Row excelRow = sheet.createRow(rowNo);
        excelRow.createCell(0).setCellValue("N°");
        excelRow.createCell(1).setCellValue("Téléphone");
        excelRow.createCell(2).setCellValue("Occurrence");
        excelRow.createCell(3).setCellValue("Durée Totale");
    }
//    N°,Numero de telephone,Occurence,Duree totale de communications

    public void createExcelRowStatistique(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("N°"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("Numero de telephone"));
        excelRow.createCell(2).setCellValue((String) csvRow.get("Occurence"));
        excelRow.createCell(3).setCellValue((String) csvRow.get("Duree totale de communications"));
    }

    public void createExcelHeaderStatistiqueLocalisation(Sheet sheet, int rowNo) {
        Row excelRow = sheet.createRow(rowNo);
        excelRow.createCell(0).setCellValue("N°");
        excelRow.createCell(1).setCellValue("Localisation");
        excelRow.createCell(2).setCellValue("Occurrence");
    }
//    N°,Localisation ,Occurence

    public void createExcelRowStatistiqueLocalisation(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("N°"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("Localisation "));
        excelRow.createCell(2).setCellValue((String) csvRow.get("Occurence"));
    }

    public void createExcelHeaderIdentification(Sheet sheet, int rowNo) {
        Row excelRow = sheet.createRow(rowNo);
        excelRow.createCell(0).setCellValue("Numéro");
        excelRow.createCell(1).setCellValue("Nom et Prénom");
        excelRow.createCell(2).setCellValue("Date Naissance");
        excelRow.createCell(3).setCellValue("Numéro CNI");
        excelRow.createCell(4).setCellValue("Date Expiration CNI");
        excelRow.createCell(5).setCellValue("Adresse");
        excelRow.createCell(6).setCellValue("");
    }
//    Numero,NomPrenom,DateNaissance,NumeroCNI,DateExpCNI,Quartier

    public void createExcelRowIdentification(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("Numero"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("NomPrenom"));
        excelRow.createCell(2).setCellValue((String) csvRow.get("DateNaissance"));
        excelRow.createCell(3).setCellValue((String) csvRow.get("NumeroCNI"));
        excelRow.createCell(4).setCellValue((String) csvRow.get("DateExpCNI"));
        excelRow.createCell(5).setCellValue((String) csvRow.get("Quartier"));
        excelRow.createCell(6).setCellValue((String) csvRow.get(""));
    }

    public void createExcelHeaderAbonne(Sheet sheet, int rowNo) {
        Row excelRow = sheet.createRow(rowNo);
        excelRow.createCell(0).setCellValue("Numéro");
        excelRow.createCell(1).setCellValue("Nom et Prénom");
        excelRow.createCell(2).setCellValue("Opérateur");
        excelRow.createCell(3).setCellValue("IMEI");
        excelRow.createCell(4).setCellValue("Adresse");
        excelRow.createCell(5).setCellValue("");
    }
//    Telephone,Noms&Prenoms,OperateurTelephonique ,IMEI,Adresse

    public void createExcelRowAbonne(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("Telephone"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("Noms&Prenoms"));
        excelRow.createCell(2).setCellValue((String) csvRow.get("OperateurTelephonique "));
        excelRow.createCell(3).setCellValue((String) csvRow.get("IMEI"));
        excelRow.createCell(4).setCellValue((String) csvRow.get("Adresse"));
        excelRow.createCell(4).setCellValue((String) csvRow.get("Adresse2"));
    }

    public static void main(String[] args) {
        OrangeCsvToXlsx ctx = new OrangeCsvToXlsx("690147926", "");
        ctx.generate();
    }
}
