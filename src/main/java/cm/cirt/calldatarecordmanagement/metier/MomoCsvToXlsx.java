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

public class MomoCsvToXlsx {

    private String location;
    private CellStyle style;
    private CellStyle headerStyle;
    private XSSFWorkbook wb;
    private String folder;
    private String dateRequisition;

    public MomoCsvToXlsx(String folder, String dateRequisition) {
        this.folder = folder;
        this.wb = new XSSFWorkbook();
        this.location = Variables111.DESTINATION_DOSSIERS;
        this.dateRequisition = dateRequisition;
    }

    public void generateListingSheet(String csvFilePath) {
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
            Sheet sheet = wb.createSheet("Listing");

            int rowNo = 0;
            createExcelHeaderListing(sheet, rowNo);

            while ((csvRow = csvMapReader.read(headers)) != null) {
                rowNo++;
                Row excelRow = sheet.createRow(rowNo);
                createExcelRowListing(csvRow, excelRow);
            }

            for (int i = 1; i <= rowNo; i++) {
                sheet.getRow(i).getCell(0).setCellStyle(style);
                sheet.getRow(i).getCell(1).setCellStyle(style);
                sheet.getRow(i).getCell(2).setCellStyle(style);
                sheet.getRow(i).getCell(3).setCellStyle(style);
                sheet.getRow(i).getCell(4).setCellStyle(style);
                sheet.getRow(i).getCell(5).setCellStyle(style);
                sheet.getRow(i).getCell(6).setCellStyle(style);
                sheet.getRow(i).getCell(7).setCellStyle(style);
            }

            sheet.getRow(0).getCell(0).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(1).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(2).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(3).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(4).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(5).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(6).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(7).setCellStyle(headerStyle);

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);

