from dataclasses import dataclass, field
from datetime import datetime
from typing import Optional


@dataclass
class Listing:
    numero_appelant: str = ""
    localisation_numero_appelant: str = ""
    imei_numero_appelant: str = ""
    date_debut_appel: str = ""
    duree_appel: str = ""
    numero_appele: str = ""


@dataclass
class ListingOrangeSMS:
    numero_envoi: str = ""
    localisation_numero_dest: str = ""
    imei_numero_dest: str = ""
    date_sms: str = ""
    numero_dest: str = ""


@dataclass
class FreqCell:
    total: str = ""
    cellule: str = ""
    zero_deux: str = ""
    deux_quatre: str = ""
    quatre_six: str = ""
    six_huit: str = ""
    huit_dix: str = ""
    dix_douze: str = ""
    douze_quatorze: str = ""
    quatorze_seize: str = ""
    seize_dixhuit: str = ""
    dixhuit_vingt: str = ""
    vingt_vingtdeux: str = ""
    vingtdeux_vingtquatre: str = ""


@dataclass
class FreqCorrespondant:
    total: str = ""
    total_entrant: str = ""
    total_sortant: str = ""
    telephone: str = ""
    identite: str = ""
    zero_deux: str = ""
    deux_quatre: str = ""
    quatre_six: str = ""
    six_huit: str = ""
    huit_dix: str = ""
    dix_douze: str = ""
    douze_quatorze: str = ""
    quatorze_seize: str = ""
    seize_dixhuit: str = ""
    dixhuit_vingt: str = ""
    vingt_vingtdeux: str = ""
    vingtdeux_vingtquatre: str = ""


@dataclass
class FreqDureeAppel:
    numero: str = ""
    identite: str = ""
    duree_appel: str = ""
    nombre_message: str = ""


@dataclass
class FreqImei:
    total: str = ""
    imei: str = ""
    first_use: str = ""
    last_use: str = ""


@dataclass
class SharedImei:
    numero: str = ""
    imei: str = ""
    identite: str = ""
    occurrence: str = ""
    first_use: str = ""
    last_use: str = ""


@dataclass
class StatistiqueAppels:
    numero_appelant: str = ""
    occurence: int = 0
    duree_appel: str = ""


@dataclass
class StatistiqueLieux:
    localisation: str = ""
    occurence: int = 0


@dataclass
class IdentificationMTN:
    numero: str = ""
    nom_prenom: str = ""
    date_naissance: str = ""
    numero_cni: str = ""
    date_exp_cni: str = ""
    quartier: str = ""
    nationalite: str = ""


@dataclass
class IdentificationORANGE:
    numero: str = ""
    nom_prenom: str = ""
    date_naissance: str = ""
    numero_cni: str = ""
    date_exp_cni: str = ""
    quartier: str = ""


@dataclass
class IdentificationNEXTTEL:
    numero: str = ""
    nom_prenom: str = ""
    date_naissance: str = ""
    numero_cni: str = ""
    date_exp_cni: str = ""


@dataclass
class SheetAttributes:
    filename: str = ""
    sheetname: str = ""
