import os
from datetime import datetime
from app.metier.variables import Variables


class CirtLog:
    def __init__(self, timestamp=None, ip_address="", task="", user=None,
                 mac_address="", numbers="", begin_date=None, end_date=None, login=""):
        self.timestamp = timestamp or datetime.now()
        self.numbers = numbers
        self.begin_date = begin_date
        self.end_date = end_date
        self.ip_address = ip_address
        self.task = task
        self.user = user
        self.login = login
        self.mac_address = mac_address

    def create_listing_log(self):
        try:
            dt_format = "%Y-%m-%d %H:%M:%S"
            dt2_format = "%Y-%m-%d"
            user_name = self.user.name if self.user else ""
            begin_str = self.begin_date.strftime(dt2_format) if self.begin_date else ""
            end_str = self.end_date.strftime(dt2_format) if self.end_date else ""
            chaine = (f"{self.timestamp.strftime(dt_format)},{user_name},"
                      f"{self.task},{self.numbers},{begin_str},{end_str},"
                      f"{self.ip_address},{self.mac_address}")
            log_dir = Variables.DESTINATION_LOGS
            os.makedirs(log_dir, exist_ok=True)
            output_file = os.path.join(log_dir, f"log-{self.timestamp.strftime(dt2_format)}.txt")
            with open(output_file, 'a') as f:
                f.write(chaine + "\n")
        except Exception:
            pass

    def create_session_log(self):
        try:
            dt_format = "%Y-%m-%d %H:%M:%S"
            dt2_format = "%Y-%m-%d"
            user_name = self.user.name if self.user else ""
            chaine = (f"{self.timestamp.strftime(dt_format)},{user_name},"
                      f"{self.task},{self.ip_address},{self.mac_address}")
            log_dir = Variables.DESTINATION_LOGS
            os.makedirs(log_dir, exist_ok=True)
            output_file = os.path.join(log_dir, f"session-{self.timestamp.strftime(dt2_format)}.txt")
            with open(output_file, 'a') as f:
                f.write(chaine + "\n")
        except Exception:
            pass

    def create_failed_session_log(self):
        try:
            dt_format = "%Y-%m-%d %H:%M:%S"
            dt2_format = "%Y-%m-%d"
            chaine = (f"{self.timestamp.strftime(dt_format)},{self.login},"
                      f"{self.task},{self.ip_address},{self.mac_address}")
            log_dir = Variables.DESTINATION_LOGS
            os.makedirs(log_dir, exist_ok=True)
            output_file = os.path.join(log_dir, f"session-{self.timestamp.strftime(dt2_format)}.txt")
            with open(output_file, 'a') as f:
                f.write(chaine + "\n")
        except Exception:
            pass
