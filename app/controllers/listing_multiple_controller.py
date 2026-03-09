import os
import shutil
from datetime import datetime
from flask import Blueprint, render_template, request, flash
from flask_login import login_required, current_user
from app.models.requisitions import Requisitions
from app.models.managers import RequisitionsManager
from app.abou.execute_shell import ExecuteShell
from app.metier.csv_to_xlsx import MtnCsvToXlsxMultiple, OrangeCsvToXlsxMultiple, NexttelCsvToXlsxMultiple
from app.abou.print.pdf_generator import PDFGenerator
from app.metier.variables import Variables
from app.metier.zip_utils import ZipUtils
from app.beans.cirt_log import CirtLog
from app.controllers.listing_controller import get_operator_by_telephone, delete_folder

listing_multiple_bp = Blueprint('listing_multiple', __name__)


@listing_multiple_bp.route('/listing_multiple', methods=['GET', 'POST'])
@login_required
def listing_multiple():
    all_files_name = []
    date_requisition_string = ""
    demandeur_requisition = ""
    account_type = Variables.ACCOUNT_TYPE
    if account_type in ("SED", "DGSN-CAB", "DSP", "BIR"):
        demandeur_requisition = account_type

    if request.method == 'POST':
        phones = request.form.getlist('phones[]')
        begin_date = request.form.get('beginDate', '')
        end_date = request.form.get('endDate', '')
        demandeur = request.form.get('demandeurRequisition', demandeur_requisition)
        numero_requisition = request.form.get('numeroRequisition', '')
        ip_address = request.headers.get('X-Forwarded-For', request.remote_addr)

        phones = [p.replace("-", "").strip() for p in phones if p.strip()]
        if phones and begin_date and end_date:
            date_requisition = datetime.now()
            date_requisition_string = date_requisition.strftime("%Y%m%d%H%M%S")

            orange_phones = set()
            mtn_phones = set()
            nexttel_phones = set()

            for tel in phones:
                op = get_operator_by_telephone(tel)
                if op == "Orange":
                    orange_phones.add(tel)
                elif op == "Mtn":
                    mtn_phones.add(tel)
                elif op == "Nexttel":
                    nexttel_phones.add(tel)

            shell = ExecuteShell(date_requisition_string, demandeur)

            for phone_set, operator in [(orange_phones, "Orange"), (mtn_phones, "Mtn"), (nexttel_phones, "Nexttel")]:
                if phone_set:
                    all_phones_str = '"' + ' '.join(phone_set) + '"'
                    param = [all_phones_str, begin_date, end_date]
                    try:
                        shell.traiter_requisition_multiple(param, "true", operator)
                    except Exception as e:
                        print(f"Error: {e}")

            # Create requisition directory
            req_dir = os.path.join(Variables.DESTINATION_DOSSIERS, date_requisition_string, "requisition")
            os.makedirs(req_dir, exist_ok=True)

            # Process each operator's phones
            for phone_set, operator, XlsxClass in [
                (mtn_phones, "Mtn", MtnCsvToXlsxMultiple),
                (orange_phones, "Orange", OrangeCsvToXlsxMultiple),
                (nexttel_phones, "Nexttel", NexttelCsvToXlsxMultiple)
            ]:
                for tel in phone_set:
                    pdf_gen = PDFGenerator(tel, date_requisition_string, demandeur, operator)
                    pdf_gen.generate()
                    XlsxClass(tel, date_requisition_string, demandeur).generate()
                    all_files_name.extend([f"{tel}.xlsx", f"Requisition_{tel}.pdf"])

                    # Copy files to requisition folder
                    src_dir = os.path.join(Variables.DESTINATION_DOSSIERS, date_requisition_string, tel)
                    for fname in [f"{tel}.xlsx", f"Requisition_{tel}.pdf"]:
                        src = os.path.join(src_dir, fname)
                        dst = os.path.join(req_dir, fname)
                        if os.path.exists(src):
                            shutil.copy2(src, dst)

            # Delete remote folders
            for phone_set, operator in [(mtn_phones, "Mtn"), (orange_phones, "Orange"), (nexttel_phones, "Nexttel")]:
                if phone_set:
                    delete_folder(date_requisition_string, operator)
                    break

            # Create zip
            source_folder = req_dir
            out_zip = os.path.join(Variables.DESTINATION_DOSSIERS, date_requisition_string,
                                  f"{demandeur}_{numero_requisition}.zip")
            zip_util = ZipUtils(source_folder, out_zip)
            zip_util.generate_file_list(source_folder)
            zip_util.zip_it(out_zip)

    return render_template('listing_multiple.html',
                         all_files_name=all_files_name,
                         date_requisition_string=date_requisition_string,
                         demandeur_requisition=demandeur_requisition)
