class IdentificationBean:
    def __init__(self, telephone="", name="", birthday=None, cni="",
                 expire_date=None, address="", status="", operator=""):
        self.telephone = telephone
        self.name = name
        self.birthday = birthday
        self.cni = cni
        self.expire_date = expire_date
        self.address = address
        self.status = status
        self.operator = operator
