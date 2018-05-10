/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SecurityClasses;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author Nick
 */
public class SendEmail extends HttpServlet {

    //Set up email details
    private String emUsername, emPassword;
    Properties emailProperties = new Properties();

    public void initialize() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
        String dbName, dbPassword, cmpHost, dbURL;

        Class.forName("org.postgresql.Driver");
        dbName = "groupcz";
        dbPassword = "groupcz";
        cmpHost = "cmpstudb-02.cmp.uea.ac.uk";
        dbURL = ("jdbc:postgresql://" + cmpHost + "/" + dbName);
        Connection connection = DriverManager.getConnection(dbURL, dbName, dbPassword);
        String SQL = "SELECT * FROM musicweb.sendemail";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        rs.next();
        emUsername = rs.getString("email");
        emPassword = rs.getString("password");

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
        emailMessage.setFrom(new InternetAddress("secmusicweb@gmail.com"));
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

    public void lockedOut(String email, String user, String IP) throws AddressException, MessagingException {
        Session session = Session.getInstance(emailProperties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emUsername, emPassword);
            }
        });

        //Send email confirming account lock
        Message emailMessage = new MimeMessage(session);
        emailMessage.setFrom(new InternetAddress("secmusicweb@gmail.com"));
        emailMessage.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(email));
        emailMessage.setSubject("You have been locked out");
        emailMessage.setText("Dear " + user
                + ",\n\nDue to 3 or more consecutive failed login attempts, "
                + "your account has been locked out for 30 minutes. \n\n"
                + "If you have forgotten your password or do not recognize the "
                + "following IP Address - " + IP
                + ", please change your password and contact the website administrator"
                + "\n\n - SecureMusicWeb");
        Transport.send(emailMessage);
    }

    public static void main(String[] args) throws IOException, FileNotFoundException, MessagingException, ClassNotFoundException, SQLException {
        SendEmail sendEmail = new SendEmail();
        sendEmail.initialize();
        sendEmail.lockedOut("nickgrajecki@gmail.com", "Nick", "139.281.10.1");
//
    }
}
