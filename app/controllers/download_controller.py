import os
import mimetypes
from flask import Blueprint, send_file, abort
from flask_login import login_required
from app.metier.variables import Variables

download_bp = Blueprint('download', __name__)


@download_bp.route('/download/<path:file_path>')
@login_required
def download(file_path):
    """Serve files from DESTINATION_DOSSIERS (equivalent to Telechargement servlet)"""
    print(f"debut/{file_path}")

    full_path = os.path.join(Variables.DESTINATION_DOSSIERS, file_path)

    if not os.path.exists(full_path):
        print("mouf")
        abort(404)

    content_type, _ = mimetypes.guess_type(full_path)
    print(os.path.basename(full_path))
    print(content_type)

    if content_type is None:
        print("erreur")
        abort(404)

    return send_file(
        full_path,
        mimetype=content_type,
        as_attachment=False,
        download_name=os.path.basename(full_path)
    )
