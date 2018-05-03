/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbServlets;

import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.lang.Object;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/Email"})
public class Email extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final String username = "securemusicweb@gmail.com";
        final String password = "WenjiaWang2018";

        String resetUser = request.getParameter("username");
        String resetEmail = request.getParameter("email");

        UUID randomGen = UUID.randomUUID();
        log("UUID One: " + randomGen);
        String newPass = randomGen.toString().substring(0, 7);

        String dbName, dbPassword, cmpHost, dbURL;
        boolean found = false;
        try {
            Class.forName("org.postgresql.Driver");
            dbName = "groupcz";
            dbPassword = "groupcz";
            cmpHost = "cmpstudb-02.cmp.uea.ac.uk";
            dbURL = ("jdbc:postgresql://" + cmpHost + "/" + dbName);
            Connection connection = DriverManager.getConnection(dbURL, dbName, dbPassword);

            String SQL1 = "SET search_path TO musicweb";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(SQL1);

            PreparedStatement ps = connection.prepareStatement("SELECT * from dbuser WHERE username = ? AND email = ?");
            ps.setString(1, resetUser);
            ps.setString(2, resetEmail);
            ResultSet rs = ps.executeQuery();
            found = rs.next();

            ps = connection.prepareStatement("UPDATE dbuser SET password = ? WHERE username = ?");
            ps.setString(1, newPass);
            ps.setString(2, resetUser);
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
        }
        if (found) {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("securemusicweb@gmail.com"));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse("nickgrajecki@gmail.com"));
                message.setSubject("You requested a password reset");
                message.setText("Dear " + resetUser
                        + ",\n\nYou have recently requested a password reset.\n"
                        + "Please use this to log in and change your password \n\n"
                        + "Your temporary password is: \n" + newPass
                        + "\nIf you hadn't requested a password reset, "
                        + "please contact the website administrator.\n\n - SecureMusicWeb");
                Transport.send(message);
                try (PrintWriter out = response.getWriter()) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<meta http-equiv=\"refresh\" content=\"3;url=index.jsp\"/>");
                    out.println("<title>Password successfully reset</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>Your password has been reset. If you haven't been automatically redirected, click <a href = \"index.jsp\">here</a></h1>");
                    out.println("</body>");
                    out.println("</html>");
                }
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        } else {

        }
    }
}
