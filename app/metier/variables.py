import os


class Variables:
    AUTH_KEY = "app.user.name"
    AUTH_STAT = "app.user.statut"

    BOSS_EMAIL = "prosper.pagou@cirt.cm"
    WANKI_EMAIL = "h.wanki@cirt.cm"
    WHITE_LIST_WARNING_SUBJECT = "White List Alert"

    ACCOUNT_TYPE = "DGSN-CAB"

    # Database configuration
    DB_HOST = "localhost"
    DB_PORT = 3306
    DB_NAME = "cdr_management_dgsn"
    DB_USER = "root"
    DB_PASSWORD = "Djamna@2019"

    # Paths - DGSN configuration
    BASE_FOLDER = "/home/cirt/Data/DGSN/"
    DESTINATION_FICHIER = "/home/cirt/Data/DGSN/requisitions/"
    DESTINATION_DOSSIERS = "/home/cirt/Data/DGSN/requisitions/"
    DESTINATION_LOGS = "/home/cirt/Data/DGSN/logs/"

    # SSH configuration
    ORANGE_HOST = "192.168.1.94"
    MTN_HOST = "192.168.1.94"
    NEXTTEL_HOST = "192.168.1.94"
    ORANGE_USER = "root"
    MTN_USER = "root"
    NEXTTEL_USER = "root"
    ORANGE_PASSWORD = "sidiar1234Bleu!@#$"
    MTN_PASSWORD = "sidiar1234Bleu!@#$"
    NEXTTEL_PASSWORD = "sidiar1234Bleu!@#$"
    SSH_KEY = "~/.ssh/private_key.pem"

    # Named folder paths
    DSP_BASE_FOLDER = "/home/cirt/Data/DSP/"
    BIR_BASE_FOLDER = "/home/cirt/Data/Bir/"
    DGSN_BASE_FOLDER = "/home/cirt/Data/DGSN/"
    SED_BASE_FOLDER = "/home/cirt/Data/SED/"

    @staticmethod
    def get_base_folder():
        home_dir = os.path.expanduser("~")
        return os.path.join(home_dir, "data") + "/"

    @staticmethod
    def get_destination_fichier():
        home_dir = os.path.expanduser("~")
        return os.path.join(home_dir, "data", "requisitions") + "/"

    @staticmethod
    def get_destination_dossiers():
        home_dir = os.path.expanduser("~")
        return os.path.join(home_dir, "data", "requisitions") + "/"

    @staticmethod
    def get_destination_logs():
        home_dir = os.path.expanduser("~")
        return os.path.join(home_dir, "data", "logs") + "/"

    @staticmethod
    def get_destination_logo():
        home_dir = os.path.expanduser("~")
        return os.path.join(home_dir, "data", "requisitions", "fichier") + "/"

    @staticmethod
    def get_id_rsa_path():
        home_dir = os.path.expanduser("~")
        potential_path = os.path.join(home_dir, ".ssh", "private_key.pem")
        if os.path.exists(potential_path):
            return os.path.abspath(potential_path)
        else:
            return Variables.SSH_KEY

    @staticmethod
    def get_known_host_path():
        home_dir = os.path.expanduser("~")
        potential_path = os.path.join(home_dir, ".ssh", "known_hosts")
        if os.path.exists(potential_path):
            return potential_path
        else:
            return Variables.SSH_KEY

    @staticmethod
    def get_file_content(file_path):
        try:
            with open(file_path, 'r') as f:
                return f.read()
        except Exception:
            return ""
