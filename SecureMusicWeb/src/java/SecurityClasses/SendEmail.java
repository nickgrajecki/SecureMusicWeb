/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SecurityClasses;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
public class SendEmail {

    //Set up email details
    private String emUsername, emPassword;
    Properties emailProperties = new Properties();

    public void initialize() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader("email.config"));
        emUsername = br.readLine();
        emPassword = br.readLine();
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        emailProperties.put("mail.smtp.host", "smtp.office365.com");
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
        emailMessage.setFrom(new InternetAddress("securemusicweb@outlook.com"));
        emailMessage.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(resetEmail));
        emailMessage.setSubject("You requested a password reset");
        emailMessage.setText("Dear " + resetUser
                + ",\n\nYou have recently requested a password reset.\n"
                + "Please use this to log in and change your password \n\n"
                + "Your temporary password is: \n" + newPass
                + "\n\nIf you hadn't requested a password reset, "
                + "please contact the website administrator.\n\n - SecureMusicWeb");
        Transport.send(emailMessage);
    }

    public void lockedOut(String email, String user) throws AddressException, MessagingException {
        Session session = Session.getInstance(emailProperties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emUsername, emPassword);
            }
        });

        //Send email confirming account lock
        Message emailMessage = new MimeMessage(session);
        emailMessage.setFrom(new InternetAddress("securemusicweb@outlook.com"));
        emailMessage.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(email));
        emailMessage.setSubject("You have been locked out");
        emailMessage.setText("Dear " + user
                + ",\n\nDue to 3 or more consecutive failed login attempts, "
                + "your account has been locked out for 30 minutes. \n"
                + "If you have forgotten your password or are unaware of any "
                + "failed loging attempts, please wait until the account"
                + " is unlocked and use the link on the page to change "
                + "your password.\nIf you have any questions, please contact "
                + "the website administrator.\n\n - SecureMusicWeb");
        Transport.send(emailMessage);
    }

}
