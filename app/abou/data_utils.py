"""Utility functions for cleaning data from remote scripts."""


def clean_null(value):
    """Replace literal 'null' strings with empty string."""
    v = value.strip()
    return "" if v.lower() == "null" else v


def clean_phone(value):
    """Clean phone number: remove 237 prefix and null."""
    v = clean_null(value)
    if v.startswith("237") and len(v) > 9:
        v = v[3:]
    return v
