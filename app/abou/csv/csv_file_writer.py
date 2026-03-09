import csv
import os


class CsvFileWriterMTN:
    @staticmethod
    def write_id_numero(file_path, data_list):
        """Write identification numero CSV for MTN"""
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["Numero", "Nom et Prenom", "Date Naissance", "Numero CNI",
                           "Date Exp CNI", "Quartier", "Nationalite"])
            for item in data_list:
                writer.writerow([item.numero, item.nom_prenom, item.date_naissance,
                               item.numero_cni, item.date_exp_cni, item.quartier, item.nationalite])

    @staticmethod
    def write_listing(file_path, data_list):
        """Write listing CSV for MTN"""
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["NumeroAppelant", "LocalisationNumeroAppelant",
                           "IMEINumeroAppelant", "DateDebutAppel", "DureeAppel", "NumeroAppele"])
            for item in data_list:
                writer.writerow([item.numero_appelant, item.localisation_numero_appelant,
                               item.imei_numero_appelant, item.date_debut_appel,
                               item.duree_appel, item.numero_appele])

    @staticmethod
    def write_identification_abonnees(file_path, data_list):
        """Write identification abonnees CSV for MTN"""
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["Numero", "Nom et Prenom", "Date Naissance", "Numero CNI",
                           "Date Exp CNI", "Quartier", "Nationalite"])
            for item in data_list:
                writer.writerow([item.numero, item.nom_prenom, item.date_naissance,
                               item.numero_cni, item.date_exp_cni, item.quartier, item.nationalite])

    @staticmethod
    def write_statistiques(file_path, data_list):
        """Write statistiques appels CSV for MTN"""
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["NumeroAppelant", "Occurence", "DureeAppel"])
            for item in data_list:
                writer.writerow([item.numero_appelant, item.occurence, item.duree_appel])

    @staticmethod
    def write_statistiques_localisation(file_path, data_list):
        """Write statistiques localisation CSV for MTN"""
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["Localisation", "Occurence"])
            for item in data_list:
                writer.writerow([item.localisation, item.occurence])


class CsvFileWriterMTNMultiple(CsvFileWriterMTN):
    @staticmethod
    def write_freq_cellule(file_path, data_list):
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["Total", "Cellule", "00h-02h", "02h-04h", "04h-06h", "06h-08h",
                           "08h-10h", "10h-12h", "12h-14h", "14h-16h", "16h-18h", "18h-20h",
                           "20h-22h", "22h-24h"])
            for item in data_list:
                writer.writerow([item.total, item.cellule, item.zero_deux, item.deux_quatre,
                               item.quatre_six, item.six_huit, item.huit_dix, item.dix_douze,
                               item.douze_quatorze, item.quatorze_seize, item.seize_dixhuit,
                               item.dixhuit_vingt, item.vingt_vingtdeux, item.vingtdeux_vingtquatre])

    @staticmethod
    def write_freq_correspondant(file_path, data_list):
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["Total", "Total Entrant", "Total Sortant", "Telephone", "Identite",
                           "00h-02h", "02h-04h", "04h-06h", "06h-08h", "08h-10h", "10h-12h",
                           "12h-14h", "14h-16h", "16h-18h", "18h-20h", "20h-22h", "22h-24h"])
            for item in data_list:
                writer.writerow([item.total, item.total_entrant, item.total_sortant,
                               item.telephone, item.identite, item.zero_deux, item.deux_quatre,
                               item.quatre_six, item.six_huit, item.huit_dix, item.dix_douze,
                               item.douze_quatorze, item.quatorze_seize, item.seize_dixhuit,
                               item.dixhuit_vingt, item.vingt_vingtdeux, item.vingtdeux_vingtquatre])

    @staticmethod
    def write_freq_duree_appel(file_path, data_list):
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["Numero", "Identite", "Duree Appel", "Nombre Message"])
            for item in data_list:
                writer.writerow([item.numero, item.identite, item.duree_appel, item.nombre_message])

    @staticmethod
    def write_freq_imei(file_path, data_list):
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["Total", "IMEI", "First Use", "Last Use"])
            for item in data_list:
                writer.writerow([item.total, item.imei, item.first_use, item.last_use])

    @staticmethod
    def write_shared_imei(file_path, data_list):
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["Numero", "IMEI", "Identite", "Occurrence", "First Use", "Last Use"])
            for item in data_list:
                writer.writerow([item.numero, item.imei, item.identite,
                               item.occurrence, item.first_use, item.last_use])