            Cell firstCell = sheet.getRow(0).getCell(0);
            Cell lastCell = sheet.getRow(rowNo).getCell(7);
            sheet.setAutoFilter(new CellRangeAddress(0, rowNo, 0, 7));
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
            Sheet sheet = wb.createSheet("Statistique Transaction Emises");

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
            Sheet sheet = wb.createSheet("Statistique Transactions Reçues");

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
            Sheet sheet = wb.createSheet("Identification des correspondants");

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
                sheet.getRow(i).getCell(5).setCellStyle(style);
                sheet.getRow(i).getCell(6).setCellStyle(style);
            }

            sheet.getRow(0).getCell(0).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(1).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(2).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(3).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(4).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(5).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(6).setCellStyle(headerStyle);

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);

            sheet.setAutoFilter(new CellRangeAddress(0, rowNo, 0, 6));
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
        File fileListing = null;
        File fileAbonne = null;
        File fileStat = null;
        File fileStatLoc = null;
        File fileId = null;

        try {
            fileOut = new FileOutputStream(location + dateRequisition + "/" + folder + "/" + folder + "_MOMO.xlsx");
            for (File file : files) {
                if (file.getName().startsWith("Requisition_Identification_Numero")) {
                    fileAbonne = file;
                } else if (file.getName().startsWith("Requisition_Listing")) {
                    fileListing = file;
                } else if (file.getName().startsWith("Statistiques_TransactionRecues" + folder)) {
                    fileStat = file;
                } else if (file.getName().startsWith("Statistiques_transactions_emises")) {
                    fileStatLoc = file;
                } else if (file.getName().startsWith("IdentificationAbonnees")) {
                    fileId = file;
                }
            }
            if (fileAbonne != null) {
                generateAbonneSheet(fileAbonne.getAbsolutePath());
            }
            if (fileListing != null) {
                generateListingSheet(fileListing.getAbsolutePath());
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

    public void createExcelHeaderListing(Sheet sheet, int rowNo) {
        Row excelRow = sheet.createRow(rowNo);
        excelRow.createCell(0).setCellValue("Id Transaction");
        excelRow.createCell(1).setCellValue("Date Transaction");
        excelRow.createCell(2).setCellValue("Id Emetteur");
        excelRow.createCell(3).setCellValue("Numéro Emetteur");
        excelRow.createCell(4).setCellValue("Id Recepteur");
        excelRow.createCell(5).setCellValue("Numéro Recepteur");
        excelRow.createCell(6).setCellValue("Montant");
        excelRow.createCell(7).setCellValue("LIQUIDE");

    }
//IdTransaction,DateTransaction,IdEmetteur,NumeroEmetteur,IdRecepteur,NumeroRecepteur,Montant,LIQUIDE
    public void createExcelRowListing(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("IdTransaction"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("DateTransaction"));
        excelRow.createCell(2).setCellValue((String) csvRow.get("IdEmetteur"));
        excelRow.createCell(3).setCellValue((String) csvRow.get("NumeroEmetteur"));
        excelRow.createCell(4).setCellValue((String) csvRow.get("IdRecepteur"));
        excelRow.createCell(5).setCellValue((String) csvRow.get("NumeroRecepteur"));
        excelRow.createCell(6).setCellValue((String) csvRow.get("Montant"));
        excelRow.createCell(7).setCellValue((String) csvRow.get("LIQUIDE"));

    }

    public void createExcelHeaderStatistique(Sheet sheet, int rowNo) {
        Row excelRow = sheet.createRow(rowNo);
        excelRow.createCell(0).setCellValue("N°");
        excelRow.createCell(1).setCellValue("Numéro de Téléphone");
        excelRow.createCell(2).setCellValue("Montant Totale");
    }
//    N°,Numero de telephone,Occurence,Duree totale de communications

    public void createExcelRowStatistique(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("N°"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("Numero de telephone"));
        excelRow.createCell(2).setCellValue((String) csvRow.get("Montant total"));
    }

    public void createExcelHeaderStatistiqueLocalisation(Sheet sheet, int rowNo) {
        Row excelRow = sheet.createRow(rowNo);
        excelRow.createCell(0).setCellValue("N°");
        excelRow.createCell(1).setCellValue("Numéro de téléphone");
        excelRow.createCell(2).setCellValue("Montant total");
    }
//    N°,Numero de telephone,Montant total

    public void createExcelRowStatistiqueLocalisation(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("N°"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("Numero de telephone"));
        excelRow.createCell(2).setCellValue((String) csvRow.get("Montant total"));
    }

    public void createExcelHeaderIdentification(Sheet sheet, int rowNo) {
        Row excelRow = sheet.createRow(rowNo);
        excelRow.createCell(0).setCellValue("Numéro");
        excelRow.createCell(1).setCellValue("Nom et Prénom");
        excelRow.createCell(2).setCellValue("Date Naissance");
        excelRow.createCell(3).setCellValue("Numéro CNI");
        excelRow.createCell(4).setCellValue("Date Expiration CNI");
        excelRow.createCell(5).setCellValue("Adresse");
//        excelRow.createCell(6).setCellValue("");
    }
//    Numero,NomPrenom,DateNaissance,NumeroCNI,DateExpCNI,Quartier

    public void createExcelRowIdentification(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("Numero"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("NomPrenom"));
        excelRow.createCell(2).setCellValue((String) csvRow.get("DateNaissance"));
        excelRow.createCell(3).setCellValue((String) csvRow.get("NumeroCNI"));
        excelRow.createCell(4).setCellValue((String) csvRow.get("DateExpCNI"));
        excelRow.createCell(5).setCellValue((String) csvRow.get("Quartier"));
//        excelRow.createCell(6).setCellValue((String) csvRow.get(""));
    }

    public void createExcelHeaderAbonne(Sheet sheet, int rowNo) {
        Row excelRow = sheet.createRow(rowNo);
        excelRow.createCell(0).setCellValue("Numéro");
        excelRow.createCell(1).setCellValue("Nom et Prénom");
        excelRow.createCell(2).setCellValue("Date de Naissance");
        excelRow.createCell(3).setCellValue("Numero CNI");
        excelRow.createCell(4).setCellValue("Date Expiration CNI");
        excelRow.createCell(5).setCellValue("Adresse");
        excelRow.createCell(6).setCellValue("ID Compte MOMO ");
    }
//    Telephone,Noms&Prenoms,date&naissance,NumeroCNI,DateExpirationCNI,Adresse,DateActivationMOMO

    public void createExcelRowAbonne(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("Telephone"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("Noms&Prenoms"));
        excelRow.createCell(2).setCellValue((String) csvRow.get("date&naissance"));
        excelRow.createCell(3).setCellValue((String) csvRow.get("NumeroCNI"));
        excelRow.createCell(4).setCellValue((String) csvRow.get("DateExpirationCNI"));
        excelRow.createCell(5).setCellValue((String) csvRow.get("Adresse"));
        excelRow.createCell(6).setCellValue((String) csvRow.get("DateActivationMOMO"));
    }

    public static void main(String[] args) {
        MtnCsvToXlsx ctx = new MtnCsvToXlsx("653167646", "");
        ctx.generate();
    }
}
