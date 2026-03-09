from datetime import datetime
from app import db
from flask_login import UserMixin


class Users(UserMixin, db.Model):
    __tablename__ = 'users'

    id = db.Column(db.BigInteger, primary_key=True, autoincrement=True)
    login = db.Column(db.String(255), nullable=False)
    password = db.Column(db.String(255), nullable=False)
    name = db.Column(db.String(255))
    phone = db.Column(db.String(255))
    email = db.Column(db.String(255))
    gender = db.Column(db.String(20))
    date_creation = db.Column(db.Date)
    date_modification = db.Column(db.Date)
    version = db.Column(db.Integer)
    signature = db.Column(db.String(255))

    requisitions = db.relationship('Requisitions', backref='user_ref', lazy=True)

    def __init__(self, **kwargs):
        super().__init__(**kwargs)

    def on_create(self):
        now = datetime.now()
        self.date_creation = now
        self.date_modification = now
        self.version = 1
        self.signature = "sig"

    def on_update(self):
        self.date_modification = datetime.now()
        self.version = (self.version or 0) + 1
        self.signature = "sig"

    def __repr__(self):
        return f"Users[id={self.id}]"
