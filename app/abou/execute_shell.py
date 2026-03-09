import os
from datetime import datetime
from app.metier.variables import Variables
from app.abou.remote_requi_mtn import RemoteRequiMTN
from app.abou.remote_requi_orange import RemoteRequiORANGE
from app.abou.remote_requi_nexttel import RemoteRequiNEXTTEL


class ExecuteShell:
    def __init__(self, date_requisition, demandeur_requisition=""):
        self.date_requisition = date_requisition
        self.demandeur_requisition = demandeur_requisition

    def create_directory(self, folder_name):
        path = os.path.join(Variables.DESTINATION_DOSSIERS, self.date_requisition, folder_name)
        os.makedirs(path, exist_ok=True)
        print(f"Directory created: {path}")

    def create_directory_for_imei(self, folder_name):
        path = os.path.join(Variables.DESTINATION_DOSSIERS, self.date_requisition, folder_name)
        os.makedirs(path, exist_ok=True)
        print(f"Directory created: {path}")

    def traiter_requisition(self, param, identification, operateur):
        """Process a single number requisition"""
        numero = param[0]
        date_debut = param[1]
        date_fin = param[2]

        # Create directory for this requisition
        self.create_directory(numero)

        if operateur == "Mtn":
            remote = RemoteRequiMTN(self.date_requisition)
            remote.remote_find_mtn(numero, date_debut, date_fin, identification)
        elif operateur == "Orange":
            remote = RemoteRequiORANGE(self.date_requisition)
            remote.remote_find_orange(numero, date_debut, date_fin, identification)
        elif operateur == "Nexttel":
            remote = RemoteRequiNEXTTEL(self.date_requisition)
            remote.remote_find_nexttel(numero, date_debut, date_fin, identification)
        elif operateur == "MOMO":
            remote = RemoteRequiMTN(self.date_requisition)
            remote.remote_find_mtn(numero, date_debut, date_fin, identification)

    def traiter_requisition_multiple(self, param, identification, operateur):
        """Process multiple numbers requisition"""
        numeros = param[0]  # quoted space-separated numbers
        date_debut = param[1]
        date_fin = param[2]

        # Remove quotes
        numeros_clean = numeros.strip('"')
        numero_list = numeros_clean.split()

        for numero in numero_list:
            self.create_directory(numero)

        if operateur == "Mtn":
            remote = RemoteRequiMTN(self.date_requisition, self.demandeur_requisition)
            remote.remote_find_mtn_multiple(numeros, date_debut, date_fin, identification)
        elif operateur == "Orange":
            remote = RemoteRequiORANGE(self.date_requisition, self.demandeur_requisition)
            remote.remote_find_orange_multiple(numeros, date_debut, date_fin, identification)
        elif operateur == "Nexttel":
            remote = RemoteRequiNEXTTEL(self.date_requisition, self.demandeur_requisition)
            remote.remote_find_nexttel_multiple(numeros, date_debut, date_fin, identification)

    def traiter_requisition_imei_multiple(self, param, identification, operateur):
        """Process multiple IMEI requisition"""
        imeis = param[0]
        date_debut = param[1]
        date_fin = param[2]

        imeis_clean = imeis.strip('"')
        imei_list = imeis_clean.split()

        for imei in imei_list:
            self.create_directory(imei)

        if operateur == "Mtn":
            remote = RemoteRequiMTN(self.date_requisition, self.demandeur_requisition)
            remote.remote_find_mtn_imei_multiple(imeis, date_debut, date_fin, identification)
        elif operateur == "Orange":
            remote = RemoteRequiORANGE(self.date_requisition, self.demandeur_requisition)
            remote.remote_find_orange_imei_multiple(imeis, date_debut, date_fin, identification)
        elif operateur == "Nexttel":
            remote = RemoteRequiNEXTTEL(self.date_requisition, self.demandeur_requisition)
            remote.remote_find_nexttel_imei_multiple(imeis, date_debut, date_fin, identification)

    @staticmethod
    def convert_date(date_str):
        try:
            return datetime.strptime(date_str, "%Y-%m-%d")
        except Exception:
            return None
