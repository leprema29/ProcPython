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
import org.apache.poi.POIXMLProperties;
import org.apache.poi.hssf.util.CellRangeAddress;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

public class OrangeCsvToXlsxMultiple {

    private String location;
    private CellStyle style;
    private CellStyle headerStyle;
    private XSSFWorkbook wb;
    private String folder;
    private String dateRequisition;
    private String demandeurRequisition;

    public OrangeCsvToXlsxMultiple(String folder, String dateRequisition, String demandeurRequisition) {
        this.folder = folder;
        this.wb = new XSSFWorkbook();
        this.location = Variables111.DESTINATION_DOSSIERS;
        this.dateRequisition = dateRequisition;
        this.demandeurRequisition = demandeurRequisition;
        POIXMLProperties props = this.wb.getProperties();
        POIXMLProperties.CoreProperties coreProp = props.getCoreProperties();
        coreProp.setCreator(demandeurRequisition); //set document creator
        coreProp.setDescription("RÉQUISITION RELATIVE AUX IDENTITÉS DES ABONNÉS DE TÉLÉPHONIE MOBILE"); //set Description
//        coreProp.setKeywords("Apache POI, Metadata, Java, Example Program, XLSX "); //set keywords
        coreProp.setTitle("Réquisition"); //Title of the document
//        coreProp.setSubjectProperty("Code"); //Subject
        coreProp.setCategory("Sécurity"); //category
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

    public void generateFrequenceCelluleSheet(String csvFilePath) {
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
            Sheet sheet = wb.createSheet("Fréquence par cellule");

            int rowNo = 0;
            createExcelHeaderFrequenceCellule(sheet, rowNo);

            while ((csvRow = csvMapReader.read(headers)) != null) {
                rowNo++;
                Row excelRow = sheet.createRow(rowNo);
                createExcelRowFrequenceCellule(csvRow, excelRow);
            }

            for (int i = 1; i <= rowNo; i++) {
                for (int j = 0; j <= 13; j++) {
                    sheet.getRow(i).getCell(j).setCellStyle(style);
                }
            }

            for (int j = 0; j <= 13; j++) {
                sheet.getRow(0).getCell(j).setCellStyle(headerStyle);
            }

            for (int j = 0; j <= 13; j++) {
                sheet.autoSizeColumn(j);
            }

            sheet.setAutoFilter(new CellRangeAddress(0, rowNo, 0, 13));
            csvMapReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generateFrequenceCorrespondantSheet(String csvFilePath) {
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
            Sheet sheet = wb.createSheet("Fréquence Correspondant");

            int rowNo = 0;
            createExcelHeaderFrequenceCorrespondant(sheet, rowNo);

            while ((csvRow = csvMapReader.read(headers)) != null) {
                rowNo++;
                Row excelRow = sheet.createRow(rowNo);
                createExcelRowFrequenceCorrespondant(csvRow, excelRow);
            }

            for (int i = 1; i <= rowNo; i++) {
                for (int j = 0; j <= 16; j++) {
                    sheet.getRow(i).getCell(j).setCellStyle(style);
                }
            }

            for (int j = 0; j <= 16; j++) {
                sheet.getRow(0).getCell(j).setCellStyle(headerStyle);
            }
            
            for (int j = 0; j <= 16; j++) {
                sheet.autoSizeColumn(j);
            }

            sheet.setAutoFilter(new CellRangeAddress(0, rowNo, 0, 16));
            csvMapReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void generateFrequenceDureeAppelSheet(String csvFilePath) {
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
            Sheet sheet = wb.createSheet("Fréquence par Durée appel");

            int rowNo = 0;
            createExcelHeaderFrequenceDureeAppel(sheet, rowNo);

            while ((csvRow = csvMapReader.read(headers)) != null) {
                rowNo++;
                Row excelRow = sheet.createRow(rowNo);
                createExcelRowFrequenceDureeAppel(csvRow, excelRow);
            }

            for (int i = 1; i <= rowNo; i++) {
                for (int j = 0; j <= 3; j++) {
                    sheet.getRow(i).getCell(j).setCellStyle(style);
                }
            }

            for (int j = 0; j <= 3; j++) {
                sheet.getRow(0).getCell(j).setCellStyle(headerStyle);
            }
            
            for (int j = 0; j <= 3; j++) {
                sheet.autoSizeColumn(j);
            }

            sheet.setAutoFilter(new CellRangeAddress(0, rowNo, 0, 3));
            csvMapReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void generateFrequenceImeiSheet(String csvFilePath) {
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
            Sheet sheet = wb.createSheet("Fréquence par IMEI");

            int rowNo = 0;
            createExcelHeaderFrequenceImei(sheet, rowNo);

            while ((csvRow = csvMapReader.read(headers)) != null) {
                rowNo++;
                Row excelRow = sheet.createRow(rowNo);
                createExcelRowFrequenceImei(csvRow, excelRow);
            }

            for (int i = 1; i <= rowNo; i++) {
                for (int j = 0; j <= 3; j++) {
                    sheet.getRow(i).getCell(j).setCellStyle(style);
                }
            }

            for (int j = 0; j <= 3; j++) {
                sheet.getRow(0).getCell(j).setCellStyle(headerStyle);
            }
            
            for (int j = 0; j <= 3; j++) {
                sheet.autoSizeColumn(j);
            }

            sheet.setAutoFilter(new CellRangeAddress(0, rowNo, 0, 3));
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

    public void generateShareImeiSheet(String csvFilePath) {
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
            Sheet sheet = wb.createSheet("Imei partagé");

            int rowNo = 0;
            createExcelHeaderSharedImei(sheet, rowNo);

            while ((csvRow = csvMapReader.read(headers)) != null) {
                rowNo++;
                Row excelRow = sheet.createRow(rowNo);
                createExcelRowShareImei(csvRow, excelRow);
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

            sheet.setAutoFilter(new CellRangeAddress(0, rowNo, 0, 5));
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
        File fileFreqCell = null;
        File fileCorresp = null;
        File fileDureeAppel = null;
        File fileImei = null;
        File fileId = null;
        File fileSharedImei = null;

        try {
            fileOut = new FileOutputStream(location + dateRequisition + "/" + folder + "/" + folder + ".xlsx");
            for (File file : files) {
                if (file.getName().startsWith("Requisition_Identification_Numero")) {
                    fileAbonne = file;
                } else if (file.getName().startsWith("Requisition_Listing_Emis")) {
                    fileListingAppel = file;
                } else if (file.getName().startsWith("FrequenceCellule" + folder)) {
                    fileFreqCell = file;
                } else if (file.getName().startsWith("FrequenceCorrespondant")) {
                    fileCorresp = file;
                } else if (file.getName().startsWith("FrequenceDureeAppel")) {
                    fileDureeAppel = file;
                } else if (file.getName().startsWith("FrequenceImei")) {
                    fileImei = file;
                } else if (file.getName().startsWith("IdentificationAbonnees")) {
                    fileId = file;
                } else if (file.getName().startsWith("Requisition_Listing_SMS")) {
                    fileListingSms = file;
                } else if (file.getName().startsWith("sharedImei")) {
                    fileSharedImei = file;
                }
            }
            if (fileAbonne != null) {
                generateAbonneSheet(fileAbonne.getAbsolutePath());
            }
            if (fileSharedImei != null) {
                generateShareImeiSheet(fileSharedImei.getAbsolutePath());
            }
            if (fileListingAppel != null) {
                generateListingAppelSheet(fileListingAppel.getAbsolutePath());
            }
            if (fileListingSms != null) {
                generateListingSmsSheet(fileListingSms.getAbsolutePath());
            }
            if (fileFreqCell != null) {
                generateFrequenceCelluleSheet(fileFreqCell.getAbsolutePath());
            }
            if (fileCorresp != null) {
                generateFrequenceCorrespondantSheet(fileCorresp.getAbsolutePath());
            }
            if (fileDureeAppel != null) {
                generateFrequenceDureeAppelSheet(fileDureeAppel.getAbsolutePath());
            }
            if (fileImei != null) {
                generateFrequenceImeiSheet(fileImei.getAbsolutePath());
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
        excelRow.createCell(1).setCellValue("Localisation numéro appelant (Longitude, Latitude)");
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
        excelRow.createCell(1).setCellValue("Localisation numéro Destination (Longitude, Latitude)");
        excelRow.createCell(2).setCellValue("IMEI numéro récepteur");
        excelRow.createCell(3).setCellValue("Date SMS");
        excelRow.createCell(4).setCellValue("Numéro récepteur");

    }
// NumeroEnvoi,LocalisationNumeroDest,IMEINumeroDest,DateSMS,NumeroDest

    public void createExcelRowListingSms(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("NumeroEnvoi"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("LocalisationNumeroDest"));
        excelRow.createCell(2).setCellValue((String) csvRow.get("IMEINumeroDest"));
        excelRow.createCell(3).setCellValue((String) csvRow.get("DateSMS"));
        excelRow.createCell(4).setCellValue((String) csvRow.get("NumeroDest"));

    }

   public void createExcelHeaderFrequenceCellule(Sheet sheet, int rowNo) {
        Row excelRow = sheet.createRow(rowNo);
        excelRow.createCell(0).setCellValue("Total");
        excelRow.createCell(1).setCellValue("Cellule");
        excelRow.createCell(2).setCellValue("0 à 2h");
        excelRow.createCell(3).setCellValue("2 à 4h");
        excelRow.createCell(4).setCellValue("4 à 6h");
        excelRow.createCell(5).setCellValue("6 à 8h");
        excelRow.createCell(6).setCellValue("8 à 10h");
        excelRow.createCell(7).setCellValue("10 à 12h");
        excelRow.createCell(8).setCellValue("12 à 14h");
        excelRow.createCell(9).setCellValue("14 à 16h");
        excelRow.createCell(10).setCellValue("16 à 18h");
        excelRow.createCell(11).setCellValue("18 à 20h");
        excelRow.createCell(12).setCellValue("20 à 22h");
        excelRow.createCell(13).setCellValue("22 à 24h");

    }
//    Total,Cellule,0h-2h,2h-4h,4h-6h,6h-8h,8h-10h,10h-12h,12h-14h,14h-16h,16h-18h,18h-20h,20h-22h,22h-24h

    public void createExcelRowFrequenceCellule(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("Total"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("Cellule"));
        excelRow.createCell(2).setCellValue((String) csvRow.get("0h-2h"));
        excelRow.createCell(3).setCellValue((String) csvRow.get("2h-4h"));
        excelRow.createCell(4).setCellValue((String) csvRow.get("4h-6h"));
        excelRow.createCell(5).setCellValue((String) csvRow.get("6h-8h"));
        excelRow.createCell(6).setCellValue((String) csvRow.get("8h-10h"));
        excelRow.createCell(7).setCellValue((String) csvRow.get("10h-12h"));
        excelRow.createCell(8).setCellValue((String) csvRow.get("12h-14h"));
        excelRow.createCell(9).setCellValue((String) csvRow.get("14h-16h"));
        excelRow.createCell(10).setCellValue((String) csvRow.get("16h-18h"));
        excelRow.createCell(11).setCellValue((String) csvRow.get("18h-20h"));
        excelRow.createCell(12).setCellValue((String) csvRow.get("20h-22h"));
        excelRow.createCell(13).setCellValue((String) csvRow.get("22h-24h"));

    }

    public void createExcelHeaderFrequenceCorrespondant(Sheet sheet, int rowNo) {
        Row excelRow = sheet.createRow(rowNo);
        excelRow.createCell(0).setCellValue("Total");
        excelRow.createCell(1).setCellValue("Total Entrant");
        excelRow.createCell(2).setCellValue("Total Sortant");
        excelRow.createCell(3).setCellValue("Téléphone");
        excelRow.createCell(4).setCellValue("Identité");
        excelRow.createCell(5).setCellValue("0 à 2h");
        excelRow.createCell(6).setCellValue("2 à 4h");
        excelRow.createCell(7).setCellValue("4 à 6h");
        excelRow.createCell(8).setCellValue("6 à 8h");
        excelRow.createCell(9).setCellValue("8 à 10h");
        excelRow.createCell(10).setCellValue("10 à 12h");
        excelRow.createCell(11).setCellValue("12 à 14h");
        excelRow.createCell(12).setCellValue("14 à 16h");
        excelRow.createCell(13).setCellValue("16 à 18h");
        excelRow.createCell(14).setCellValue("18 à 20h");
        excelRow.createCell(15).setCellValue("20 à 22h");
        excelRow.createCell(16).setCellValue("22 à 24h");

    }
//    Total,Cellule,0h-2h,2h-4h,4h-6h,6h-8h,8h-10h,10h-12h,12h-14h,14h-16h,16h-18h,18h-20h,20h-22h,22h-24h

    public void createExcelRowFrequenceCorrespondant(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("Total"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("TotalEntrant"));
        excelRow.createCell(2).setCellValue((String) csvRow.get("TotalSortant"));
        excelRow.createCell(3).setCellValue((String) csvRow.get("Telephone"));
        excelRow.createCell(4).setCellValue((String) csvRow.get("Identite"));
        excelRow.createCell(5).setCellValue((String) csvRow.get("0h-2h"));
        excelRow.createCell(6).setCellValue((String) csvRow.get("2h-4h"));
        excelRow.createCell(7).setCellValue((String) csvRow.get("4h-6h"));
        excelRow.createCell(8).setCellValue((String) csvRow.get("6h-8h"));
        excelRow.createCell(9).setCellValue((String) csvRow.get("8h-10h"));
        excelRow.createCell(10).setCellValue((String) csvRow.get("10h-12h"));
        excelRow.createCell(11).setCellValue((String) csvRow.get("12h-14h"));
        excelRow.createCell(12).setCellValue((String) csvRow.get("14h-16h"));
        excelRow.createCell(13).setCellValue((String) csvRow.get("16h-18h"));
        excelRow.createCell(14).setCellValue((String) csvRow.get("18h-20h"));
        excelRow.createCell(15).setCellValue((String) csvRow.get("20h-22h"));
        excelRow.createCell(16).setCellValue((String) csvRow.get("22h-24h"));

    }

    public void createExcelHeaderFrequenceDureeAppel(Sheet sheet, int rowNo) {
        Row excelRow = sheet.createRow(rowNo);
        excelRow.createCell(0).setCellValue("Numéro");
        excelRow.createCell(1).setCellValue("Identité");
        excelRow.createCell(2).setCellValue("Durée de l'appel");
        excelRow.createCell(3).setCellValue("Nombre de messages");
    }
//    Numero,Identite,DureeAppel,NombreMessages

    public void createExcelRowFrequenceDureeAppel(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("Numero"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("Identite"));
        excelRow.createCell(2).setCellValue((String) csvRow.get("DureeAppel"));
        excelRow.createCell(3).setCellValue((String) csvRow.get("NombreMessages"));
    }

    public void createExcelHeaderFrequenceImei(Sheet sheet, int rowNo) {
        Row excelRow = sheet.createRow(rowNo);
        excelRow.createCell(0).setCellValue("Total");
        excelRow.createCell(1).setCellValue("IMEI");
        excelRow.createCell(2).setCellValue("Première utilisation");
        excelRow.createCell(3).setCellValue("Dernière utilisation");
    }
//    Total,Imei,PremiereUtilisation,DerniereUtilisation

    public void createExcelRowFrequenceImei(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("Total"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("Imei"));
        excelRow.createCell(2).setCellValue((String) csvRow.get("PremiereUtilisation"));
        excelRow.createCell(3).setCellValue((String) csvRow.get("DerniereUtilisation"));
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
    
    public void createExcelHeaderSharedImei(Sheet sheet, int rowNo) {
        Row excelRow = sheet.createRow(rowNo);
        excelRow.createCell(0).setCellValue("Numéro");
        excelRow.createCell(1).setCellValue("IMEI");
        excelRow.createCell(2).setCellValue("Identité");
        excelRow.createCell(3).setCellValue("Occurrences");
        excelRow.createCell(4).setCellValue("Première utilisation");
        excelRow.createCell(5).setCellValue("Dernière utilisation");
//        excelRow.createCell(5).setCellValue("");
    }
//    Numero,Imei,Identite,Occurrence,PremiereUtilisation,DerniereUtilisation

    public void createExcelRowShareImei(Map csvRow, Row excelRow) {
        excelRow.createCell(0).setCellValue((String) csvRow.get("Numero"));
        excelRow.createCell(1).setCellValue((String) csvRow.get("Imei"));
        excelRow.createCell(2).setCellValue((String) csvRow.get("Identite"));
        excelRow.createCell(3).setCellValue((String) csvRow.get("Occurrence"));
        excelRow.createCell(4).setCellValue((String) csvRow.get("PremiereUtilisation"));
        excelRow.createCell(5).setCellValue((String) csvRow.get("DerniereUtilisation"));
    }

    public static void main(String[] args) {
//        OrangeCsvToXlsx ctx = new OrangeCsvToXlsx("690147926", "");
//        ctx.generate();
    }
}
