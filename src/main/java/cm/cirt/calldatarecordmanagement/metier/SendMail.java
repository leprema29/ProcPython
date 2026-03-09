/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.metier;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Wanki
 */
public class SendMail {

    public static void sendMessage(String to, String Subject, String info) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.cirt.cm");
        props.put("mail.smtp.socketFactory.port", "25");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "25");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("h.wanki@cirt.cm", "Alu1234!@#$1990");
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@cirt.cm"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(Variables111.WANKI_EMAIL));
            message.setSubject(Subject);
            message.setText(info);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) {
        SendMail.sendMessage("h.wanki@cirt.cm", "Just a test", "Hello \n Just an email test.");
    }
}
