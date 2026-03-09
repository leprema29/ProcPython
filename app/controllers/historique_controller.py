import os
import shutil
from flask import Blueprint, render_template, request, redirect, url_for, flash
from flask_login import login_required, current_user
from app.models.requisitions import Requisitions
from app.models.managers import RequisitionsManager
from app.metier.variables import Variables

historique_bp = Blueprint('historique', __name__)


def format_date(date):
    if date:
        return date.strftime("%Y%m%d%H%M%S")
    return ""


def get_etat_requisition(etat):
    if etat == 1:
        return "Finish"
    return "Ongoing"


@historique_bp.route('/historique_listing')
@login_required
def historique_listing():
    all_requisitions = RequisitionsManager.get_requisitions_by_user(current_user)
    return render_template('historique_listing.html',
                         all_requisitions=all_requisitions,
                         format_date=format_date,
                         get_etat_requisition=get_etat_requisition)


@historique_bp.route('/delete_requisition/<int:req_id>', methods=['POST'])
@login_required
def delete_requisition(req_id):
    requisition = RequisitionsManager.get_by_id(req_id, Requisitions)
    if requisition:
        print(f"out {req_id}")
        print(f"Telephone {requisition.telephone}")
        RequisitionsManager.delete(requisition)
        date_req = format_date(requisition.date_requisition)
        print(f"Deletion of requisition No {requisition.telephone}")

        try:
            dir_path = os.path.join(Variables.DESTINATION_DOSSIERS, date_req, requisition.telephone)
            if os.path.isdir(dir_path):
                shutil.rmtree(dir_path)

            xlsx_path = os.path.join(Variables.DESTINATION_DOSSIERS, date_req, "requisition",
                                    f"{requisition.telephone}.xlsx")
            if os.path.exists(xlsx_path):
                os.remove(xlsx_path)

            pdf_path = os.path.join(Variables.DESTINATION_DOSSIERS, date_req, "requisition",
                                   f"Requisition_{requisition.telephone}.pdf")
            if os.path.exists(pdf_path):
                os.remove(pdf_path)

            zip_path = os.path.join(Variables.DESTINATION_DOSSIERS, date_req, "requisition.zip")
            if os.path.exists(zip_path):
                os.remove(zip_path)
        except Exception as e:
            print(f"Error deleting files: {e}")

    return redirect(url_for('historique.historique_listing'))
