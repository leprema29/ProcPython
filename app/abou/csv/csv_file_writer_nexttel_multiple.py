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


def write_freq_cellule(file_path, freq_cells):
    """Write frequency by cell CSV"""
    with open(file_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['Total', 'Cellule', '0h-2h', '2h-4h', '4h-6h', '6h-8h', '8h-10h', '10h-12h',
                        '12h-14h', '14h-16h', '16h-18h', '18h-20h', '20h-22h', '22h-24h'])
        for fc in freq_cells:
            writer.writerow([fc.total, fc.cellule, fc.zero_deux, fc.deux_quatre, fc.quatre_six,
                           fc.six_huit, fc.huit_dix, fc.dix_douze, fc.douze_quatorze,
                           fc.quatorze_seize, fc.seize_dixhuit, fc.dixhuit_vingt,
                           fc.vingt_vingtdeux, fc.vingtdeux_vingtquatre])


def write_freq_correspondant(file_path, freq_cors):
    """Write frequency by correspondent CSV"""
    with open(file_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['Total', 'TotalEntrant', 'TotalSortant', 'Telephone', 'Identite',
                        '0h-2h', '2h-4h', '4h-6h', '6h-8h', '8h-10h', '10h-12h',
                        '12h-14h', '14h-16h', '16h-18h', '18h-20h', '20h-22h', '22h-24h'])
        for fc in freq_cors:
            writer.writerow([fc.total, fc.total_entrant, fc.total_sortant, fc.telephone, fc.identite,
                           fc.zero_deux, fc.deux_quatre, fc.quatre_six, fc.six_huit,
                           fc.huit_dix, fc.dix_douze, fc.douze_quatorze, fc.quatorze_seize,
                           fc.seize_dixhuit, fc.dixhuit_vingt, fc.vingt_vingtdeux,
                           fc.vingtdeux_vingtquatre])


def write_freq_duree_appel(file_path, freq_durees):
    """Write frequency by call duration CSV"""
    with open(file_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['Numero', 'Identite', 'DureeAppel', 'NombreMessage'])
        for fd in freq_durees:
            writer.writerow([fd.numero, fd.identite, fd.duree_appel, fd.nombre_message])


def write_freq_imei(file_path, freq_imeis):
    """Write frequency by IMEI CSV"""
    with open(file_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['Total', 'IMEI', 'FirstUse', 'LastUse'])
        for fi in freq_imeis:
            writer.writerow([fi.total, fi.imei, fi.first_use, fi.last_use])


def write_shared_imei(file_path, shared_imeis):
    """Write shared IMEI CSV"""
    with open(file_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['Numero', 'IMEI', 'Identite', 'Occurrence', 'FirstUse', 'LastUse'])
        for si in shared_imeis:
            writer.writerow([si.numero, si.imei, si.identite, si.occurrence,
                           si.first_use, si.last_use])
