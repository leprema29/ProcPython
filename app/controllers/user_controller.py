from flask import Blueprint, render_template, request, flash
from flask_login import login_required
from app.models.users import Users
from app.models.managers import UsersManager
from app.metier.security import md5

user_bp = Blueprint('user', __name__)


@user_bp.route('/create_user', methods=['GET', 'POST'])
@login_required
def create_user():
    if request.method == 'POST':
        login_val = request.form.get('login', '')
        password_val = request.form.get('password', '')
        name_val = request.form.get('name', '')

        user = Users()
        user.login = login_val
        user.password = md5(password_val)
        user.name = name_val

        result = UsersManager.create(user)
        if result:
            flash("User created successfully.", "info")
        else:
            flash("User creation failed.", "error")

    return render_template('create_user.html')
