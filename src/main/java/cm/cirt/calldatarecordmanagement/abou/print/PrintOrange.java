/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.abou.print;

/**
 *
 * @author aboubecker
 */
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PrintOrange {

//    String LOGO_PATH = "image" + System.getProperty("file.separator") + "Untitled.png";
    String LOGO_PATH = "C:\\Users\\cinfopres\\Documents\\NetBeansProjects\\RequisitionMONC\\src\\print\\entete.jpeg";
    public static String id_number_Mtn[] = new String[5];

    Font myContentStyle = new Font();
    /* Define a new Font Object */
    public static final Font catFontTitreFicheCandidature = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
    public static final Font catFontTitreposteCandidature = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);
    public static final Font catFontvalueFicheCandidature = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
    public static final Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    public static final Font catFont_organe = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    public static final Font titre_s = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    public static final Font titre_ss = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    public static final Font titre_cb = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
    public static final Font titre_fait = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
    public static final Font titre_cel = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
    public static final Font titre_tab = new Font(Font.FontFamily.COURIER, 11, Font.BOLD);
    public static final Font catFont_titre = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
    public static final Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    public static final Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.UNDERLINE, BaseColor.BLACK);
    public static final Font FontTitre = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK);
    public static final Font FontValue = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.UNDERLINE, BaseColor.BLACK);
    public static final Font FontValue_Value_tab = new Font(Font.FontFamily.TIMES_ROMAN, 8);
    public static final Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLDITALIC);
    public static final Font smallBoldseparateur = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.BOLD);
    public static final Font smallBoldUnite = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLDITALIC);

    public Document createfile(String number, Document document) throws BadElementException, IOException {

        try {
            System.out.println("charger entete");
            PdfWriter.getInstance(document, new FileOutputStream("Requisition_" + number + ".pdf"));

            document.open();

            TitreDocument(document, "RÉQUISITION RELATIVE AUX IDENTITÉS DES ABONNÉS DE TÉLÉPHONIE MOBILE");

        } catch (FileNotFoundException | DocumentException e) {
        }
        return document;
    }

    public Document PrintIDAbonneOrange(String number, Document document, String[] data) throws BadElementException, IOException {
        try {
//656026559|175677767|DEDEA AKI|1990-03-07|2022-06-04|EDEA-EDEA
            TitrePartie(document, "I.  Identité de l’abonné ");

            PdfPTable table = new PdfPTable(4);

            table.setWidthPercentage(100);

            PdfPCell c1 = new PdfPCell(new Phrase("Téléphone :", smallBold));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c1);
            table.addCell(MakeCellValueTableau(number));
            PdfPCell c2 = new PdfPCell(new Phrase("Nom et Prénom :", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c2);
            table.addCell(MakeCellValueTableau(data[3]));

            c1 = new PdfPCell(new Phrase("Opérateur téléphonique :", smallBold));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c1);
            table.addCell(MakeCellValueTableau("ORANGE"));
            c2 = new PdfPCell(new Phrase("Numéro CNI :", smallBold));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c2);
            table.addCell(MakeCellValueTableau(data[2]));

