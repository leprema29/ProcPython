import os
import csv
from reportlab.lib import colors
from reportlab.lib.pagesizes import A4, landscape
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.lib.units import cm, mm
from reportlab.platypus import SimpleDocTemplate, Table, TableStyle, Paragraph, Spacer, Image
from reportlab.lib.enums import TA_CENTER, TA_LEFT
from app.metier.variables import Variables


class PDFGenerator:
    """Generates PDF requisition reports for all operators"""

    def __init__(self, phone, date_requisition, demandeur_requisition="", operator=""):
        self.phone = phone
        self.date_requisition = date_requisition
        self.demandeur_requisition = demandeur_requisition
        self.operator = operator
        self.base_dir = os.path.join(Variables.DESTINATION_DOSSIERS, date_requisition, phone)
        self.styles = getSampleStyleSheet()

    def _read_csv(self, file_path):
        rows = []
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                reader = csv.reader(f)
                for row in reader:
                    rows.append(row)
        except Exception as e:
            print(f"Error reading CSV {file_path}: {e}")
        return rows

    def _create_header(self):
        """Create document header with logo and title"""
        elements = []
        # Try to add logo
        logo_path = os.path.join(Variables.get_destination_logo(), "antic_logo.png")
        if os.path.exists(logo_path):
            try:
                img = Image(logo_path, width=3*cm, height=3*cm)
                elements.append(img)
            except Exception:
                pass

        # Title
        title_style = self.styles['Title']
        title_style.alignment = TA_CENTER

        header_text = "TRACKING SECURITY SYSTEM"
        if self.demandeur_requisition:
            header_text = f"{self.demandeur_requisition} - {header_text}"

        elements.append(Paragraph(header_text, title_style))
        elements.append(Spacer(1, 5*mm))

        # Subtitle with operator and phone
        subtitle = f"Requisition - {self.operator} - {self.phone}"
        subtitle_style = self.styles['Heading2']
        subtitle_style.alignment = TA_CENTER
        elements.append(Paragraph(subtitle, subtitle_style))
        elements.append(Spacer(1, 10*mm))

        return elements

    def _create_table(self, title, data, col_widths=None):
        """Create a formatted table section"""
        elements = []
        # Section title
        section_style = self.styles['Heading3']
        elements.append(Paragraph(title, section_style))
        elements.append(Spacer(1, 3*mm))

        if not data or len(data) < 1:
            return elements

        table = Table(data, colWidths=col_widths, repeatRows=1)
        style = TableStyle([
            ('BACKGROUND', (0, 0), (-1, 0), colors.HexColor('#4472C4')),
            ('TEXTCOLOR', (0, 0), (-1, 0), colors.white),
            ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
            ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
            ('FONTSIZE', (0, 0), (-1, 0), 8),
            ('FONTSIZE', (0, 1), (-1, -1), 7),
            ('BOTTOMPADDING', (0, 0), (-1, 0), 6),
            ('TOPPADDING', (0, 0), (-1, 0), 6),
            ('GRID', (0, 0), (-1, -1), 0.5, colors.black),
            ('ROWBACKGROUNDS', (0, 1), (-1, -1), [colors.white, colors.HexColor('#D9E2F3')]),
        ])
        table.setStyle(style)
        elements.append(table)
        elements.append(Spacer(1, 8*mm))

        return elements

    def generate(self):
        """Generate the PDF report"""
        output_path = os.path.join(self.base_dir, f"Requisition_{self.phone}.pdf")
        os.makedirs(self.base_dir, exist_ok=True)

        doc = SimpleDocTemplate(
            output_path,
            pagesize=landscape(A4),
            rightMargin=1*cm, leftMargin=1*cm,
            topMargin=1*cm, bottomMargin=1*cm
        )

        elements = self._create_header()
        files = os.listdir(self.base_dir) if os.path.exists(self.base_dir) else []

        for f in sorted(files):
            if not f.endswith('.csv'):
                continue

            csv_path = os.path.join(self.base_dir, f)
            data = self._read_csv(csv_path)
            if not data:
                continue

            # Determine section title from filename
            if "IdNumero" in f or "Identification_Numero" in f:
                elements.extend(self._create_table("Identification du Numero", data))
            elif "Listing_SMS" in f:
                elements.extend(self._create_table("Listing SMS", data))
            elif "Listing_Emis" in f or "Listing_" in f:
                elements.extend(self._create_table("Listing des Appels", data))
            elif "IdentificationAbonnees" in f:
                elements.extend(self._create_table("Identification des Abonnes", data))
            elif "Statistiques_Localisation" in f:
                elements.extend(self._create_table("Statistique par Localisation", data))
            elif "Statistiques_" in f and "Localisation" not in f:
                elements.extend(self._create_table("Statistique des Appels", data))
            elif "frequenceCellule" in f:
                elements.extend(self._create_table("Frequence par Cellule", data))
            elif "frequenceCorrespondance" in f:
                elements.extend(self._create_table("Frequence par Correspondant", data))
            elif "frequenceDureeAppel" in f:
                elements.extend(self._create_table("Frequence par Duree d'Appel", data))
            elif "frequenceImei" in f:
                elements.extend(self._create_table("Frequence par IMEI", data))
            elif "sharedImei" in f:
                elements.extend(self._create_table("IMEI Partage", data))

        try:
            doc.build(elements)
            print(f"PDF generated: {output_path}")
        except Exception as e:
            print(f"Error generating PDF: {e}")
            import traceback
            traceback.print_exc()
