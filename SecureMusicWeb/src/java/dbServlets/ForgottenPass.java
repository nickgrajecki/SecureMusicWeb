/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbServlets;

import SecurityClasses.SendEmail;
import static SecurityClasses.PasswordHash.hashPass;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

@WebServlet(urlPatterns = {"/Email"})
public class ForgottenPass extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Set up HTML sanitizers to allow inline formatting and links only
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

        String resetUser = policy.sanitize(request.getParameter("username"));
        String resetEmail = policy.sanitize(request.getParameter("email")).replaceAll("&#64;", "@");

        /*Generate random 8 char string + append lowercase and uppercase to 
        match password requirements*/
        UUID randomGen = UUID.randomUUID();
        log("UUID One: " + randomGen);
        String newPass = "T" + randomGen.toString().substring(0, 7) + "f";

        String dbName, dbPassword, cmpHost, dbURL;

        try {
            //Connect to DB
            Class.forName("org.postgresql.Driver");
            dbName = "groupcz";
            dbPassword = "groupcz";
            cmpHost = "cmpstudb-02.cmp.uea.ac.uk";
            dbURL = ("jdbc:postgresql://" + cmpHost + "/" + dbName);
            Connection connection = DriverManager.getConnection(dbURL, dbName, dbPassword);

            //Check if user exists in database 
            PreparedStatement ps = connection.prepareStatement("SELECT * from musicweb.dbuser WHERE username = ? AND email = ?");
            ps.setString(1, resetUser);
            ps.setString(2, resetEmail);
            ResultSet rs = ps.executeQuery();
            //Assign true to found if there is a result
            boolean found = rs.next();
            String salt = rs.getString("salt");

            //If user found, update password and send email
            if (found) {

                //Hash the newly generated temporary password
                String hashPass = hashPass(salt + newPass);

                ps = connection.prepareStatement("UPDATE musicweb.dbuser SET password = ? WHERE username = ?");
                ps.setString(1, hashPass);
                ps.setString(2, resetUser);
                ps.executeUpdate();

                SendEmail lockE = new SendEmail();
                lockE.initialize();
                lockE.changePassEmail(resetEmail, resetUser, newPass);
                
                //Redirect to a confirmation page, return to index after 3s
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
            }
        } catch (ClassNotFoundException | SQLException | NoSuchAlgorithmException | MessagingException ex) {
            Logger.getLogger(ForgottenPass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
