/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbServlets;

import static SecurityClasses.PasswordHash.hashPass;
import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.owasp.html.*;

/**
 *
 * @author uhv14amu
 */
@WebServlet(urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            HttpSession session = request.getSession();

            //Set up HTML sanitizers to allow inline formatting and links only
            PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

            String logName = policy.sanitize(request.getParameter("username"));
            String logPass = hashPass(policy.sanitize(request.getParameter("pass")));

            String dbName, dbPassword, cmpHost, dbURL;
            Class.forName("org.postgresql.Driver");
            dbName = "groupcz";
            dbPassword = "groupcz";
            cmpHost = "cmpstudb-02.cmp.uea.ac.uk";
            dbURL = ("jdbc:postgresql://" + cmpHost + "/" + dbName);
            Connection connection = DriverManager.getConnection(dbURL, dbName, dbPassword);

            PreparedStatement ps1 = connection.prepareStatement("SELECT failed_attempts FROM musicweb.dbuser WHERE username = ?");
            ps1.setString(1, logName);
            ResultSet rs = ps1.executeQuery();
            rs.next();
            int failedAttempt = (rs.getInt("failed_attempts"));

            if (failedAttempt < 3) {
                if (UserCheck.verifyUser(logName, logPass)) {
                    PreparedStatement ps = connection.prepareStatement("UPDATE musicweb.dbuser SET failed_attempts = 0 WHERE username = ?");
                    ps.setString(1, logName);
                    ps.executeUpdate();
                    session.setAttribute("username", logName);
                    request.setAttribute("username", logName);
                    session.setAttribute("isLoggedIn", true);
                } else {
                    PreparedStatement ps = connection.prepareStatement("UPDATE musicweb.dbuser SET failed_attempts = failed_attempts + 1 WHERE username = ?");
                    ps.setString(1, logName);
                    ps.executeUpdate();
                    if (failedAttempt < 2) {
                        request.setAttribute("invalidMessage", "Password or username invalid. You have " + (2 - failedAttempt) + " attempts left.");
                    } else {
                        request.setAttribute("invalidMessage", "Too many failed attempts. Your account has been locked out");
                    }
                }
            } else {
                request.setAttribute("invalidMessage", "Too many failed attempts. Your account has been locked out");
            }
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (NoSuchAlgorithmException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
