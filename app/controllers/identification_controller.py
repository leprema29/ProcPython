from datetime import datetime
from flask import Blueprint, render_template, request, session
from flask_login import login_required
from app.beans.identification_bean import IdentificationBean
from app.abou.ssh_utils import execute_remote_command
from app.metier.variables import Variables

identification_bp = Blueprint('identification', __name__)

MTN_BDI = "/home/data/mtn/backupCSV/Identification_database/bdi.txt"
OCM_BDI = "/home/data/orange/backupCSV/BDI/bdi.csv"
NEXTTEL_BDI = "/home/data/nexttel/nexttel_cdr_data/processed/identification/bdi.csv"


def get_operator_by_telephone(tel):
    if any(tel.startswith(p) for p in ["67", "650", "651", "652", "653", "654", "680", "681", "682", "683", "684"]):
        return "Mtn"
    elif any(tel.startswith(p) for p in ["69", "655", "656", "657", "658", "659", "685", "686", "687", "688", "689"]):
        return "Orange"
    elif any(tel.startswith(p) for p in ["66", "64", "63", "62", "61", "60"]):
        return "Nexttel"
    elif tel.startswith("2"):
        return "Camtel"
    return ""


def send_request(file_path, search_term):
    """Execute remote grep via SSH (equivalent to Grep4j in Java)"""
    retour = []
    try:
        command = f'grep -i "{search_term}" "{file_path}"'
        output = execute_remote_command(
            Variables.MTN_HOST, Variables.MTN_USER, Variables.MTN_PASSWORD, command
        )
        if output:
            for line in output.splitlines():
                if line.strip():
                    retour.append(line)
    except Exception:
        pass
    return retour


def parse_date_safe(date_str, fmt):
    """Safely parse a date string, return None on failure"""
    if not date_str or "null" in date_str:
        return None
    try:
        return datetime.strptime(date_str.strip(), fmt)
    except Exception:
        return None


def find_identification_ocm_by_phone(telephone, all_subscribers):
    lines = send_request(OCM_BDI, telephone.replace("-", ""))
    for chaine in lines:
        if chaine:
            chaine = chaine.replace("|", ";")
            print(chaine)
            tab = chaine.split(";")
            if len(tab) >= 17:
                ib = IdentificationBean()
                ib.telephone = tab[0]
                ib.cni = tab[2]
                ib.name = tab[3]
                ib.birthday = parse_date_safe(tab[4], "%Y-%m-%d")
                ib.expire_date = parse_date_safe(tab[5], "%Y-%m-%d")
                ib.address = tab[6]
                ib.status = tab[16]
                ib.operator = "Orange"
                if ib.telephone == telephone.replace("-", ""):
                    all_subscribers.append(ib)


def find_identification_mtn_by_phone(telephone, all_subscribers):
    lines = send_request(MTN_BDI, telephone.replace("-", ""))
    for chaine in lines:
        if chaine:
            tab = chaine.split(",")
            if len(tab) >= 14:
                ib = IdentificationBean()
                ib.telephone = tab[0]
                ib.cni = tab[1]
                ib.name = tab[3]
                ib.birthday = parse_date_safe(tab[4], "%d-%b-%y")
                ib.expire_date = parse_date_safe(tab[5], "%d-%b-%y")
                ib.address = tab[6]
                ib.status = tab[13]
                ib.operator = "MTN"
                if ib.telephone == telephone.replace("-", ""):
                    all_subscribers.append(ib)


def find_identification_nexttel_by_phone(telephone, all_subscribers):
    lines = send_request(NEXTTEL_BDI, telephone.replace("-", ""))
    for chaine in lines:
        if chaine:
            tab = chaine.split(",")
            if len(tab) >= 12:
                ib = IdentificationBean()
                ib.telephone = tab[0]
                ib.cni = tab[8]
                ib.name = tab[3] + " " + tab[4]
                ib.birthday = parse_date_safe(tab[5], "%d/%m/%y")
                ib.expire_date = parse_date_safe(tab[10], "%d/%m/%y")
                ib.address = tab[11]
                ib.status = ""
                ib.operator = "Nexttel"
                if ib.telephone == telephone.replace("-", ""):
                    all_subscribers.append(ib)


def find_identification_ocm_by_name(name, all_subscribers):
    lines = send_request(OCM_BDI, name)
    for chaine in lines:
        if chaine:
            chaine = chaine.replace("|", ";")
            tab = chaine.split(";")
            if len(tab) >= 17:
                ib = IdentificationBean()
                ib.telephone = tab[0]
                ib.cni = tab[2]
                ib.name = tab[3]
                ib.birthday = parse_date_safe(tab[4], "%Y-%m-%d")
                ib.expire_date = parse_date_safe(tab[5], "%Y-%m-%d")
                ib.address = tab[6]
                ib.status = tab[16]
                ib.operator = "Orange"
                all_subscribers.append(ib)


