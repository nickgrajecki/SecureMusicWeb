/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SecurityClasses;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Nick
 */
public class LockEmail {

    //Set up email details
    final String emUsername = "securemusicweb@gmail.com";
    final String emPassword = "WenjiaWang2018";
    Properties emailProperties = new Properties();

    public void initialize() {
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        emailProperties.put("mail.smtp.host", "smtp.gmail.com");
        emailProperties.put("mail.smtp.port", "587");
    }

    public void changePassEmail(String resetEmail, String resetUser, String newPass) throws AddressException, MessagingException {
        Session session = Session.getInstance(emailProperties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emUsername, emPassword);
            }
        });

        //Send email with new password (unhashed)
        Message emailMessage = new MimeMessage(session);
        emailMessage.setFrom(new InternetAddress("securemusicweb@gmail.com"));
        emailMessage.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(resetEmail));
        emailMessage.setSubject("You requested a password reset");
        emailMessage.setText("Dear " + resetUser
                + ",\n\nYou have recently requested a password reset.\n"
                + "Please use this to log in and change your password \n\n"
                + "Your temporary password is: \n" + newPass
                + "\nIf you hadn't requested a password reset, "
                + "please contact the website administrator.\n\n - SecureMusicWeb");
        Transport.send(emailMessage);
    }
}
