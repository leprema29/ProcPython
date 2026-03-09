import os
import csv
from app.metier.variables import Variables
from app.abou.ssh_utils import execute_remote_command, sftp_read_file, sftp_download_file
from app.abou.csv.format.entities import (
    Listing, IdentificationMTN, StatistiqueAppels, StatistiqueLieux,
    FreqCell, FreqCorrespondant, FreqDureeAppel, FreqImei, SharedImei
)
from app.abou.csv.csv_file_writer import CsvFileWriterMTN, CsvFileWriterMTNMultiple


class RemoteRequiMTN:
    def __init__(self, date_requisition, demandeur_requisition=""):
        self.date_requisition = date_requisition
        self.demandeur_requisition = demandeur_requisition
        self.host = Variables.MTN_HOST
        self.user = Variables.MTN_USER
        self.password = Variables.MTN_PASSWORD

    def remote_find_mtn(self, numero, date_debut, date_fin, identification):
        """Execute remote shell script for MTN single number"""
        command = (f"bash /home/data/mtn/operations/cdr/requisitionMTN_Multiple_Optimized.sh "
                   f"{numero} {date_debut} {date_fin} {self.date_requisition}")
        print(f"Executing: {command}")
        try:
            output = execute_remote_command(self.host, self.user, self.password, command)
            print(f"Command output: {output}")
        except Exception as e:
            print(f"Error executing remote command: {e}")

        # Read and process the result files
        self.lire_fic_mtn(numero)

    def remote_find_mtn_multiple(self, numeros, date_debut, date_fin, identification):
        """Execute remote shell script for MTN multiple numbers"""
        command = (f"bash /home/data/mtn/operations/cdr/requisitionMTN_Multiple_Optimized.sh "
                   f"{numeros} {date_debut} {date_fin} {self.date_requisition}")
        print(f"Executing: {command}")
        try:
            output = execute_remote_command(self.host, self.user, self.password, command)
            print(f"Command output: {output}")
        except Exception as e:
            print(f"Error executing remote command: {e}")

        numeros_clean = numeros.strip('"')
        for numero in numeros_clean.split():
            self.lire_fic_mtn_multiple(numero)

    def remote_find_mtn_imei_multiple(self, imeis, date_debut, date_fin, identification):
        """Execute remote shell script for MTN multiple IMEI"""
        command = (f"bash /home/data/mtn/operations/cdr/requisitionImeiMTN_Multiple_Optimized.sh "
                   f"{imeis} {date_debut} {date_fin} {self.date_requisition}")
        print(f"Executing: {command}")
        try:
            output = execute_remote_command(self.host, self.user, self.password, command)
            print(f"Command output: {output}")
        except Exception as e:
            print(f"Error executing remote command: {e}")

        imeis_clean = imeis.strip('"')
        for imei in imeis_clean.split():
            self.lire_fic_mtn_multiple(imei)

    def lire_fic_mtn(self, numero):
        """Read remote files and generate local CSV files for single MTN number"""
        base_remote = f"/root/{self.date_requisition}/{numero}"
        base_local = os.path.join(Variables.DESTINATION_DOSSIERS, self.date_requisition, numero)
        os.makedirs(base_local, exist_ok=True)

        # Read identity file
        self._read_and_write_id_numero(base_remote, base_local, numero)
        # Read listing file
        self._read_and_write_listing(base_remote, base_local, numero)
        # Read identification abonnees
        self._read_and_write_identification_abonnees(base_remote, base_local, numero)
        # Read statistiques
        self._read_and_write_statistiques(base_remote, base_local, numero)
        # Read statistiques localisation
        self._read_and_write_statistiques_localisation(base_remote, base_local, numero)

    def lire_fic_mtn_multiple(self, numero):
        """Read remote files for multiple MTN - includes frequency data"""
        base_remote = f"/root/{self.date_requisition}/{numero}"
        base_local = os.path.join(Variables.DESTINATION_DOSSIERS, self.date_requisition, numero)
        os.makedirs(base_local, exist_ok=True)

        self._read_and_write_id_numero(base_remote, base_local, numero)
        self._read_and_write_listing(base_remote, base_local, numero)
        self._read_and_write_identification_abonnees(base_remote, base_local, numero)
        self._read_and_write_statistiques(base_remote, base_local, numero)
        self._read_and_write_statistiques_localisation(base_remote, base_local, numero)
        # Multiple-specific files
        self._read_and_write_freq_cellule(base_remote, base_local, numero)
        self._read_and_write_freq_correspondant(base_remote, base_local, numero)
        self._read_and_write_freq_duree_appel(base_remote, base_local, numero)
        self._read_and_write_freq_imei(base_remote, base_local, numero)
        self._read_and_write_shared_imei(base_remote, base_local, numero)

    def _read_and_write_id_numero(self, base_remote, base_local, numero):
        try:
            content = sftp_read_file(self.host, self.user, self.password,
                                     f"{base_remote}/identite_numero.txt")
            data_list = []
            for line in content.strip().split('\n'):
                if line.strip():
                    parts = line.split(',')
                    if len(parts) >= 7:
                        item = IdentificationMTN(
                            numero=parts[0], nom_prenom=parts[1], date_naissance=parts[2],
                            numero_cni=parts[3], date_exp_cni=parts[4],
                            quartier=parts[5], nationalite=parts[6]
                        )
                        data_list.append(item)
            csv_path = os.path.join(base_local, f"Requisition_IdNumero_{numero}.csv")
            CsvFileWriterMTN.write_id_numero(csv_path, data_list)
        except Exception as e:
            print(f"Error reading id_numero: {e}")

    def _read_and_write_listing(self, base_remote, base_local, numero):
        try:
            content = sftp_read_file(self.host, self.user, self.password,
                                     f"{base_remote}/appelemis.txt")
            data_list = []
            for line in content.strip().split('\n'):
                if line.strip():
                    parts = line.split(',')
                    if len(parts) >= 6:
                        item = Listing(
                            numero_appelant=parts[0],
                            localisation_numero_appelant=parts[1],
                            imei_numero_appelant=parts[2],
                            date_debut_appel=parts[3],
                            duree_appel=parts[4],
                            numero_appele=parts[5]
                        )
                        data_list.append(item)
            csv_path = os.path.join(base_local, f"Requisition_Listing_{numero}.csv")
            CsvFileWriterMTN.write_listing(csv_path, data_list)
        except Exception as e:
            print(f"Error reading listing: {e}")

    def _read_and_write_identification_abonnees(self, base_remote, base_local, numero):
        try:
            content = sftp_read_file(self.host, self.user, self.password,
                                     f"{base_remote}/IdentificationAbonneesfinal.txt")
            data_list = []
            for line in content.strip().split('\n'):
                if line.strip():
                    parts = line.split(',')
                    if len(parts) >= 7:
                        item = IdentificationMTN(
                            numero=parts[0], nom_prenom=parts[1], date_naissance=parts[2],
                            numero_cni=parts[3], date_exp_cni=parts[4],
                            quartier=parts[5], nationalite=parts[6]
                        )
                        data_list.append(item)
            csv_path = os.path.join(base_local, f"IdentificationAbonnees_{numero}.csv")
            CsvFileWriterMTN.write_identification_abonnees(csv_path, data_list)
        except Exception as e:
            print(f"Error reading identification abonnees: {e}")

    def _read_and_write_statistiques(self, base_remote, base_local, numero):
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
            CsvFileWriterMTN.write_statistiques(csv_path, data_list)
        except Exception as e:
            print(f"Error reading statistiques: {e}")

    def _read_and_write_statistiques_localisation(self, base_remote, base_local, numero):
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
            CsvFileWriterMTN.write_statistiques_localisation(csv_path, data_list)
        except Exception as e:
            print(f"Error reading statistiques localisation: {e}")

    def _read_and_write_freq_cellule(self, base_remote, base_local, numero):
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
            CsvFileWriterMTNMultiple.write_freq_cellule(csv_path, data_list)
        except Exception as e:
            print(f"Error reading freq cellule: {e}")

    def _read_and_write_freq_correspondant(self, base_remote, base_local, numero):
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
            CsvFileWriterMTNMultiple.write_freq_correspondant(csv_path, data_list)
        except Exception as e:
            print(f"Error reading freq correspondant: {e}")

    def _read_and_write_freq_duree_appel(self, base_remote, base_local, numero):
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
            CsvFileWriterMTNMultiple.write_freq_duree_appel(csv_path, data_list)
        except Exception as e:
            print(f"Error reading freq duree appel: {e}")

    def _read_and_write_freq_imei(self, base_remote, base_local, numero):
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
            CsvFileWriterMTNMultiple.write_freq_imei(csv_path, data_list)
        except Exception as e:
            print(f"Error reading freq imei: {e}")

    def _read_and_write_shared_imei(self, base_remote, base_local, numero):
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
            CsvFileWriterMTNMultiple.write_shared_imei(csv_path, data_list)
        except Exception as e:
            print(f"Error reading shared imei: {e}")
