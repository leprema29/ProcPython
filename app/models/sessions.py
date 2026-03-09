from datetime import datetime
from app import db


class Sessions(db.Model):
    __tablename__ = 'sessions'

    id = db.Column(db.BigInteger, primary_key=True, autoincrement=True)
    name = db.Column(db.String(255), nullable=False)
    created = db.Column(db.DateTime)
    date_creation = db.Column(db.DateTime)
    date_modification = db.Column(db.DateTime)
    version = db.Column(db.Integer)
    signature = db.Column(db.String(255))

    def __repr__(self):
        return f"Sessions[id={self.id}]"
