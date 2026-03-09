import os
from flask import Blueprint, render_template, request, flash
from flask_login import login_required
from app.metier.variables import Variables

whitelist_bp = Blueprint('whitelist', __name__)

WHITELIST_FILE = os.path.join(Variables.BASE_FOLDER, "whitelistphone.txt")


def load_whitelist():
    val = ""
    try:
        with open(WHITELIST_FILE, 'r', encoding='utf-8') as f:
            for line in f:
                phone = line.rstrip('\n')
                val = val + "\n" + phone
    except Exception as e:
        print(f"Error loading whitelist: {e}")
    return val.lstrip("\n")


@whitelist_bp.route('/whitelist', methods=['GET', 'POST'])
@login_required
def whitelist():
    white_list = load_whitelist()

    if request.method == 'POST':
        white_list = request.form.get('whiteList', '')
        # Write whitelist (replicated 5 times as in Java original)
        for _ in range(5):
            try:
                with open(WHITELIST_FILE, 'w', encoding='utf-8') as f:
                    f.write(white_list)
            except Exception:
                pass
        flash("Whitelist updated.", "info")

    return render_template('whitelist.html', white_list=white_list)
