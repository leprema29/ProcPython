import os
import csv
from reportlab.lib.pagesizes import A4
from reportlab.lib.units import mm
from reportlab.lib.enums import TA_CENTER, TA_LEFT
from reportlab.platypus import SimpleDocTemplate, Table, TableStyle, Paragraph, Spacer, Image
from reportlab.lib.styles import ParagraphStyle
from reportlab.lib import colors
from app.metier.variables import Variables


# Font styles matching Java iText fonts
FONT_TITRE = ParagraphStyle('titre', fontName='Times-Bold', fontSize=11, alignment=TA_CENTER, spaceAfter=15)
FONT_TITRE_DATE = ParagraphStyle('titre_date', fontName='Times-Bold', fontSize=12, alignment=TA_CENTER, spaceAfter=2)
FONT_TITRE_DEMANDEUR = ParagraphStyle('titre_demandeur', fontName='Times-Bold', fontSize=12, alignment=TA_CENTER, spaceAfter=3)
FONT_TITRE_PARTIE = ParagraphStyle('titre_partie', fontName='Times-Bold', fontSize=10, alignment=TA_LEFT, spaceAfter=15)


class HeaderFooterPageEvent:
    def __init__(self):
        self.logo_path = self._find_logo()

    def _find_logo(self):
        paths = [
            os.path.join(Variables.DESTINATION_DOSSIERS, "fichier", "antic.jpg"),
            os.path.join(Variables.DESTINATION_DOSSIERS, "fichier", "LogoAntic.jpg"),
            os.path.join(os.path.dirname(__file__), '..', '..', 'static', 'fichier', 'antic.jpg'),
        ]
        for p in paths:
            if os.path.exists(p):
                return p
        return None

    def __call__(self, canvas, doc):
        canvas.saveState()
        if self.logo_path:
            try:
                canvas.drawImage(self.logo_path, 40, 30, width=40, height=25,
                                 preserveAspectRatio=True, mask='auto')
            except Exception:
                pass
        canvas.setFont('Times-Roman', 5)
        canvas.drawCentredString(
            130, 30,
            "Agence Nationale des Technologies de l'information et de la Communication")
        canvas.drawRightString(550, 30, f"P. {canvas.getPageNumber()}")
        canvas.restoreState()


def _read_csv(file_path):
    rows = []
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            reader = csv.reader(f)
            for row in reader:
                rows.append(row)
    except Exception as e:
        print(f"Error reading CSV {file_path}: {e}")
    return rows


def _make_identity_label(text):
    return Paragraph(
        f'<font name="Times-Bold" size="8"><i>{text}</i></font>',
        ParagraphStyle('lbl', alignment=TA_LEFT))


def _make_identity_value(text):
    return Paragraph(
        f'<font name="Times-Roman" size="8">{text}</font>',
        ParagraphStyle('val', alignment=TA_LEFT))


def _build_header_table():
    elements = []
    logo_path = None
    paths = [
        os.path.join(Variables.DESTINATION_DOSSIERS, "fichier", "LogoAntic.jpg"),
        os.path.join(Variables.DESTINATION_DOSSIERS, "fichier", "antic.jpg"),
        os.path.join(os.path.dirname(__file__), '..', '..', 'static', 'fichier', 'antic.jpg'),
    ]
    for p in paths:
        if os.path.exists(p):
            logo_path = p
            break

    style_center = ParagraphStyle('c', fontName='Times-Bold', fontSize=5, alignment=TA_CENTER)
    style_center_bold = ParagraphStyle('cb', fontName='Times-Bold', fontSize=8, alignment=TA_CENTER)

    row1_data = [
        Paragraph("", style_center),
        "",
        Paragraph("", style_center),
    ]
    row2_data = [
        Paragraph("RÉPUBLIQUE DU CAMEROUN", style_center_bold),
        "",
        Paragraph("REPUBLIC OF CAMEROON", style_center_bold),
    ]
    row3_data = [
        Paragraph("Paix – Travail – Patrie", style_center),
        "",
        Paragraph("Peace – Work – Fatherland", style_center),
    ]

    if logo_path:
        try:
            img = Image(logo_path)
            img.drawWidth = 60
            img.drawHeight = 40
            row1_data[1] = img
        except Exception:
            pass

    table = Table([row1_data, row2_data, row3_data],
                  colWidths=[220, 60, 220])
    table.setStyle(TableStyle([
        ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
        ('VALIGN', (0, 0), (-1, -1), 'MIDDLE'),
        ('SPAN', (1, 0), (1, 2)),
        ('GRID', (0, 0), (-1, -1), 0, colors.white),
    ]))
    elements.append(table)
    elements.append(Spacer(1, 10 * mm))
    return elements


