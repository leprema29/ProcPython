from datetime import datetime
from app import db


class Requisitions(db.Model):
    __tablename__ = 'requisitions'

    id = db.Column(db.BigInteger, primary_key=True, autoincrement=True)
    telephone = db.Column(db.String(255), nullable=False)
    date_requisition = db.Column(db.DateTime)
    user = db.Column(db.BigInteger, db.ForeignKey('users.id'))
    date_creation = db.Column(db.DateTime)
    date_modification = db.Column(db.DateTime)
    version = db.Column(db.Integer)
    signature = db.Column(db.String(255))
    etat = db.Column(db.Integer, default=0)

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
        return f"Requisitions[id={self.id}]"
