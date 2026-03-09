from urllib.parse import quote_plus
from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_login import LoginManager

db = SQLAlchemy()
login_manager = LoginManager()


def create_app():
    app = Flask(__name__)

    from app.metier.variables import Variables
    app.config['SECRET_KEY'] = 'tracking-security-system-secret-key'
    app.config['SQLALCHEMY_DATABASE_URI'] = (
        f'mysql+pymysql://{Variables.DB_USER}:{quote_plus(Variables.DB_PASSWORD)}'
        f'@{Variables.DB_HOST}:{Variables.DB_PORT}/{Variables.DB_NAME}'
    )
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
    app.config['PERMANENT_SESSION_LIFETIME'] = 18000  # 300 minutes

    db.init_app(app)
    login_manager.init_app(app)
    login_manager.login_view = 'session.login'

    from app.controllers.session_controller import session_bp
    from app.controllers.listing_controller import listing_bp
    from app.controllers.listing_multiple_controller import listing_multiple_bp
    from app.controllers.listing_imei_controller import listing_imei_bp
    from app.controllers.listing_imei_multiple_controller import listing_imei_multiple_bp
    from app.controllers.listing_momo_controller import listing_momo_bp
    from app.controllers.identification_controller import identification_bp
    from app.controllers.historique_controller import historique_bp
    from app.controllers.user_controller import user_bp
    from app.controllers.whitelist_controller import whitelist_bp
    from app.controllers.download_controller import download_bp

    app.register_blueprint(session_bp)
    app.register_blueprint(listing_bp)
    app.register_blueprint(listing_multiple_bp)
    app.register_blueprint(listing_imei_bp)
    app.register_blueprint(listing_imei_multiple_bp)
    app.register_blueprint(listing_momo_bp)
    app.register_blueprint(identification_bp)
    app.register_blueprint(historique_bp)
    app.register_blueprint(user_bp)
    app.register_blueprint(whitelist_bp)
    app.register_blueprint(download_bp)

    with app.app_context():
        db.create_all()

    return app