def _build_identity_table(phone, data_row, operator_name):
    table_data = [
        [_make_identity_label("Téléphone :"), _make_identity_value(phone),
         _make_identity_label("Nom et Prénom :"), _make_identity_value(data_row.get('nom', ''))],
        [_make_identity_label("Opérateur téléphonique :"), _make_identity_value(operator_name),
         _make_identity_label("Numéro CNI :"), _make_identity_value(data_row.get('cni', ''))],
        [_make_identity_label("IMEI :"), _make_identity_value(""),
         _make_identity_label("Adresse :"), _make_identity_value(data_row.get('adresse', ''))],
    ]
    table = Table(table_data, colWidths=[120, 120, 120, 120])
    table.setStyle(TableStyle([
        ('GRID', (0, 0), (-1, -1), 0.5, colors.black),
        ('VALIGN', (0, 0), (-1, -1), 'MIDDLE'),
    ]))
    return table


def _build_data_table(headers, rows):
    n_cols = len(headers)
    header_row = []
    for h in headers:
        header_row.append(Paragraph(
            f'<font name="Times-Bold" size="8"><i>{h}</i></font>',
            ParagraphStyle('h', alignment=TA_LEFT)))
    all_data = [header_row]
    for row in rows:
        data_row = []
        for i in range(n_cols):
            val = row[i] if i < len(row) else ""
            data_row.append(val)
        all_data.append(data_row)

    page_width = A4[0] - 2 * 28.35
    col_width = page_width / n_cols
    table = Table(all_data, colWidths=[col_width] * n_cols, repeatRows=1)
    table.setStyle(TableStyle([
        ('FONTNAME', (0, 0), (-1, 0), 'Times-Bold'),
        ('FONTSIZE', (0, 0), (-1, 0), 8),
        ('FONTNAME', (0, 1), (-1, -1), 'Times-Bold'),
        ('FONTSIZE', (0, 1), (-1, -1), 5),
        ('ALIGN', (0, 1), (-1, -1), 'CENTER'),
        ('GRID', (0, 0), (-1, -1), 0.5, colors.black),
        ('VALIGN', (0, 0), (-1, -1), 'MIDDLE'),
    ]))
    return table


def _parse_id_csv(rows, num_fields=6):
    if len(rows) <= 1:
        return None
    data = rows[1]
    result = {
        'numero': data[0] if len(data) > 0 else '',
        'nom': data[1] if len(data) > 1 else '',
        'date_naissance': data[2] if len(data) > 2 else '',
        'cni': data[3] if len(data) > 3 else '',
        'date_exp_cni': data[4] if len(data) > 4 else '',
        'adresse': data[5] if len(data) > 5 else '',
    }
    if num_fields >= 7:
        result['nationalite'] = data[6] if len(data) > 6 else ''
    return result


def _roman(n):
    vals = [(10, 'X'), (9, 'IX'), (5, 'V'), (4, 'IV'), (1, 'I')]
    result = ''
    for val, numeral in vals:
        while n >= val:
            result += numeral
            n -= val
    return result


