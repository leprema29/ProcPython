import re
from datetime import datetime
from flask import Blueprint, render_template, request, redirect, url_for, flash, session
from flask_login import login_user, logout_user, login_required, current_user
from app import db, login_manager
from app.models.users import Users
from app.models.managers import UsersManager
from app.metier.security import md5
from app.beans.cirt_log import CirtLog

session_bp = Blueprint('session', __name__)


@login_manager.user_loader
def load_user(user_id):
    return Users.query.get(int(user_id))


def create_one_time_password():
    now = datetime.now()
    day = now.strftime("%d")
    month = now.strftime("%m")
    year = now.strftime("%y")

    somme_jour = int(day[0]) + int(day[1])
    while somme_jour > 9:
        s = str(somme_jour)
        somme_jour = int(s[0]) + int(s[1])

    pwd = f"{year[1]}{year[0]}{somme_jour}{month[1]}{month[0]}{day[1]}{day[0]}"
    return pwd


@session_bp.route('/')
@session_bp.route('/login', methods=['GET', 'POST'])
def login():
    if current_user.is_authenticated:
        return redirect(url_for('session.home'))

    if request.method == 'POST':
        login_val = request.form.get('login', '')
        password_val = request.form.get('password', '')
        key_val = request.form.get('key', '')
        ip_address = request.headers.get('X-Forwarded-For', request.remote_addr)

        username_pattern = r'^[a-zA-Z0-9]+$'
        password_pattern = r'^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()<>?/|}{~`]).{8,}$'

        if re.match(username_pattern, login_val) and re.match(password_pattern, password_val):
            user = UsersManager.get_user_by_login_password(login_val, md5(password_val))
            if user and key_val == create_one_time_password():
                login_user(user)
                session['admin'] = ((user.id == 2 and user.login == "prosper") or
                                   (user.id == 9 and user.login == "lee"))
                session['allowed'] = user.id not in (5, 6)
                log = CirtLog(timestamp=datetime.now(), ip_address=ip_address,
                             task="Login", user=user, mac_address="")
                log.create_session_log()
                flash(f"Welcome, {user.name}", "info")
                return redirect(url_for('session.home'))
            else:
                log = CirtLog(timestamp=datetime.now(), ip_address=ip_address,
                             login=login_val, task="Login failed", mac_address="")
                log.create_failed_session_log()
                flash(f"Login failed: {login_val}", "error")
        else:
            log = CirtLog(timestamp=datetime.now(), ip_address=ip_address,
                         login=login_val, task="Login failed", mac_address="")
            log.create_failed_session_log()
            flash(f"Login failed: {login_val}", "error")

    return render_template('login.html')


@session_bp.route('/home')
@login_required
def home():
    return render_template('home.html')


@session_bp.route('/logout')
@login_required
def logout():
    ip_address = request.headers.get('X-Forwarded-For', request.remote_addr)
    log = CirtLog(timestamp=datetime.now(), ip_address=ip_address,
                 task="Logout", user=current_user, mac_address="")
    log.create_session_log()
    logout_user()
    session.clear()
    return redirect(url_for('session.login'))


@session_bp.route('/change_password', methods=['GET', 'POST'])
@login_required
def change_password():
    if request.method == 'POST':
        old_password = request.form.get('oldPassword', '')
        new_password = request.form.get('newPassword', '')
        if current_user.password == md5(old_password):
            current_user.password = md5(new_password)
            UsersManager.update(current_user)
            flash("Password changed successfully.", "info")
        else:
            flash("Old password incorrect", "error")
    return render_template('change_password.html')
