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

            String dbName, dbPassword, cmpHost, dbURL;
            Class.forName("org.postgresql.Driver");
            dbName = "groupcz";
            dbPassword = "groupcz";
            cmpHost = "cmpstudb-02.cmp.uea.ac.uk";
            dbURL = ("jdbc:postgresql://" + cmpHost + "/" + dbName);
            Connection connection = DriverManager.getConnection(dbURL, dbName, dbPassword);
            //Set up HTML sanitizers to allow inline formatting and links only
            PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
            long currentTime = System.currentTimeMillis();

            //My failed attempt at IP flood control
            
//            String IPAddress = InetAddress.getLocalHost().toString();
//            String[] IPparts = IPAddress.split("/");
//            IPAddress = IPparts[1];
//            int IPAttemptJ = 0;
//            
//            if (session.getAttribute("IPAttempt") == null) {
//                session.setAttribute("IPAttempt", 1);
//                IPAttemptJ = Integer.parseInt(session.getAttribute("IPAttempt").toString());
//            }
//
//            if (IPAttemptJ < 3) {
//                session.setAttribute("IPAttempt", IPAttemptJ + 1);
//            } else {
//                PreparedStatement psip = connection.prepareStatement("INSERT INTO musicweb.ip VALUES (?,?)");
//                psip.setString(1, IPAddress);
//                psip.setLong(2, currentTime);
//                psip.executeUpdate();
//                session.setAttribute("IPLock", true);
//            }
//
//            try {
//                PreparedStatement psip = connection.prepareStatement("SELECT * FROM musicweb.ip WHERE address = ?");
//                psip.setString(1, IPAddress);
//                ResultSet rsIP = psip.executeQuery();
//                while (rsIP.next()) {
//                    long ipTime = rsIP.getLong("time_added");
//                    boolean ipTimeElapsed = (((currentTime - ipTime) / 1000) / 60) > 30;
//                    if (ipTimeElapsed) {
//                        session.setAttribute("IPLock", false);
//                        session.setAttribute("IPAttempt", 0);
//                    }
//                }
//            } catch (SQLException E) {
//            }

            String logName = policy.sanitize(request.getParameter("username"));
            String logPass = hashPass(policy.sanitize(request.getParameter("pass")));

            PreparedStatement ps1 = connection.prepareStatement("SELECT failed_attempts, last_attempt FROM musicweb.dbuser WHERE username = ?");
            ps1.setString(1, logName);
            ResultSet rs = ps1.executeQuery();
            rs.next();
            int failedAttempt = rs.getInt("failed_attempts");
            long failedTime = rs.getLong("last_attempt");
            long timeLeft = (((currentTime - failedTime) / 1000) / 60);
            long timeRemaining = 30 - timeLeft;
            boolean timeElapsed = timeLeft > 30;

            if (timeElapsed && failedAttempt >= 3) {
                PreparedStatement ps = connection.prepareStatement("UPDATE musicweb.dbuser SET failed_attempts = 0 WHERE username = ?");
                ps.setString(1, logName);
                ps.executeUpdate();
                failedAttempt = 0;
            }

            if (UserCheck.verifyUser(logName, logPass) && failedAttempt < 3) {
                PreparedStatement ps = connection.prepareStatement("UPDATE musicweb.dbuser SET failed_attempts = 0 WHERE username = ?");
                ps.setString(1, logName);
                ps.executeUpdate();
                session.setAttribute("username", logName);
                request.setAttribute("username", logName);
                session.setAttribute("isLoggedIn", true);
            } else if (failedAttempt < 3) {
                PreparedStatement ps = connection.prepareStatement("UPDATE musicweb.dbuser SET failed_attempts = failed_attempts + 1 WHERE username = ?");
                ps.setString(1, logName);
                ps.executeUpdate();
                if (failedAttempt < 2) {
                    request.setAttribute("invalidMessage", "Password or username invalid. You have " + (2 - failedAttempt) + " attempts left.");
                } else {
                    PreparedStatement ps2 = connection.prepareStatement("UPDATE musicweb.dbuser SET last_attempt = ? WHERE username = ?");
                    ps2.setLong(1, currentTime);
                    ps2.setString(2, logName);
                    ps2.executeUpdate();
                    request.setAttribute("invalidMessage", "Too many failed attempts. Your account has been locked out");
                }
            } else if (failedAttempt >= 3) {
                request.setAttribute("invalidMessage", "Account locked. Time remaining: " + timeRemaining + " minutes");
            }
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (NoSuchAlgorithmException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