def find_identification_mtn_by_name(name, all_subscribers):
    lines = send_request(MTN_BDI, name)
    for chaine in lines:
        if chaine:
            tab = chaine.split(",")
            if len(tab) >= 14:
                ib = IdentificationBean()
                ib.telephone = tab[0]
                ib.cni = tab[1]
                ib.name = tab[3]
                ib.birthday = parse_date_safe(tab[4], "%d-%b-%y")
                ib.expire_date = parse_date_safe(tab[5], "%d-%b-%y")
                ib.address = tab[6]
                ib.status = tab[13]
                ib.operator = "MTN"
                all_subscribers.append(ib)


def find_identification_nexttel_by_name(name, all_subscribers):
    lines = send_request(NEXTTEL_BDI, name)
    for chaine in lines:
        if chaine:
            tab = chaine.split(",")
            if len(tab) >= 12:
                ib = IdentificationBean()
                ib.telephone = tab[0]
                ib.cni = tab[8]
                ib.name = tab[3] + " " + tab[4]
                ib.birthday = parse_date_safe(tab[5], "%d/%m/%y")
                ib.expire_date = parse_date_safe(tab[10], "%d/%m/%y")
                ib.address = tab[11]
                ib.status = ""
                ib.operator = "Nexttel"
                all_subscribers.append(ib)


def find_identification_ocm_by_numero_piece(id_number, all_subscribers):
    lines = send_request(OCM_BDI, id_number)
    for chaine in lines:
        if chaine:
            chaine = chaine.replace("|", ";")
            tab = chaine.split(";")
            if len(tab) >= 17:
                ib = IdentificationBean()
                ib.telephone = tab[0]
                ib.cni = tab[2]
                ib.name = tab[3]
                ib.birthday = parse_date_safe(tab[4], "%Y-%m-%d")
                ib.expire_date = parse_date_safe(tab[5], "%Y-%m-%d")
                ib.address = tab[6]
                ib.status = tab[16]
                ib.operator = "Orange"
                if ib.cni == id_number:
                    all_subscribers.append(ib)


def find_identification_mtn_by_numero_piece(id_number, all_subscribers):
    lines = send_request(MTN_BDI, id_number)
    for chaine in lines:
        if chaine:
            tab = chaine.split(",")
            if len(tab) >= 14:
                ib = IdentificationBean()
                ib.telephone = tab[0]
                ib.cni = tab[1]
                ib.name = tab[3]
                ib.birthday = parse_date_safe(tab[4], "%d-%b-%y")
                ib.expire_date = parse_date_safe(tab[5], "%d-%b-%y")
                ib.address = tab[6]
                ib.status = tab[13]
                ib.operator = "MTN"
                if ib.cni == id_number:
                    all_subscribers.append(ib)


def find_identification_nexttel_by_numero_piece(id_number, all_subscribers):
    lines = send_request(NEXTTEL_BDI, id_number)
    for chaine in lines:
        if chaine:
            tab = chaine.split(",")
            if len(tab) >= 12:
                ib = IdentificationBean()
                ib.telephone = tab[0]
                ib.cni = tab[8]
                ib.name = tab[3] + " " + tab[4]
                ib.birthday = parse_date_safe(tab[5], "%d/%m/%y")
                ib.expire_date = parse_date_safe(tab[10], "%d/%m/%y")
                ib.address = tab[11]
                ib.status = ""
                ib.operator = "Nexttel"
                if ib.cni == id_number:
                    all_subscribers.append(ib)


def format_date(date):
    if date:
        return date.strftime("%Y-%m-%d")
    return ""


@identification_bp.route('/identification', methods=['GET', 'POST'])
@login_required
def identification():
    all_subscribers = []
    telephone = session.get('id_telephone', '')
    id_number = session.get('id_number', '')
    name = session.get('id_name', '')

    if request.method == 'POST':
        telephone = request.form.get('telephone', '').strip()
        id_number = request.form.get('idNumber', '').strip()
        name = request.form.get('name', '').strip()

        session['id_telephone'] = telephone
        session['id_number'] = id_number
        session['id_name'] = name

        if telephone:
            op = get_operator_by_telephone(telephone.replace("-", ""))
            if op == "Orange":
                find_identification_ocm_by_phone(telephone, all_subscribers)
            elif op == "Mtn":
                find_identification_mtn_by_phone(telephone, all_subscribers)
            elif op == "Nexttel":
                find_identification_nexttel_by_phone(telephone, all_subscribers)
        elif name:
            find_identification_ocm_by_name(name, all_subscribers)
            find_identification_mtn_by_name(name, all_subscribers)
            find_identification_nexttel_by_name(name, all_subscribers)
        elif id_number:
            find_identification_ocm_by_numero_piece(id_number, all_subscribers)
            find_identification_mtn_by_numero_piece(id_number, all_subscribers)
            find_identification_nexttel_by_numero_piece(id_number, all_subscribers)

    return render_template('identification.html',
                         all_subscribers=all_subscribers,
                         telephone=telephone,
                         id_number=id_number,
                         name=name,
                         format_date=format_date)
