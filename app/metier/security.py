import hashlib
from datetime import datetime, date
import calendar


def md5(text):
    if text is not None:
        return hashlib.md5(text.encode()).hexdigest()
    return None


def double_to_int(mon_double):
    l_val = round(mon_double)
    try:
        return int(l_val)
    except (ValueError, OverflowError):
        if l_val < 0:
            return -(2**31)
        else:
            return 2**31 - 1


def get_date_from_string(string_date):
    try:
        tab = string_date.split(" ")
        tab2 = tab[0].split("-")
        year = int(tab2[0]) + 2000
        month = int(tab2[1])
        day = int(tab2[2])
        hrs = int(tab[1].replace("h", ""))
        mins = int(tab[2].replace("mm", ""))
        sec = int(tab[3].replace("s", ""))
        return datetime(year, month, day, hrs, mins, sec)
    except Exception:
        return None


def split_to_component_times(seconds):
    p1 = seconds % 60
    p2 = seconds // 60
    p3 = p2 % 60
    p2 = p2 // 60
    return f"{p2}:{p3}:{p1}"
