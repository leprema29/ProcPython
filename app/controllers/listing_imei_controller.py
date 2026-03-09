from datetime import datetime
from flask import Blueprint, render_template, request
from flask_login import login_required, current_user
from app.models.requisitions import Requisitions
from app.models.managers import RequisitionsManager
from app.abou.execute_shell import ExecuteShell
from app.metier.csv_to_xlsx import MtnCsvToXlsx, OrangeCsvToXlsx, NexttelCsvToXlsx
from app.abou.print.pdf_generator import PDFGenerator

listing_imei_bp = Blueprint('listing_imei', __name__)


@listing_imei_bp.route('/listing_imei', methods=['GET', 'POST'])
@login_required
def listing_imei():
    all_files_name = []
    good_phone = ""
    date_requisition_string = ""

    if request.method == 'POST':
        telephone = request.form.get('telephone', '').replace("-", "")
        operator = request.form.get('operator', '')
        begin_date = request.form.get('beginDate', '')
        end_date = request.form.get('endDate', '')

        if telephone and operator and begin_date and end_date:
            date_requisition = datetime.now()
            date_requisition_string = date_requisition.strftime("%Y%m%d%H%M%S")
            good_phone = telephone

            requisition = Requisitions()
            requisition.telephone = good_phone
            requisition.user = current_user.id
            requisition.date_requisition = date_requisition
            RequisitionsManager.create(requisition)

            shell = ExecuteShell(date_requisition_string)
            param = [telephone, begin_date, end_date]
            try:
                shell.traiter_requisition(param, "true", operator)
            except Exception as e:
                print(f"Error: {e}")

            pdf_gen = PDFGenerator(good_phone, date_requisition_string, operator=operator)
            pdf_gen.generate()

            if operator == "Mtn":
                MtnCsvToXlsx(good_phone, date_requisition_string).generate()
            elif operator == "Orange":
                OrangeCsvToXlsx(good_phone, date_requisition_string).generate()
            elif operator == "Nexttel":
                NexttelCsvToXlsx(good_phone, date_requisition_string).generate()

            all_files_name = [f"{good_phone}.xlsx", f"Requisition_{good_phone}.pdf"]

    return render_template('listing_imei.html',
                         all_files_name=all_files_name,
                         good_phone=good_phone,
                         date_requisition_string=date_requisition_string)
