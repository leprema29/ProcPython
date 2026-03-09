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
import com.itextpdf.text.Chunk;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PrintNEXTTELMultiple {

//    String LOGO_PATH = "image" + System.getProperty("file.separator") + "Untitled.png";
    String LOGO_PATH = "C:\\Users\\cinfopres\\Documents\\NetBeansProjects\\RequisitionMONC\\src\\print\\entete.jpeg";
    public static String id_number_Mtn[] = new String[5];
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
            PdfWriter.getInstance(document, new FileOutputStream("requisition_" + number + ".pdf"));

            document.open();

            TitreDocument(document, "RÉQUISITION RELATIVE AUX IDENTITÉS DES ABONNÉS DE TÉLÉPHONIE MOBILE");

        } catch (FileNotFoundException | DocumentException e) {
        }
        return document;
    }

    public Document PrintIDAbonneNEXTTEL(String number, Document document, String[] data) throws BadElementException, IOException {

        try {

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
            table.addCell(MakeCellValueTableau(data[2]));

            c1 = new PdfPCell(new Phrase("Opérateur téléphonique :", smallBold));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c1);
            table.addCell(MakeCellValueTableau("NEXTTEL"));
            c2 = new PdfPCell(new Phrase("Numéro CNI :", smallBold));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c2);
            table.addCell(MakeCellValueTableau(data[4]));

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
            System.out.println("idabonné bon ");
        } catch (DocumentException e) {
        }
        return document;
    }

    public Document PrintHeaderListingNEXTTEL(Document document, String[] data) throws BadElementException, IOException {

        try {
            TitrePartie(document, "II. Listing  ");

            PdfPTable table1 = new PdfPTable(6);
            table1.setWidthPercentage(100);
//             table.setSpacingBefore(5f);
//            float[] columnWidths = {1.2f, 1.5f, 1f, 1.5f};
//            table1.setWidths(columnWidths);

        } catch (DocumentException e) {
        }
        return document;
    }

    public PdfPTable PrintAddListingNEXTTEL(Document document, String[] data, PdfPTable table1) throws BadElementException, IOException {

        PdfPCell c2 = new PdfPCell();
        table1.addCell(MakeCellRequi(data[3]));
        table1.addCell(MakeCellRequi(data[6]));
        table1.addCell(MakeCellRequi(data[5]));
        table1.addCell(MakeCellRequi(data[1]));
        table1.addCell(MakeCellRequi(data[2]));
        table1.addCell(MakeCellRequi(data[7]));
        return table1;
    }

    public PdfPTable PrintAddIdAbonneNEXTTEL(Document document, String[] data, PdfPTable table1) throws BadElementException, IOException {
//IdAbonne[0], IdAbonne[1] + " " +  IdAbonne[2], IdAbonne[3], IdAbonne[4], IdAbonne[5]
        PdfPCell c2 = new PdfPCell();
        table1.addCell(MakeCellRequi(data[0]));
        table1.addCell(MakeCellRequi(data[1]));
        table1.addCell(MakeCellRequi(data[2]));
        table1.addCell(MakeCellRequi(data[3]));
        table1.addCell(MakeCellRequi(data[4]));

        return table1;
    }

    public PdfPTable PrintAddFreqCelluleNEXTTEL(Document document, String[] data, PdfPTable table1, int i) throws BadElementException, IOException {

        PdfPCell c2 = new PdfPCell();
//        for (int j = 6; j <= 17; j++) {
//            if (data[j].equals("-1")) {
//                data[j] = "";
//            }
//        }
        table1.addCell(MakeCellRequi(String.valueOf(data[0])));
        table1.addCell(MakeCellRequi(data[1]));
        table1.addCell(MakeCellRequi(data[2].replace("-1", "")));
        table1.addCell(MakeCellRequi(data[3].replace("-1", "")));
        table1.addCell(MakeCellRequi(data[4].replace("-1", "")));
        table1.addCell(MakeCellRequi(data[5].replace("-1", "")));
        table1.addCell(MakeCellRequi(data[6].replace("-1", "")));
        table1.addCell(MakeCellRequi(data[7].replace("-1", "")));
        table1.addCell(MakeCellRequi(data[8].replace("-1", "")));
        table1.addCell(MakeCellRequi(data[9].replace("-1", "")));
        table1.addCell(MakeCellRequi(data[10].replace("-1", "")));
        table1.addCell(MakeCellRequi(data[11].replace("-1", "")));
        table1.addCell(MakeCellRequi(data[12].replace("-1", "")));
        table1.addCell(MakeCellRequi(data[13].replace("-1", "")));
        
        return table1;
    }

    public PdfPTable PrintAddFreqCorrespondantNEXTTEL(Document document, String[] data, PdfPTable table1, int i) throws BadElementException, IOException {

        PdfPCell c2 = new PdfPCell();
//        for (int j = 0; j <= 20; j++) {
//            if (data[j].equals("null")) {
//                data[j] = "";
//            }
//        }
        table1.addCell(MakeCellRequi(String.valueOf(data[0])));
        table1.addCell(MakeCellRequi(data[1].replace("null", "")));
        table1.addCell(MakeCellRequi(data[2].replace("null", "")));
        table1.addCell(MakeCellRequi(data[3].replace("null", "")));
        table1.addCell(MakeCellRequi(data[4].replace("null", "")));
        table1.addCell(MakeCellRequi(data[9].replace("null", "")));
        table1.addCell(MakeCellRequi(data[10].replace("null", "")));
        table1.addCell(MakeCellRequi(data[11].replace("null", "")));
        table1.addCell(MakeCellRequi(data[12].replace("null", "")));
        table1.addCell(MakeCellRequi(data[13].replace("null", "")));
        table1.addCell(MakeCellRequi(data[14].replace("null", "")));
        table1.addCell(MakeCellRequi(data[15].replace("null", "")));
        table1.addCell(MakeCellRequi(data[16].replace("null", "")));
        table1.addCell(MakeCellRequi(data[17].replace("null", "")));
        table1.addCell(MakeCellRequi(data[18].replace("null", "")));
        table1.addCell(MakeCellRequi(data[19].replace("null", "")));
        table1.addCell(MakeCellRequi(data[20].replace("null", "")));

        return table1;
    }
    
    public PdfPTable PrintAddFreqDureeAppelNEXTTEL(Document document, String[] data, PdfPTable table1, int i) throws BadElementException, IOException {

        PdfPCell c2 = new PdfPCell();
        table1.addCell(MakeCellRequi(String.valueOf(data[0])));
        table1.addCell(MakeCellRequi(data[1]));
        table1.addCell(MakeCellRequi(data[6]));
        table1.addCell(MakeCellRequi(data[7]));

        return table1;
    }
    
    public PdfPTable PrintAddFreqImeiNEXTTEL(Document document, String[] data, PdfPTable table1, int i) throws BadElementException, IOException {

        PdfPCell c2 = new PdfPCell();
        table1.addCell(MakeCellRequi(String.valueOf(data[0])));
        table1.addCell(MakeCellRequi(data[1]));
        table1.addCell(MakeCellRequi(data[2]));
        table1.addCell(MakeCellRequi(data[3]));

        return table1;
    }
    
    //    just for listing imei
    public PdfPTable PrintAddImeiPartageNexttel(Document document, String[] data, PdfPTable table1) throws BadElementException, IOException {

        String identite = data[2] + "(CNI: " + data[4] + " Naissance: " + data[3] + " Adresse: " + data[6] + ")";
        if (identite.contains("null")) {
            identite = "inconnu";
        }
        PdfPCell c2 = new PdfPCell();
        table1.addCell(MakeCellRequi(data[0]));
        table1.addCell(MakeCellRequi(data[1]));
        table1.addCell(MakeCellRequi(identite));
        table1.addCell(MakeCellRequi(data[7]));
        table1.addCell(MakeCellRequi(data[8]));
        table1.addCell(MakeCellRequi(data[9]));
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

    public static Document Charger_enteteNEXTTEL(Document document) throws DocumentException, BadElementException, IOException {

        PdfPTable table = new PdfPTable(3);
        table.setSpacingBefore(0);
        table.getDefaultCell().setPadding(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(0f);
        table.setSpacingAfter(20);
        float[] columnWidths = {2.2f, 0.6f, 2.2f};
        table.setWidths(columnWidths);

        PdfPCell cell = new PdfPCell();

        table.addCell(MakeCellL(""));

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        Image img = Image.getInstance("antic.png");
        cell.addElement(img);
//       cell.setPadding(5);
        cell.setRowspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        table.addCell(MakeCellL(""));

        table.addCell(MakeCell("RÉPUBLIQUE DU CAMEROUN"));

        table.addCell(MakeCell("REPUBLIC OF CAMEROON"));

        table.addCell(MakeCellL("Paix – Travail – Patrie"));
//        table.addCell(MakeCellU("logo"));
        table.addCell(MakeCellL("Peace – Work – Fatherland"));

//        table.addCell(MakeCellL("AGENCE NATIONALE DES TECHNOLOGIES DE L’INFORMATION ET DE LA COMMUNICATION"));
////        table.addCell(MakeCellL(""));
//        table.addCell(MakeCellL("NATIONAL AGENCY NATIONALE OF INFORMATION AND COMMUNICATION TECHNOLOGIES"));

        document.add(table);
        document.add(Chunk.NEWLINE);
        return document;
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
