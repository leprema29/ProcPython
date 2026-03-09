import os
import shutil
from datetime import datetime
from flask import Blueprint, render_template, request
from flask_login import login_required, current_user
from app.abou.execute_shell import ExecuteShell
from app.metier.csv_to_xlsx import MtnCsvToXlsxMultiple, OrangeCsvToXlsxMultiple, NexttelCsvToXlsxMultiple
from app.abou.print.pdf_generator import PDFGenerator
from app.metier.variables import Variables
from app.metier.zip_utils import ZipUtils
from app.controllers.listing_controller import delete_folder

listing_imei_multiple_bp = Blueprint('listing_imei_multiple', __name__)


@listing_imei_multiple_bp.route('/listing_imei_multiple', methods=['GET', 'POST'])
@login_required
def listing_imei_multiple():
    all_files_name = []
    date_requisition_string = ""
    demandeur_requisition = ""
    account_type = Variables.ACCOUNT_TYPE
    if account_type in ("SED", "DGSN-CAB", "DSP", "BIR"):
        demandeur_requisition = account_type

    if request.method == 'POST':
        imeis = request.form.getlist('imeis[]')
        begin_date = request.form.get('beginDate', '')
        end_date = request.form.get('endDate', '')
        demandeur = request.form.get('demandeurRequisition', demandeur_requisition)
        numero_requisition = request.form.get('numeroRequisition', '')

        imeis = list(set(i.strip().replace(" ", "") for i in imeis if i.strip() and len(i.strip()) > 2))

        if imeis and begin_date and end_date:
            date_requisition = datetime.now()
            date_requisition_string = date_requisition.strftime("%Y%m%d%H%M%S")

            all_imeis_str = '"' + ' '.join(imeis) + '"'
            param = [all_imeis_str, begin_date, end_date]

            req_dir = os.path.join(Variables.DESTINATION_DOSSIERS, date_requisition_string, "requisition")
            os.makedirs(req_dir, exist_ok=True)

            shell = ExecuteShell(date_requisition_string, demandeur)

            for operator, XlsxClass in [("Orange", OrangeCsvToXlsxMultiple),
                                         ("Mtn", MtnCsvToXlsxMultiple),
                                         ("Nexttel", NexttelCsvToXlsxMultiple)]:
                try:
                    shell.traiter_requisition_imei_multiple(param, "true", operator)
                except Exception as e:
                    print(f"Error: {e}")

                for imei in imeis:
                    pdf_gen = PDFGenerator(imei, date_requisition_string, demandeur, operator,
                                           date_debut=begin_date, date_fin=end_date)
                    pdf_gen.generate()
                    XlsxClass(imei, date_requisition_string, demandeur).generate()

                    src_dir = os.path.join(Variables.DESTINATION_DOSSIERS, date_requisition_string, imei)
                    for fname, dst_fname in [(f"{imei}.xlsx", f"{imei}-{operator}.xlsx"),
                                              (f"Requisition_{imei}.pdf", f"Requisition_{imei}-{operator}.pdf")]:
                        src = os.path.join(src_dir, fname)
                        dst = os.path.join(req_dir, dst_fname)
                        if os.path.exists(src):
                            shutil.copy2(src, dst)

                    # Clean imei directory
                    if os.path.exists(src_dir):
                        for f in os.listdir(src_dir):
                            os.remove(os.path.join(src_dir, f))

                delete_folder(date_requisition_string, operator)

            for imei in imeis:
                for op in ["Mtn", "Orange", "Nexttel"]:
                    all_files_name.extend([f"{imei}-{op}.xlsx", f"Requisition_{imei}-{op}.pdf"])

            source_folder = req_dir
            out_zip = os.path.join(Variables.DESTINATION_DOSSIERS, date_requisition_string,
                                  f"{demandeur}_{numero_requisition}.zip")
            zip_util = ZipUtils(source_folder, out_zip)
            zip_util.generate_file_list(source_folder)
            zip_util.zip_it(out_zip)

    return render_template('listing_imei_multiple.html',
                         all_files_name=all_files_name,
                         date_requisition_string=date_requisition_string,
                         demandeur_requisition=demandeur_requisition)
