/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbServlets;

import SecurityClasses.SendEmail;
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
import javax.mail.MessagingException;
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            HttpSession session = request.getSession();

            //Connect do DB
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

            //IP Block for brute force attempts
            String IPAddress = InetAddress.getLocalHost().toString();

            //It shows it as 'COMPUTER NAME/IP ADDRESS' so I split it
            String[] IPparts = IPAddress.split("/");
            IPAddress = IPparts[1];
            int IPAttemptJ = 0;
            boolean locked = false;

            //If this is the first attempt this session, initialize
            if (session.getAttribute("IPAttempt") == null) {
                session.setAttribute("IPAttempt", 1);
                IPAttemptJ = 1;
            } else {
                //If this is the nth attempt, add +1 to the count
                IPAttemptJ = (int) session.getAttribute("IPAttempt") + 1;
                session.setAttribute("IPAttempt", IPAttemptJ);
            }

            /*If user inputs wrong details 5 times in a row, blacklist IP
            Set at 5 for the purpose of the presentation*/
            if (IPAttemptJ == 5) {
                PreparedStatement psipTable = connection.prepareStatement("INSERT INTO musicweb.ip VALUES (?,?)");
                psipTable.setString(1, IPAddress);
                psipTable.setLong(2, currentTime);
                psipTable.executeUpdate();
            }

            /*Check if current IP is blacklisted; if so, check if enough time
            elapsed; if it has, remove IP from blacklist*/
            try {
                PreparedStatement psip = connection.prepareStatement("SELECT * FROM musicweb.ip WHERE address = ?");
                psip.setString(1, IPAddress);
                ResultSet rsIP = psip.executeQuery();
                while (rsIP.next()) {
                    long ipTime = rsIP.getLong("time_added");
                    boolean ipTimeElapsed = (((currentTime - ipTime) / 1000) / 60) > 30;
                    if (ipTimeElapsed) {
                        session.setAttribute("IPAttempt", 0);
                        psip = connection.prepareStatement("DELETE FROM musicweb.ip WHERE address = ?");
                        psip.setString(1, IPAddress);
                        psip.executeUpdate();
                        locked = false;
                    } else if (!ipTimeElapsed) {
                        locked = true;
                    }
                }
            } catch (SQLException E) {
            }

            //If IP isn't blacklisted, proceed with login
            if (!locked) {

                //Retrieve username and password, then hash it
                String logName = policy.sanitize(request.getParameter("username"));
                String logPass = policy.sanitize(request.getParameter("pass"));

                PreparedStatement psHash = connection.prepareStatement("SELECT salt, email FROM musicweb.dbuser WHERE username = ?");
                psHash.setString(1, logName);
                ResultSet rsHash = psHash.executeQuery();
                boolean userExists = rsHash.next();

                if (userExists) {
                    String salt = rsHash.getString("salt");
                    String email = rsHash.getString("email");

                    String saltedPass = salt + logPass;
                    String hashedPass = hashPass(saltedPass);

                    //Retrieve number of failed logins and time of last attempt
                    PreparedStatement ps1 = connection.prepareStatement("SELECT failed_attempts, last_attempt FROM musicweb.dbuser WHERE username = ?");
                    ps1.setString(1, logName);
                    ResultSet rs = ps1.executeQuery();
                    rs.next();
                    int failedAttempt = rs.getInt("failed_attempts");
                    long failedTime = rs.getLong("last_attempt");

                    //Convert time from ms to minutes
                    long timeLeft = (((currentTime - failedTime) / 1000) / 60);
                    boolean timeElapsed = timeLeft > 30;

                    //If 30 minutes elapsed, unlock the user, reset attempt count
                    if (timeElapsed && failedAttempt >= 3) {
                        PreparedStatement ps = connection.prepareStatement("UPDATE musicweb.dbuser SET failed_attempts = 0 WHERE username = ?");
                        ps.setString(1, logName);
                        ps.executeUpdate();
                        failedAttempt = 0;
                    }

                    //Allow login if details are correct and user isn't locked out
                    if (UserCheck.verifyUser(logName, hashedPass) && failedAttempt < 3) {
                        PreparedStatement ps = connection.prepareStatement("UPDATE musicweb.dbuser SET failed_attempts = 0 WHERE username = ?");
                        ps.setString(1, logName);
                        ps.executeUpdate();
                        session.setAttribute("username", logName);
                        session.setAttribute("isLoggedIn", true);
                        session.setAttribute("IPAttempt", 0);
                        
                        /*Delay page redirect to make sure attackers cannot use
                        differences in load time to reverse engineer*/
                        Thread.sleep(1200);
                        
                    } else if (failedAttempt < 3) {
                        //If details are incorrect, Add +1 to failed attempts
                        PreparedStatement ps = connection.prepareStatement("UPDATE musicweb.dbuser SET failed_attempts = failed_attempts + 1 WHERE username = ?");
                        ps.setString(1, logName);
                        ps.executeUpdate();
                        if (failedAttempt < 2) {
                            Thread.sleep(1200);
                            request.setAttribute("invalidMessage", "Password or username invalid");
                        } else {
                            //If this was the last allowed attempt, time-lock account and send email
                            PreparedStatement ps2 = connection.prepareStatement("UPDATE musicweb.dbuser SET last_attempt = ? WHERE username = ?");
                            ps2.setLong(1, currentTime);
                            ps2.setString(2, logName);
                            ps2.executeUpdate();
                            SendEmail sendEmail = new SendEmail();
                            sendEmail.initialize();
                            sendEmail.lockedOut(email, logName, IPAddress);
                        }

                    } else if (failedAttempt >= 3) {
                        //If failed attempts over allowed threshold
                        Thread.sleep(1200);
                        request.setAttribute("invalidMessage", "Password or username invalid");
                    }
                } else {
                    Thread.sleep(1200);
                    request.setAttribute("invalidMessage", "Password or username invalid");
                }
            } else {
                Thread.sleep(1200);
                request.setAttribute("invalidMessage", "Too many failed attempts. IP Blocked");
            }
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (NoSuchAlgorithmException | ClassNotFoundException | SQLException | MessagingException | InterruptedException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
