import csv
import os


def write_id_numero_orange(file_path, identifications):
    """Write identification CSV for Orange"""
    with open(file_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['Numero', 'NomPrenom', 'DateNaissance', 'NumeroCNI', 'DateExpCNI', 'Quartier'])
        for ident in identifications:
            writer.writerow([ident.numero, ident.nom_prenom, ident.date_naissance,
                           ident.numero_cni, ident.date_exp_cni, ident.quartier])


def write_listing_orange(file_path, listings):
    """Write listing CSV for Orange"""
    with open(file_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['NumeroAppelant', 'LocalisationNumeroAppelant', 'IMEINumeroAppelant',
                        'DateDebutAppel', 'DureeAppel', 'NumeroAppele'])
        for listing in listings:
            writer.writerow([listing.numero_appelant, listing.localisation_numero_appelant,
                           listing.imei_numero_appelant, listing.date_debut_appel,
                           listing.duree_appel, listing.numero_appele])


def write_listing_sms_orange(file_path, listings):
    """Write SMS listing CSV for Orange"""
    with open(file_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['NumeroEnvoi', 'LocalisationNumeroDest', 'IMEINumeroDest',
                        'DateSMS', 'NumeroDest'])
        for listing in listings:
            writer.writerow([listing.numero_envoi, listing.localisation_numero_dest,
                           listing.imei_numero_dest, listing.date_sms,
                           listing.numero_dest])


def write_statistiques_orange(file_path, stats):
    """Write statistics CSV for Orange"""
    with open(file_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['NumeroAppelant', 'Occurence', 'DureeAppel'])
        for stat in stats:
            writer.writerow([stat.numero_appelant, stat.occurence, stat.duree_appel])


def write_statistiques_localisation_orange(file_path, stats):
    """Write location statistics CSV for Orange"""
    with open(file_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['Localisation', 'Occurence'])
        for stat in stats:
            writer.writerow([stat.localisation, stat.occurence])


def write_identification_abonnees_orange(file_path, identifications):
    """Write subscriber identification CSV for Orange"""
    with open(file_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['Numero', 'NomPrenom', 'DateNaissance', 'NumeroCNI', 'DateExpCNI', 'Quartier'])
        for ident in identifications:
            writer.writerow([ident.numero, ident.nom_prenom, ident.date_naissance,
                           ident.numero_cni, ident.date_exp_cni, ident.quartier])
