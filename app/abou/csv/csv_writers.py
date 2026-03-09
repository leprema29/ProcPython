"""CSV file writers for all operators - single and multiple."""
import csv
import os


class CsvFileWriterMTN:
    @staticmethod
    def write_id_numero(file_path, data_list):
        """Write identification numero CSV."""
        try:
            os.makedirs(os.path.dirname(file_path), exist_ok=True)
            with open(file_path, 'w', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                writer.writerow(["Numero", "NomPrenom", "DateNaissance",
                                "NumeroCNI", "DateExpCNI", "Quartier", "Nationalite"])
                for item in data_list:
                    writer.writerow([item.numero, item.nom_prenom, item.date_naissance,
                                   item.numero_cni, item.date_exp_cni, item.quartier,
                                   item.nationalite])
        except Exception as e:
            import traceback
            traceback.print_exc()

    @staticmethod
    def write_listing(file_path, data_list):
        """Write listing CSV."""
        try:
            os.makedirs(os.path.dirname(file_path), exist_ok=True)
            with open(file_path, 'w', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                writer.writerow(["NumeroAppelant", "LocalisationNumeroAppelant",
                                "IMEINumeroAppelant", "DateDebutAppel",
                                "DureeAppel", "NumeroAppele"])
                for item in data_list:
                    writer.writerow([item.numero_appelant, item.localisation_numero_appelant,
                                   item.imei_numero_appelant, item.date_debut_appel,
                                   item.duree_appel, item.numero_appele])
        except Exception as e:
            import traceback
            traceback.print_exc()

    @staticmethod
    def write_identification_abonnees(file_path, data_list):
        """Write subscriber identification CSV."""
        try:
            os.makedirs(os.path.dirname(file_path), exist_ok=True)
            with open(file_path, 'w', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                writer.writerow(["Numero", "NomPrenom", "DateNaissance",
                                "NumeroCNI", "DateExpCNI", "Quartier", "Nationalite"])
                for item in data_list:
                    writer.writerow([item.numero, item.nom_prenom, item.date_naissance,
                                   item.numero_cni, item.date_exp_cni, item.quartier,
                                   item.nationalite])
        except Exception as e:
            import traceback
            traceback.print_exc()

    @staticmethod
    def write_statistiques(file_path, data_list):
        """Write statistics CSV."""
        try:
            os.makedirs(os.path.dirname(file_path), exist_ok=True)
            with open(file_path, 'w', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                writer.writerow(["NumeroAppelant", "Occurence", "DureeAppel"])
                for item in data_list:
                    writer.writerow([item.numero_appelant, item.occurence, item.duree_appel])
        except Exception as e:
            import traceback
            traceback.print_exc()

    @staticmethod
    def write_statistiques_lieux(file_path, data_list):
        """Write location statistics CSV."""
        try:
            os.makedirs(os.path.dirname(file_path), exist_ok=True)
            with open(file_path, 'w', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                writer.writerow(["Localisation", "Occurence"])
                for item in data_list:
                    writer.writerow([item.localisation, item.occurence])
        except Exception as e:
            import traceback
            traceback.print_exc()


class CsvFileWriterMTNMultiple(CsvFileWriterMTN):
    @staticmethod
    def write_freq_cellule(file_path, data_list):
        try:
            os.makedirs(os.path.dirname(file_path), exist_ok=True)
            with open(file_path, 'w', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                writer.writerow(["Total", "Cellule", "00h-02h", "02h-04h", "04h-06h",
                                "06h-08h", "08h-10h", "10h-12h", "12h-14h", "14h-16h",
                                "16h-18h", "18h-20h", "20h-22h", "22h-00h"])
                for item in data_list:
                    writer.writerow([item.total, item.cellule, item.zero_deux, item.deux_quatre,
                                   item.quatre_six, item.six_huit, item.huit_dix, item.dix_douze,
                                   item.douze_quatorze, item.quatorze_seize, item.seize_dixhuit,
                                   item.dixhuit_vingt, item.vingt_vingtdeux, item.vingtdeux_vingtquatre])
        except Exception:
            import traceback
            traceback.print_exc()

    @staticmethod
    def write_freq_correspondant(file_path, data_list):
        try:
            os.makedirs(os.path.dirname(file_path), exist_ok=True)
            with open(file_path, 'w', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                writer.writerow(["Total", "TotalEntrant", "TotalSortant", "Telephone", "Identite",
                                "00h-02h", "02h-04h", "04h-06h", "06h-08h", "08h-10h", "10h-12h",
                                "12h-14h", "14h-16h", "16h-18h", "18h-20h", "20h-22h", "22h-00h"])
                for item in data_list:
                    writer.writerow([item.total, item.total_entrant, item.total_sortant,
                                   item.telephone, item.identite, item.zero_deux, item.deux_quatre,
                                   item.quatre_six, item.six_huit, item.huit_dix, item.dix_douze,
                                   item.douze_quatorze, item.quatorze_seize, item.seize_dixhuit,
                                   item.dixhuit_vingt, item.vingt_vingtdeux, item.vingtdeux_vingtquatre])
        except Exception:
            import traceback
            traceback.print_exc()

    @staticmethod
    def write_freq_duree_appel(file_path, data_list):
        try:
            os.makedirs(os.path.dirname(file_path), exist_ok=True)
            with open(file_path, 'w', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                writer.writerow(["Numero", "Identite", "DureeAppel", "NombreMessage"])
                for item in data_list:
                    writer.writerow([item.numero, item.identite, item.duree_appel, item.nombre_message])
        except Exception:
            import traceback
            traceback.print_exc()

    @staticmethod
    def write_freq_imei(file_path, data_list):
        try:
            os.makedirs(os.path.dirname(file_path), exist_ok=True)
            with open(file_path, 'w', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                writer.writerow(["Total", "IMEI", "PremierUtilisation", "DernierUtilisation"])
                for item in data_list:
                    writer.writerow([item.total, item.imei, item.first_use, item.last_use])
        except Exception:
            import traceback
            traceback.print_exc()

    @staticmethod
    def write_shared_imei(file_path, data_list):
        try:
            os.makedirs(os.path.dirname(file_path), exist_ok=True)
            with open(file_path, 'w', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                writer.writerow(["Numero", "IMEI", "Identite", "Occurrence", "PremierUtilisation", "DernierUtilisation"])
                for item in data_list:
                    writer.writerow([item.numero, item.imei, item.identite, item.occurrence,
                                   item.first_use, item.last_use])
        except Exception:
            import traceback
            traceback.print_exc()


class CsvFileWriterORANGE(CsvFileWriterMTN):
    @staticmethod
    def write_listing_sms(file_path, data_list):
        try:
            os.makedirs(os.path.dirname(file_path), exist_ok=True)
            with open(file_path, 'w', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                writer.writerow(["NumeroEnvoi", "LocalisationNumeroDest",
                                "IMEINumeroDest", "DateSMS", "NumeroDest"])
                for item in data_list:
                    writer.writerow([item.numero_envoi, item.localisation_numero_dest,
                                   item.imei_numero_dest, item.date_sms, item.numero_dest])
        except Exception:
            import traceback
            traceback.print_exc()

    @staticmethod
    def write_id_numero(file_path, data_list):
        try:
            os.makedirs(os.path.dirname(file_path), exist_ok=True)
            with open(file_path, 'w', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                writer.writerow(["Numero", "NomPrenom", "DateNaissance",
                                "NumeroCNI", "DateExpCNI", "Quartier"])
                for item in data_list:
                    writer.writerow([item.numero, item.nom_prenom, item.date_naissance,
                                   item.numero_cni, item.date_exp_cni, item.quartier])
        except Exception:
            import traceback
            traceback.print_exc()


class CsvFileWriterORANGEMultiple(CsvFileWriterORANGE, CsvFileWriterMTNMultiple):
    pass


class CsvFileWriterNEXTTEL(CsvFileWriterMTN):
    @staticmethod
    def write_id_numero(file_path, data_list):
        try:
            os.makedirs(os.path.dirname(file_path), exist_ok=True)
            with open(file_path, 'w', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                writer.writerow(["Numero", "NomPrenom", "DateNaissance",
                                "NumeroCNI", "DateExpCNI"])
                for item in data_list:
                    writer.writerow([item.numero, item.nom_prenom, item.date_naissance,
                                   item.numero_cni, item.date_exp_cni])
        except Exception:
            import traceback
            traceback.print_exc()


class CsvFileWriterNEXTTELMultiple(CsvFileWriterNEXTTEL, CsvFileWriterMTNMultiple):
    pass