class PDFGenerator:
    def __init__(self, phone, date_requisition, demandeur_requisition="", operator="",
                 date_debut="", date_fin=""):
        self.phone = phone
        self.date_requisition = date_requisition
        self.demandeur_requisition = demandeur_requisition
        self.operator = operator
        self.date_debut = date_debut
        self.date_fin = date_fin
        self.base_dir = os.path.join(Variables.DESTINATION_DOSSIERS, date_requisition, phone)

    def _csv_path(self, pattern):
        return os.path.join(self.base_dir, pattern)

    def _convert_date(self, d):
        if not d or len(d) < 8:
            return d
        try:
            parts = d.split('-')
            if len(parts) == 3:
                return f"{parts[2]}/{parts[1]}/{parts[0]}"
        except Exception:
            pass
        return d

    def generate(self):
        output_dir = os.path.join(Variables.DESTINATION_DOSSIERS, self.date_requisition, self.phone)
        os.makedirs(output_dir, exist_ok=True)
        output_path = os.path.join(output_dir, f"Requisition_{self.phone}.pdf")

        footer = HeaderFooterPageEvent()
        doc = SimpleDocTemplate(
            output_path,
            pagesize=A4,
            rightMargin=28.35, leftMargin=28.35,
            topMargin=28.35, bottomMargin=42.5
        )

        elements = []

        # Header with logo (matching Java Charger_enteteMTN)
        elements.extend(_build_header_table())

        # Main title
        elements.append(Paragraph(
            "RÉQUISITION RELATIVE AUX IDENTITÉS DES ABONNÉS DE TÉLÉPHONIE MOBILE",
            FONT_TITRE))

        # Date period
        if self.date_debut and self.date_fin:
            elements.append(Paragraph(
                f"Période : du {self._convert_date(self.date_debut)} Au {self._convert_date(self.date_fin)}",
                FONT_TITRE_DATE))

        # Demandeur
        if self.demandeur_requisition:
            elements.append(Paragraph(
                f"Service Demandeur: {self.demandeur_requisition}", FONT_TITRE_DEMANDEUR))

        elements.append(Spacer(1, 5 * mm))

        op = (self.operator or "").lower()
        if op == "orange":
            self._generate_orange(elements)
        elif op == "mtn":
            self._generate_mtn(elements)
        elif op == "nexttel":
            self._generate_nexttel(elements)
        elif op == "momo":
            self._generate_mtn(elements)
        else:
            self._generate_generic(elements)

        try:
            doc.build(elements, onFirstPage=footer, onLaterPages=footer)
            print(f"PDF generated: {output_path}")
        except Exception as e:
            print(f"Error generating PDF: {e}")
            import traceback
            traceback.print_exc()

    def _generate_orange(self, elements):
        section_num = 0

        # I. Identité de l'abonné
        id_csv = self._csv_path(f"Requisition_IdNumero_{self.phone}.csv")
        id_rows = _read_csv(id_csv)
        id_data = _parse_id_csv(id_rows, 6)

        if id_data:
            section_num += 1
            elements.append(Paragraph(
                f"{_roman(section_num)}.  Identité de l'abonné ", FONT_TITRE_PARTIE))
            elements.append(_build_identity_table(self.phone, id_data, "ORANGE"))
            elements.append(Spacer(1, 5 * mm))

        # II. Listing Appels
        listing_csv = self._csv_path(f"Requisition_Listing_Emis_{self.phone}.csv")
        listing_rows = _read_csv(listing_csv)
        if listing_rows and len(listing_rows) > 1:
            section_num += 1
            headers = ["Numéro appellant", "Localisation numéro appelant", "IMEI",
                       "Date début appel", "Durée de l'appel", "Numéro appelé"]
            elements.append(Paragraph(
                f"{_roman(section_num)}. Listing Appels ", FONT_TITRE_PARTIE))
            elements.append(_build_data_table(headers, listing_rows[1:]))
            elements.append(Spacer(1, 5 * mm))

        # III. Listing SMS
        sms_csv = self._csv_path(f"Requisition_Listing_SMS_{self.phone}.csv")
        sms_rows = _read_csv(sms_csv)
        if sms_rows and len(sms_rows) > 1:
            section_num += 1
            headers = ["Numéro d'envoi", "Localisation numéro récepteur",
                       "IMEI Numéro récepteur", "Date sms", "Numéro récepteur"]
            elements.append(Paragraph(
                f"{_roman(section_num)}. Listing SMS", FONT_TITRE_PARTIE))
            elements.append(_build_data_table(headers, sms_rows[1:]))
            elements.append(Spacer(1, 5 * mm))

        # Identification des numeros
        ident_csv = self._csv_path(f"IdentificationAbonnees_{self.phone}.csv")
        ident_rows = _read_csv(ident_csv)
        if ident_rows and len(ident_rows) > 1 and id_data:
            section_num += 1
            headers = ["Numéro", "Nom & Prénom", "Date de naissance",
                       "Numéro CNI", "Date expiration CNI", "Adresse"]
            elements.append(Paragraph(
                f"{_roman(section_num)}. Identification des numeros  ", FONT_TITRE_PARTIE))
            elements.append(_build_data_table(headers, ident_rows[1:]))
            elements.append(Spacer(1, 5 * mm))

        # Statistiques
        self._add_statistics(elements, section_num)

    def _generate_mtn(self, elements):
        section_num = 0

        # I. Identité de l'abonné
        id_csv = self._csv_path(f"Requisition_IdNumero_{self.phone}.csv")
        id_rows = _read_csv(id_csv)
        id_data = _parse_id_csv(id_rows, 7)

        if id_data:
            section_num += 1
            elements.append(Paragraph(
                f"{_roman(section_num)}.  Identité de l'abonné ", FONT_TITRE_PARTIE))
            elements.append(_build_identity_table(self.phone, id_data, "MTN"))
            elements.append(Spacer(1, 5 * mm))

        # II. Listing
        listing_csv = self._csv_path(f"Requisition_Listing_{self.phone}.csv")
        if not os.path.exists(listing_csv):
            listing_csv = self._csv_path(f"Requisition_Listing_Emis_{self.phone}.csv")
        listing_rows = _read_csv(listing_csv)
        if listing_rows and len(listing_rows) > 1:
            section_num += 1
            headers = ["Numéro appellant", "Localisation numéro appelant", "IMEI",
                       "Date début appel", "Durée de l'appel", "Numéro appelé"]
            elements.append(Paragraph(
                f"{_roman(section_num)}. Listing Appels ", FONT_TITRE_PARTIE))
            elements.append(_build_data_table(headers, listing_rows[1:]))
            elements.append(Spacer(1, 5 * mm))

        # Identification des numeros
        ident_csv = self._csv_path(f"IdentificationAbonnees_{self.phone}.csv")
        ident_rows = _read_csv(ident_csv)
        if ident_rows and len(ident_rows) > 1 and id_data:
            section_num += 1
            headers = ["Numéro", "Nom & Prénom", "Date de naissance",
                       "Numéro CNI", "Date expiration CNI", "Adresse", "Nationalité"]
            elements.append(Paragraph(
                f"{_roman(section_num)}. Identification des numeros  ", FONT_TITRE_PARTIE))
            elements.append(_build_data_table(headers, ident_rows[1:]))
            elements.append(Spacer(1, 5 * mm))

        self._add_statistics(elements, section_num)

    def _generate_nexttel(self, elements):
        section_num = 0

        # I. Identité de l'abonné
        id_csv = self._csv_path(f"Requisition_IdNumero_{self.phone}.csv")
        id_rows = _read_csv(id_csv)
        id_data = _parse_id_csv(id_rows, 5)

        if id_data:
            section_num += 1
            elements.append(Paragraph(
                f"{_roman(section_num)}.  Identité de l'abonné ", FONT_TITRE_PARTIE))
            elements.append(_build_identity_table(self.phone, id_data, "NEXTTEL"))
            elements.append(Spacer(1, 5 * mm))

        # II. Listing
        listing_csv = self._csv_path(f"Requisition_Listing_{self.phone}.csv")
        if not os.path.exists(listing_csv):
            listing_csv = self._csv_path(f"Requisition_Listing_Emis_{self.phone}.csv")
        listing_rows = _read_csv(listing_csv)
        if listing_rows and len(listing_rows) > 1:
            section_num += 1
            headers = ["Numéro appellant", "Localisation numéro appelant", "IMEI",
                       "Date début appel", "Durée de l'appel", "Numéro appelé"]
            elements.append(Paragraph(
                f"{_roman(section_num)}. Listing Appels ", FONT_TITRE_PARTIE))
            elements.append(_build_data_table(headers, listing_rows[1:]))
            elements.append(Spacer(1, 5 * mm))

        # Identification des numeros
        ident_csv = self._csv_path(f"IdentificationAbonnees_{self.phone}.csv")
        ident_rows = _read_csv(ident_csv)
        if ident_rows and len(ident_rows) > 1 and id_data:
            section_num += 1
            headers = ["Numéro", "Nom & Prénom", "Date de naissance",
                       "Numéro CNI", "Date expiration CNI"]
            elements.append(Paragraph(
                f"{_roman(section_num)}. Identification des numeros  ", FONT_TITRE_PARTIE))
            elements.append(_build_data_table(headers, ident_rows[1:]))
            elements.append(Spacer(1, 5 * mm))

        self._add_statistics(elements, section_num)

    def _add_statistics(self, elements, section_num):
        # Statistiques frequence numeros
        stats_csv = self._csv_path(f"Statistiques_{self.phone}.csv")
        stats_rows = _read_csv(stats_csv)
        if stats_rows and len(stats_rows) > 1:
            section_num += 1
            elements.append(Paragraph(
                f"{_roman(section_num)}. Statistiques", FONT_TITRE_PARTIE))
            elements.append(Paragraph(
                f"{_roman(section_num)}.1 Statistique sur la frequence des numeros",
                FONT_TITRE_PARTIE))
            headers = ["N°", "Numero de telephone", "Occurence",
                       "Duree totale de communications"]
            numbered_rows = []
            for i, row in enumerate(stats_rows[1:]):
                numbered_rows.append([str(i + 1)] + row)
            elements.append(_build_data_table(headers, numbered_rows))
            elements.append(Spacer(1, 5 * mm))

            # Statistiques Localisation
            stats_loc_csv = self._csv_path(f"Statistiques_Localisation_{self.phone}.csv")
            stats_loc_rows = _read_csv(stats_loc_csv)
            if stats_loc_rows and len(stats_loc_rows) > 1:
                elements.append(Paragraph(
                    f"{_roman(section_num)}.2 Statistique sur la frequence des Lieux d'appels",
                    FONT_TITRE_PARTIE))
                headers = ["N", "Localisation du Numero", "Occurence"]
                numbered_rows = []
                for i, row in enumerate(stats_loc_rows[1:]):
                    numbered_rows.append([str(i + 1)] + row)
                elements.append(_build_data_table(headers, numbered_rows))
                elements.append(Spacer(1, 5 * mm))

        # Frequency sections (for multiple requisitions)
        self._add_freq_sections(elements, section_num)

    def _add_freq_sections(self, elements, section_num):
        freq_cell_csv = self._csv_path(f"frequenceCellule_{self.phone}.csv")
        freq_cell_rows = _read_csv(freq_cell_csv)
        if freq_cell_rows and len(freq_cell_rows) > 1:
            section_num += 1
            elements.append(Paragraph(
                f"{_roman(section_num)}. Fréquence par cellule", FONT_TITRE_PARTIE))
            headers = ["Total", "Cellule", "Region", "Latitude", "Longitude",
                       "0h-2h", "2h-4h", "4h-6h", "6h-8h",
                       "8h-10h", "10h-12h", "12h-14h", "14h-16h", "16h-18h",
                       "18h-20h", "20h-22h", "22h-24h"]
            elements.append(_build_data_table(headers, freq_cell_rows[1:]))
            elements.append(Spacer(1, 5 * mm))

        freq_cor_csv = self._csv_path(f"frequenceCorrespondance_{self.phone}.csv")
        freq_cor_rows = _read_csv(freq_cor_csv)
        if freq_cor_rows and len(freq_cor_rows) > 1:
            section_num += 1
            elements.append(Paragraph(
                f"{_roman(section_num)}. Fréquence par correspondant", FONT_TITRE_PARTIE))
            headers = ["Total", "Entrant", "Sortant", "Téléphone", "Identité",
                       "Date Naiss.", "CNI", "Date Exp.", "Quartier",
                       "0h-2h", "2h-4h", "4h-6h", "6h-8h", "8h-10h",
                       "10h-12h", "12h-14h", "14h-16h", "16h-18h", "18h-20h",
                       "20h-22h", "22h-24h"]
            elements.append(_build_data_table(headers, freq_cor_rows[1:]))
            elements.append(Spacer(1, 5 * mm))

        freq_duree_csv = self._csv_path(f"frequenceDureeAppel_{self.phone}.csv")
        freq_duree_rows = _read_csv(freq_duree_csv)
        if freq_duree_rows and len(freq_duree_rows) > 1:
            section_num += 1
            elements.append(Paragraph(
                f"{_roman(section_num)}. Fréquence par durée d'appel", FONT_TITRE_PARTIE))
            headers = ["Numero", "Identité", "Date Naiss.", "CNI",
                       "Date Exp.", "Quartier", "Durée Appel", "Nb Messages"]
            elements.append(_build_data_table(headers, freq_duree_rows[1:]))
            elements.append(Spacer(1, 5 * mm))

        freq_imei_csv = self._csv_path(f"frequenceImei_{self.phone}.csv")
        freq_imei_rows = _read_csv(freq_imei_csv)
        if freq_imei_rows and len(freq_imei_rows) > 1:
            section_num += 1
            elements.append(Paragraph(
                f"{_roman(section_num)}. Fréquence par IMEI", FONT_TITRE_PARTIE))
            headers = ["N°", "IMEI", "Occurence", "Pourcentage"]
            numbered_rows = []
            for i, row in enumerate(freq_imei_rows[1:]):
                numbered_rows.append([str(i + 1)] + row)
            elements.append(_build_data_table(headers, numbered_rows))
            elements.append(Spacer(1, 5 * mm))

        shared_imei_csv = self._csv_path(f"sharedImei_{self.phone}.csv")
        shared_imei_rows = _read_csv(shared_imei_csv)
        if shared_imei_rows and len(shared_imei_rows) > 1:
            section_num += 1
            elements.append(Paragraph(
                f"{_roman(section_num)}. Imei partagé: différent(s) utilisateur(s) du téléphone",
                FONT_TITRE_PARTIE))
            headers = ["Numéro", "Nom & Prénom", "Date de naissance",
                       "Numéro CNI", "Date expiration CNI", "Adresse"]
            elements.append(_build_data_table(headers, shared_imei_rows[1:]))
            elements.append(Spacer(1, 5 * mm))

    def _generate_generic(self, elements):
        if not os.path.exists(self.base_dir):
            return
        files = sorted(os.listdir(self.base_dir))
        for f in files:
            if not f.endswith('.csv'):
                continue
            csv_path = os.path.join(self.base_dir, f)
            rows = _read_csv(csv_path)
            if not rows or len(rows) < 2:
                continue
            title = os.path.splitext(f)[0].replace('_', ' ')
            elements.append(Paragraph(title, FONT_TITRE_PARTIE))
            elements.append(_build_data_table(rows[0], rows[1:]))
            elements.append(Spacer(1, 5 * mm))
