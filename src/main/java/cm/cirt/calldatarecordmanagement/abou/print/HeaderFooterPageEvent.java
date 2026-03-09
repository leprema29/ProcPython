/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.abou.print;

import cm.cirt.calldatarecordmanagement.metier.Variables111;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;

/**
 *
 * @author Harry Wanki
 */
public class HeaderFooterPageEvent extends PdfPageEventHelper {

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
//        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Top Left"), 30, 800, 0);
//        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Top Right"), 550, 800, 0);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        Image image;
        try {
            image = Image.getInstance(Variables111.DESTINATION_DOSSIERS + "fichier/antic.jpg");
            image.setAlignment(Element.ALIGN_RIGHT);
            image.setAbsolutePosition(40, 30);
            image.scalePercent(8f, 8f);
            writer.getDirectContent().addImage(image, true);
        } catch (IOException | DocumentException e) {
        }
        Font font = new Font(FontFamily.TIMES_ROMAN, 5, Font.NORMAL);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Agence Nationale des Technologies de l'information et de la Communication", font), 130, 30, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("P. " + document.getPageNumber(), font), 550, 30, 0);
    }
}