class CsvFileWriterORANGE:
    @staticmethod
    def write_id_numero(file_path, data_list):
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["Numero", "Nom et Prenom", "Date Naissance", "Numero CNI",
                           "Date Exp CNI", "Quartier"])
            for item in data_list:
                writer.writerow([item.numero, item.nom_prenom, item.date_naissance,
                               item.numero_cni, item.date_exp_cni, item.quartier])

    @staticmethod
    def write_listing_emis(file_path, data_list):
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["NumeroAppelant", "LocalisationNumeroAppelant",
                           "IMEINumeroAppelant", "DateDebutAppel", "DureeAppel", "NumeroAppele"])
            for item in data_list:
                writer.writerow([item.numero_appelant, item.localisation_numero_appelant,
                               item.imei_numero_appelant, item.date_debut_appel,
                               item.duree_appel, item.numero_appele])

    @staticmethod
    def write_listing_sms(file_path, data_list):
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["NumeroEnvoi", "LocalisationNumeroDest", "IMEINumeroDest",
                           "DateSMS", "NumeroDest"])
            for item in data_list:
                writer.writerow([item.numero_envoi, item.localisation_numero_dest,
                               item.imei_numero_dest, item.date_sms, item.numero_dest])

    @staticmethod
    def write_identification_abonnees(file_path, data_list):
        CsvFileWriterORANGE.write_id_numero(file_path, data_list)

    @staticmethod
    def write_statistiques(file_path, data_list):
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["NumeroAppelant", "Occurence", "DureeAppel"])
            for item in data_list:
                writer.writerow([item.numero_appelant, item.occurence, item.duree_appel])

    @staticmethod
    def write_statistiques_localisation(file_path, data_list):
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["Localisation", "Occurence"])
            for item in data_list:
                writer.writerow([item.localisation, item.occurence])


class CsvFileWriterORANGEMultiple(CsvFileWriterORANGE):
    @staticmethod
    def write_freq_cellule(file_path, data_list):
        CsvFileWriterMTNMultiple.write_freq_cellule(file_path, data_list)

    @staticmethod
    def write_freq_correspondant(file_path, data_list):
        CsvFileWriterMTNMultiple.write_freq_correspondant(file_path, data_list)

    @staticmethod
    def write_freq_duree_appel(file_path, data_list):
        CsvFileWriterMTNMultiple.write_freq_duree_appel(file_path, data_list)

    @staticmethod
    def write_freq_imei(file_path, data_list):
        CsvFileWriterMTNMultiple.write_freq_imei(file_path, data_list)

    @staticmethod
    def write_shared_imei(file_path, data_list):
        CsvFileWriterMTNMultiple.write_shared_imei(file_path, data_list)


class CsvFileWriterNEXTTEL:
    @staticmethod
    def write_id_numero(file_path, data_list):
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["Numero", "Nom et Prenom", "Date Naissance", "Numero CNI", "Date Exp CNI"])
            for item in data_list:
                writer.writerow([item.numero, item.nom_prenom, item.date_naissance,
                               item.numero_cni, item.date_exp_cni])

    @staticmethod
    def write_listing(file_path, data_list):
        CsvFileWriterMTN.write_listing(file_path, data_list)

    @staticmethod
    def write_identification_abonnees(file_path, data_list):
        CsvFileWriterNEXTTEL.write_id_numero(file_path, data_list)

    @staticmethod
    def write_statistiques(file_path, data_list):
        CsvFileWriterMTN.write_statistiques(file_path, data_list)

    @staticmethod
    def write_statistiques_localisation(file_path, data_list):
        CsvFileWriterMTN.write_statistiques_localisation(file_path, data_list)


class CsvFileWriterNEXTTELMultiple(CsvFileWriterNEXTTEL):
    @staticmethod
    def write_freq_cellule(file_path, data_list):
        CsvFileWriterMTNMultiple.write_freq_cellule(file_path, data_list)

    @staticmethod
    def write_freq_correspondant(file_path, data_list):
        CsvFileWriterMTNMultiple.write_freq_correspondant(file_path, data_list)

    @staticmethod
    def write_freq_duree_appel(file_path, data_list):
        CsvFileWriterMTNMultiple.write_freq_duree_appel(file_path, data_list)

    @staticmethod
    def write_freq_imei(file_path, data_list):
        CsvFileWriterMTNMultiple.write_freq_imei(file_path, data_list)

    @staticmethod
    def write_shared_imei(file_path, data_list):
        CsvFileWriterMTNMultiple.write_shared_imei(file_path, data_list)
