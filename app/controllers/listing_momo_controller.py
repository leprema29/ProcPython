from datetime import datetime
from flask import Blueprint, render_template, request
from flask_login import login_required
from app.abou.execute_shell import ExecuteShell
from app.metier.csv_to_xlsx import MomoCsvToXlsx
from app.abou.print.pdf_generator import PDFGenerator
from app.controllers.listing_controller import get_operator_by_telephone

listing_momo_bp = Blueprint('listing_momo', __name__)


@listing_momo_bp.route('/listing_momo', methods=['GET', 'POST'])
@login_required
def listing_momo():
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

            shell = ExecuteShell(date_requisition_string)
            param = [telephone, begin_date, end_date]
            try:
                shell.traiter_requisition(param, "true", "MOMO")
            except Exception as e:
                print(f"Error: {e}")

            pdf_gen = PDFGenerator(good_phone, date_requisition_string, operator="MOMO")
            pdf_gen.generate()
            MomoCsvToXlsx(good_phone, date_requisition_string).generate()

            all_files_name = [f"{good_phone}_MOMO.xlsx", f"Requisition_{good_phone}.pdf"]

    return render_template('listing_momo.html',
                         all_files_name=all_files_name,
                         good_phone=good_phone,
                         date_requisition_string=date_requisition_string)
