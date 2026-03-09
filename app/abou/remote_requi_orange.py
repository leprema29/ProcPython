import os
from app.metier.variables import Variables
from app.abou.ssh_utils import execute_remote_command, sftp_read_file
from app.abou.csv.format.entities import (
    Listing, ListingOrangeSMS, IdentificationORANGE, StatistiqueAppels, StatistiqueLieux,
    FreqCell, FreqCorrespondant, FreqDureeAppel, FreqImei, SharedImei
)
from app.abou.csv.csv_file_writer import CsvFileWriterORANGE, CsvFileWriterORANGEMultiple


class RemoteRequiORANGE:
    def __init__(self, date_requisition, demandeur_requisition=""):
        self.date_requisition = date_requisition
        self.demandeur_requisition = demandeur_requisition
        self.host = Variables.ORANGE_HOST
        self.user = Variables.ORANGE_USER
        self.password = Variables.ORANGE_PASSWORD

    def remote_find_orange(self, numero, date_debut, date_fin, identification):
        command = (f"bash /home/data/orange/operations/cdr/requisitionOrange_Multiple_Optimized.sh "
                   f"{numero} {date_debut} {date_fin} {self.date_requisition}")
        print(f"Executing: {command}")
        try:
            output = execute_remote_command(self.host, self.user, self.password, command)
            print(f"Command output: {output}")
        except Exception as e:
            print(f"Error executing remote command: {e}")
        self.lire_fic_orange(numero)

    def remote_find_orange_multiple(self, numeros, date_debut, date_fin, identification):
        command = (f"bash /home/data/orange/operations/cdr/requisitionOrange_Multiple_Optimized.sh "
                   f"{numeros} {date_debut} {date_fin} {self.date_requisition}")
        print(f"Executing: {command}")
        try:
            output = execute_remote_command(self.host, self.user, self.password, command)
            print(f"Command output: {output}")
        except Exception as e:
            print(f"Error: {e}")
        numeros_clean = numeros.strip('"')
        for numero in numeros_clean.split():
            self.lire_fic_orange_multiple(numero)

    def remote_find_orange_imei_multiple(self, imeis, date_debut, date_fin, identification):
        command = (f"bash /home/data/orange/operations/cdr/requisitionImeiOrange_Multiple_Optimized.sh "
                   f"{imeis} {date_debut} {date_fin} {self.date_requisition}")
        print(f"Executing: {command}")
        try:
            output = execute_remote_command(self.host, self.user, self.password, command)
            print(f"Command output: {output}")
        except Exception as e:
            print(f"Error: {e}")
        imeis_clean = imeis.strip('"')
        for imei in imeis_clean.split():
            self.lire_fic_orange_multiple(imei)

    def lire_fic_orange(self, numero):
        base_remote = f"/root/{self.date_requisition}/{numero}"
        base_local = os.path.join(Variables.DESTINATION_DOSSIERS, self.date_requisition, numero)
        os.makedirs(base_local, exist_ok=True)

        self._read_id_numero(base_remote, base_local, numero)
        self._read_listing_emis(base_remote, base_local, numero)
        self._read_listing_sms(base_remote, base_local, numero)
        self._read_identification_abonnees(base_remote, base_local, numero)
        self._read_statistiques(base_remote, base_local, numero)
        self._read_statistiques_localisation(base_remote, base_local, numero)

    def lire_fic_orange_multiple(self, numero):
        base_remote = f"/root/{self.date_requisition}/{numero}"
        base_local = os.path.join(Variables.DESTINATION_DOSSIERS, self.date_requisition, numero)
        os.makedirs(base_local, exist_ok=True)

        self._read_id_numero(base_remote, base_local, numero)
        self._read_listing_emis(base_remote, base_local, numero)
        self._read_listing_sms(base_remote, base_local, numero)
        self._read_identification_abonnees(base_remote, base_local, numero)
        self._read_statistiques(base_remote, base_local, numero)
        self._read_statistiques_localisation(base_remote, base_local, numero)
        self._read_freq_cellule(base_remote, base_local, numero)
        self._read_freq_correspondant(base_remote, base_local, numero)
        self._read_freq_duree_appel(base_remote, base_local, numero)
        self._read_freq_imei(base_remote, base_local, numero)
        self._read_shared_imei(base_remote, base_local, numero)

    def _read_id_numero(self, base_remote, base_local, numero):
        try:
            content = sftp_read_file(self.host, self.user, self.password,
                                     f"{base_remote}/identite_numero.txt")
            data_list = []
            for line in content.strip().split('\n'):
                if line.strip():
                    parts = line.split(',')
                    if len(parts) >= 6:
                        item = IdentificationORANGE(
                            numero=parts[0], nom_prenom=parts[1], date_naissance=parts[2],
                            numero_cni=parts[3], date_exp_cni=parts[4], quartier=parts[5]
                        )
                        data_list.append(item)
            csv_path = os.path.join(base_local, f"Requisition_IdNumero_{numero}.csv")
            CsvFileWriterORANGE.write_id_numero(csv_path, data_list)
        except Exception as e:
            print(f"Error reading id_numero Orange: {e}")

    def _read_listing_emis(self, base_remote, base_local, numero):
        try:
            content = sftp_read_file(self.host, self.user, self.password,
                                     f"{base_remote}/appelemis.txt")
            data_list = []
            for line in content.strip().split('\n'):
                if line.strip():
                    parts = line.split(',')
                    if len(parts) >= 6:
                        item = Listing(
                            numero_appelant=parts[0], localisation_numero_appelant=parts[1],
                            imei_numero_appelant=parts[2], date_debut_appel=parts[3],
                            duree_appel=parts[4], numero_appele=parts[5]
                        )
                        data_list.append(item)
            csv_path = os.path.join(base_local, f"Requisition_Listing_Emis_{numero}.csv")
            CsvFileWriterORANGE.write_listing_emis(csv_path, data_list)
        except Exception as e:
            print(f"Error reading listing emis Orange: {e}")

    def _read_listing_sms(self, base_remote, base_local, numero):
        try:
            content = sftp_read_file(self.host, self.user, self.password,
                                     f"{base_remote}/smsfinal.txt")
            data_list = []
            for line in content.strip().split('\n'):
                if line.strip():
                    parts = line.split(',')
                    if len(parts) >= 5:
                        item = ListingOrangeSMS(
                            numero_envoi=parts[0], localisation_numero_dest=parts[1],
                            imei_numero_dest=parts[2], date_sms=parts[3], numero_dest=parts[4]
                        )
                        data_list.append(item)
            csv_path = os.path.join(base_local, f"Requisition_Listing_SMS_{numero}.csv")
            CsvFileWriterORANGE.write_listing_sms(csv_path, data_list)
        except Exception as e:
            print(f"Error reading listing SMS Orange: {e}")

    def _read_identification_abonnees(self, base_remote, base_local, numero):
        try:
            content = sftp_read_file(self.host, self.user, self.password,
                                     f"{base_remote}/IdentificationAbonneesfinal.txt")
            data_list = []
            for line in content.strip().split('\n'):
                if line.strip():
                    parts = line.split(',')
                    if len(parts) >= 6:
                        item = IdentificationORANGE(
                            numero=parts[0], nom_prenom=parts[1], date_naissance=parts[2],
                            numero_cni=parts[3], date_exp_cni=parts[4], quartier=parts[5]
                        )
                        data_list.append(item)
            csv_path = os.path.join(base_local, f"IdentificationAbonnees_{numero}.csv")
            CsvFileWriterORANGE.write_identification_abonnees(csv_path, data_list)
        except Exception as e:
            print(f"Error reading identification abonnees Orange: {e}")

    def _read_statistiques(self, base_remote, base_local, numero):
        try:
            content = sftp_read_file(self.host, self.user, self.password,
                                     f"{base_remote}/statistiques.txt")
            data_list = []
            for line in content.strip().split('\n'):
                if line.strip():
                    parts = line.split(',')
                    if len(parts) >= 3:
                        item = StatistiqueAppels(
                            numero_appelant=parts[0],
                            occurence=int(parts[1]) if parts[1].isdigit() else 0,
                            duree_appel=parts[2]
                        )
                        data_list.append(item)
            csv_path = os.path.join(base_local, f"Statistiques_{numero}.csv")
            CsvFileWriterORANGE.write_statistiques(csv_path, data_list)
        except Exception as e:
            print(f"Error reading statistiques Orange: {e}")

    def _read_statistiques_localisation(self, base_remote, base_local, numero):
        try:
            content = sftp_read_file(self.host, self.user, self.password,
                                     f"{base_remote}/statistiquesLocalisation.txt")
            data_list = []
            for line in content.strip().split('\n'):
                if line.strip():
                    parts = line.split(',')
                    if len(parts) >= 2:
                        item = StatistiqueLieux(
                            localisation=parts[0],
                            occurence=int(parts[1]) if parts[1].isdigit() else 0
                        )
                        data_list.append(item)
            csv_path = os.path.join(base_local, f"Statistiques_Localisation_{numero}.csv")
            CsvFileWriterORANGE.write_statistiques_localisation(csv_path, data_list)
        except Exception as e:
            print(f"Error reading statistiques localisation Orange: {e}")

    def _read_freq_cellule(self, base_remote, base_local, numero):
        try:
            content = sftp_read_file(self.host, self.user, self.password,
                                     f"{base_remote}/frequenceCellule.txt")
            data_list = []
            for line in content.strip().split('\n'):
                if line.strip():
                    parts = line.split(',')
                    if len(parts) >= 14:
                        item = FreqCell(
                            total=parts[0], cellule=parts[1],
                            zero_deux=parts[2], deux_quatre=parts[3],
                            quatre_six=parts[4], six_huit=parts[5],
                            huit_dix=parts[6], dix_douze=parts[7],
                            douze_quatorze=parts[8], quatorze_seize=parts[9],
                            seize_dixhuit=parts[10], dixhuit_vingt=parts[11],
                            vingt_vingtdeux=parts[12], vingtdeux_vingtquatre=parts[13]
                        )
                        data_list.append(item)
            csv_path = os.path.join(base_local, f"frequenceCellule_{numero}.csv")
            CsvFileWriterORANGEMultiple.write_freq_cellule(csv_path, data_list)
        except Exception as e:
            print(f"Error reading freq cellule Orange: {e}")

    def _read_freq_correspondant(self, base_remote, base_local, numero):
        try:
            content = sftp_read_file(self.host, self.user, self.password,
                                     f"{base_remote}/frequenceCorrespondance.txt")
            data_list = []
            for line in content.strip().split('\n'):
                if line.strip():
                    parts = line.split(',')
                    if len(parts) >= 17:
                        item = FreqCorrespondant(
                            total=parts[0], total_entrant=parts[1], total_sortant=parts[2],
                            telephone=parts[3], identite=parts[4],
                            zero_deux=parts[5], deux_quatre=parts[6],
                            quatre_six=parts[7], six_huit=parts[8],
                            huit_dix=parts[9], dix_douze=parts[10],
                            douze_quatorze=parts[11], quatorze_seize=parts[12],
                            seize_dixhuit=parts[13], dixhuit_vingt=parts[14],
                            vingt_vingtdeux=parts[15], vingtdeux_vingtquatre=parts[16]
                        )
                        data_list.append(item)
            csv_path = os.path.join(base_local, f"frequenceCorrespondance_{numero}.csv")
            CsvFileWriterORANGEMultiple.write_freq_correspondant(csv_path, data_list)
        except Exception as e:
            print(f"Error: {e}")

    def _read_freq_duree_appel(self, base_remote, base_local, numero):
        try:
            content = sftp_read_file(self.host, self.user, self.password,
                                     f"{base_remote}/frequenceDureeAppel.txt")
            data_list = []
            for line in content.strip().split('\n'):
                if line.strip():
                    parts = line.split(',')
                    if len(parts) >= 4:
                        item = FreqDureeAppel(
                            numero=parts[0], identite=parts[1],
                            duree_appel=parts[2], nombre_message=parts[3]
                        )
                        data_list.append(item)
            csv_path = os.path.join(base_local, f"frequenceDureeAppel_{numero}.csv")
            CsvFileWriterORANGEMultiple.write_freq_duree_appel(csv_path, data_list)
        except Exception as e:
            print(f"Error: {e}")

    def _read_freq_imei(self, base_remote, base_local, numero):
        try:
            content = sftp_read_file(self.host, self.user, self.password,
                                     f"{base_remote}/frequenceImei.txt")
            data_list = []
            for line in content.strip().split('\n'):
                if line.strip():
                    parts = line.split(',')
                    if len(parts) >= 4:
                        item = FreqImei(
                            total=parts[0], imei=parts[1],
                            first_use=parts[2], last_use=parts[3]
                        )
                        data_list.append(item)
            csv_path = os.path.join(base_local, f"frequenceImei_{numero}.csv")
            CsvFileWriterORANGEMultiple.write_freq_imei(csv_path, data_list)
        except Exception as e:
            print(f"Error: {e}")

    def _read_shared_imei(self, base_remote, base_local, numero):
        try:
            content = sftp_read_file(self.host, self.user, self.password,
                                     f"{base_remote}/sharedImei.txt")
            data_list = []
            for line in content.strip().split('\n'):
                if line.strip():
                    parts = line.split(',')
                    if len(parts) >= 6:
                        item = SharedImei(
                            numero=parts[0], imei=parts[1], identite=parts[2],
                            occurrence=parts[3], first_use=parts[4], last_use=parts[5]
                        )
                        data_list.append(item)
            csv_path = os.path.join(base_local, f"sharedImei_{numero}.csv")
            CsvFileWriterORANGEMultiple.write_shared_imei(csv_path, data_list)
        except Exception as e:
            print(f"Error: {e}")