//            System.out.println("data[1]" + data[1]);
            c1 = new PdfPCell(new Phrase("IMEI : ", smallBold));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c1);
            table.addCell(MakeCellValueTableau(""));
            c2 = new PdfPCell(new Phrase("Adresse :", smallBold));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c2);
            table.addCell(MakeCellValueTableau(data[6]));

            document.add(table);
            System.out.println("document PrintIDAbonneOrange ");

        } catch (DocumentException e) {
        }
        return document;
    }

    public PdfPTable PrintAddListingOrangeEmis(Document document, String[] data, PdfPTable table1) throws BadElementException, IOException {

//        18/07/2018 12:58:47,54,237695282679,693203710,,0000,352193096331790,237693203710,H-Paradis,Yaounde
        PdfPCell c2 = new PdfPCell();
        table1.addCell(MakeCellRequi(data[3]));
        table1.addCell(MakeCellRequi(data[8] + ", " + data[9]));
        table1.addCell(MakeCellRequi(data[6]));
        table1.addCell(MakeCellRequi(data[0]));
        table1.addCell(MakeCellRequi(data[1]));
        table1.addCell(MakeCellRequi(data[2]));
        return table1;
    }

    public PdfPTable PrintAddListingOrangeRecus(Document document, String[] data, PdfPTable table1) throws BadElementException, IOException {

//        18/07/2018 12:58:47,54,237695282679,693203710,,0000,352193096331790,237693203710,H-Paradis,Yaounde
        PdfPCell c2 = new PdfPCell();
        table1.addCell(MakeCellRequi(data[2]));
        table1.addCell(MakeCellRequi(data[8] + ", " + data[9]));
        table1.addCell(MakeCellRequi(data[6]));
        table1.addCell(MakeCellRequi(data[0]));
        table1.addCell(MakeCellRequi(data[1]));
        table1.addCell(MakeCellRequi(data[3]));
        return table1;
    }

    public PdfPTable PrintAddListingOrangeSMS(Document document, String[] data, PdfPTable table1) throws BadElementException, IOException {

//21/07/2018 05:33:52,,,,237697812220,0007,352193096331790,237693203710,H-Paradis,Yaounde
//237697812220 Chapelle-Manguier-IHS, Yaounde 352193096331790 23/07/2018 06:13:14 237693203710
        PdfPCell c2 = new PdfPCell();
        table1.addCell(MakeCellRequi(data[4]));
        table1.addCell(MakeCellRequi(data[8] + ", " + data[9]));
        table1.addCell(MakeCellRequi(data[6]));
        table1.addCell(MakeCellRequi(data[0]));
        table1.addCell(MakeCellRequi(data[7]));

        return table1;
    }

    public PdfPTable PrintAddIdAbonneOrange(Document document, String[] data, PdfPTable table1) throws BadElementException, IOException {
//656026559|175677767|DEDEA AKI|1990-03-07|2022-06-04|EDEA-EDEA
        PdfPCell c2 = new PdfPCell();
        table1.addCell(MakeCellRequi(data[0]));
        table1.addCell(MakeCellRequi(data[2]));
        table1.addCell(MakeCellRequi(data[3]));
        table1.addCell(MakeCellRequi(data[1]));
        table1.addCell(MakeCellRequi(data[4]));
        table1.addCell(MakeCellRequi(data[5]));

        return table1;
    }

    public PdfPTable PrintAddStatsFREORANGE(Document document, String[] data, PdfPTable table1, int i) throws BadElementException, IOException {

        PdfPCell c2 = new PdfPCell();
        table1.addCell(MakeCellRequi(String.valueOf(i)));
        table1.addCell(MakeCellRequi(data[2]));
        table1.addCell(MakeCellRequi(data[0]));
        table1.addCell(MakeCellRequi(data[1])); // charger la dure
        return table1;
    }

    public PdfPTable PrintAddStatsORANGELieux(Document document, String[] data, PdfPTable table1, int i) throws BadElementException, IOException {

        PdfPCell c2 = new PdfPCell();
        table1.addCell(MakeCellRequi(String.valueOf(i)));
        table1.addCell(MakeCellRequi(data[1]));
        table1.addCell(MakeCellRequi(data[0]));

        return table1;
    }

    public static PdfPCell MakeCellValueTableau(String value) {
        PdfPCell cell = new PdfPCell(new Phrase(value, FontValue_Value_tab));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

    public static void TitreDocument(Document document, String chaine) throws DocumentException, BadElementException {
        Paragraph para1 = new Paragraph(chaine, catFont_titre);
        para1.setAlignment(Paragraph.ALIGN_CENTER);
        para1.setSpacingAfter(15);
        document.add(para1);
        //document.add(paragraphe("************************************"));
    }

    public static void TitrePartie(Document document, String chaine) throws DocumentException, BadElementException {
        Paragraph para1 = new Paragraph(chaine, catFont_titre);
        para1.setAlignment(Paragraph.ALIGN_LEFT);
        para1.setSpacingAfter(15);
        document.add(para1);
        //document.add(paragraphe("************************************"));
    }

    public static PdfPCell MakeCell(String chaine) {
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(chaine, smallBold));
        cell.setPadding(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
//        cell.setFixedHeight(35f);

        return cell;
    }

    public static PdfPCell MakeCellRequi(String chaine) {
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(chaine, smallBoldseparateur));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public static PdfPCell MakeCellL(String chaine) {
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(chaine, smallBoldseparateur));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public static PdfPCell MakeCellU(String chaine) {
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(chaine, smallBoldUnite));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
}
