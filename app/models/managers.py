from app import db
from app.models.users import Users
from app.models.requisitions import Requisitions


class UsersManager:
    @staticmethod
    def get_by_id(user_id):
        try:
            return Users.query.get(user_id)
        except Exception as e:
            import traceback
            traceback.print_exc()
            return None

    @staticmethod
    def get_all():
        try:
            return Users.query.all()
        except Exception as e:
            import traceback
            traceback.print_exc()
            return None

    @staticmethod
    def create(user):
        try:
            user.on_create()
            db.session.add(user)
            db.session.commit()
            return user
        except Exception as e:
            db.session.rollback()
            import traceback
            traceback.print_exc()
            return None

    @staticmethod
    def update(user):
        try:
            user.on_update()
            db.session.commit()
        except Exception:
            db.session.rollback()

    @staticmethod
    def delete(user):
        try:
            db.session.delete(user)
            db.session.commit()
        except Exception:
            db.session.rollback()

    @staticmethod
    def get_user_by_login_password(login, password):
        try:
            return Users.query.filter_by(login=login, password=password).first()
        except Exception:
            return None


class RequisitionsManager:
    @staticmethod
    def get_by_id(req_id, model_class=None):
        try:
            if model_class:
                return model_class.query.get(req_id)
            return Requisitions.query.get(req_id)
        except Exception:
            import traceback
            traceback.print_exc()
            return None

    @staticmethod
    def get_all():
        try:
            return Requisitions.query.all()
        except Exception:
            import traceback
            traceback.print_exc()
            return None

    @staticmethod
    def get_requisitions_by_user(user):
        try:
            return Requisitions.query.filter_by(user=user.id).all()
        except Exception:
            import traceback
            traceback.print_exc()
            return None

    @staticmethod
    def create(requisition):
        try:
            requisition.on_create()
            db.session.add(requisition)
            db.session.commit()
            return requisition
        except Exception:
            db.session.rollback()
            import traceback
            traceback.print_exc()
            return None

    @staticmethod
    def update(requisition):
        try:
            requisition.on_update()
            db.session.commit()
        except Exception:
            db.session.rollback()

    @staticmethod
    def delete(requisition):
        try:
            db.session.delete(requisition)
            db.session.commit()
        except Exception:
            db.session.rollback()
