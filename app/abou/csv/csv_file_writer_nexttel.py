import csv
import os


def write_id_numero_nexttel(file_path, identifications):
    """Write identification CSV for Nexttel"""
    with open(file_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['Numero', 'NomPrenom', 'DateNaissance', 'NumeroCNI', 'DateExpCNI'])
        for ident in identifications:
            writer.writerow([ident.numero, ident.nom_prenom, ident.date_naissance,
                           ident.numero_cni, ident.date_exp_cni])


def write_listing_nexttel(file_path, listings):
    """Write listing CSV for Nexttel"""
    with open(file_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['NumeroAppelant', 'LocalisationNumeroAppelant', 'IMEINumeroAppelant',
                        'DateDebutAppel', 'DureeAppel', 'NumeroAppele'])
        for listing in listings:
            writer.writerow([listing.numero_appelant, listing.localisation_numero_appelant,
                           listing.imei_numero_appelant, listing.date_debut_appel,
                           listing.duree_appel, listing.numero_appele])


def write_statistiques_nexttel(file_path, stats):
    """Write statistics CSV for Nexttel"""
    with open(file_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['NumeroAppelant', 'Occurence', 'DureeAppel'])
        for stat in stats:
            writer.writerow([stat.numero_appelant, stat.occurence, stat.duree_appel])


def write_statistiques_localisation_nexttel(file_path, stats):
    """Write location statistics CSV for Nexttel"""
    with open(file_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['Localisation', 'Occurence'])
        for stat in stats:
            writer.writerow([stat.localisation, stat.occurence])


def write_identification_abonnees_nexttel(file_path, identifications):
    """Write subscriber identification CSV for Nexttel"""
    with open(file_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['Numero', 'NomPrenom', 'DateNaissance', 'NumeroCNI', 'DateExpCNI'])
        for ident in identifications:
            writer.writerow([ident.numero, ident.nom_prenom, ident.date_naissance,
                           ident.numero_cni, ident.date_exp_cni])
