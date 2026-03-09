import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from app.metier.variables import Variables


def send_message(to, subject, info):
    try:
        msg = MIMEMultipart()
        msg['From'] = "no-reply@cirt.cm"
        msg['To'] = to
        msg['Bcc'] = Variables.WANKI_EMAIL
        msg['Subject'] = subject
        msg.attach(MIMEText(info, 'plain'))

        server = smtplib.SMTP("mail.cirt.cm", 25)
        server.login("h.wanki@cirt.cm", "Alu1234!@#$1990")
        server.sendmail("no-reply@cirt.cm", [to, Variables.WANKI_EMAIL], msg.as_string())
        server.quit()
        print("Done")
    except Exception as e:
        raise RuntimeError(str(e))
