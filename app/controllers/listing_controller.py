import os
from datetime import datetime
from flask import Blueprint, render_template, request, flash
from flask_login import login_required, current_user
from app.models.requisitions import Requisitions
from app.models.managers import RequisitionsManager
from app.abou.execute_shell import ExecuteShell
from app.metier.csv_to_xlsx import MtnCsvToXlsx, OrangeCsvToXlsx, NexttelCsvToXlsx
from app.abou.print.pdf_generator import PDFGenerator
from app.metier.variables import Variables
from app.abou.ssh_utils import sftp_recursive_delete

listing_bp = Blueprint('listing', __name__)


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


def delete_folder(folder, operator):
    host, user, password = "", "", ""
    if operator == "Orange":
        host, user, password = Variables.ORANGE_HOST, Variables.ORANGE_USER, Variables.ORANGE_PASSWORD
    elif operator == "Mtn":
        host, user, password = Variables.MTN_HOST, Variables.MTN_USER, Variables.MTN_PASSWORD
    elif operator == "Nexttel":
        host, user, password = Variables.NEXTTEL_HOST, Variables.NEXTTEL_USER, Variables.NEXTTEL_PASSWORD
    if host:
        sftp_recursive_delete(host, user, password, f"/root/{folder}")
        print("folder deleted")


@listing_bp.route('/listing', methods=['GET', 'POST'])
@login_required
def listing():
    all_files_name = []
    good_phone = ""
    date_requisition_string = ""

    if request.method == 'POST':
        telephone = request.form.get('telephone', '').replace("-", "")
        begin_date = request.form.get('beginDate', '')
        end_date = request.form.get('endDate', '')

        if telephone and begin_date and end_date:
            date_requisition = datetime.now()
            date_requisition_string = date_requisition.strftime("%Y%m%d%H%M%S")
            good_phone = telephone
            operateur = get_operator_by_telephone(telephone)
            print(f"Operateur = {operateur}")

            requisition = Requisitions()
            requisition.telephone = good_phone
            requisition.user = current_user.id
            requisition.date_requisition = date_requisition
            RequisitionsManager.create(requisition)

            shell = ExecuteShell(date_requisition_string)
            param = [telephone, begin_date, end_date]
            try:
                shell.traiter_requisition(param, "true", operateur)
            except Exception as e:
                print(f"Error: {e}")

            # Generate PDF
            pdf_gen = PDFGenerator(good_phone, date_requisition_string, operator=operateur,
                                   date_debut=begin_date, date_fin=end_date)
            pdf_gen.generate()

            # Generate XLSX
            if operateur == "Mtn":
                MtnCsvToXlsx(good_phone, date_requisition_string).generate()
            elif operateur == "Orange":
                OrangeCsvToXlsx(good_phone, date_requisition_string).generate()
            elif operateur == "Nexttel":
                NexttelCsvToXlsx(good_phone, date_requisition_string).generate()

            all_files_name = [f"{good_phone}.xlsx", f"Requisition_{good_phone}.pdf"]
            delete_folder(good_phone, operateur)
            print(len(all_files_name))

    return render_template('listing.html',
                         all_files_name=all_files_name,
                         good_phone=good_phone,
                         date_requisition_string=date_requisition_string)
