/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbServlets;

import static SecurityClasses.PasswordHash.hashPass;
import SecurityClasses.Salt;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

@WebServlet(urlPatterns = {"/Register"})
public class Register extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            HttpSession session = request.getSession();

            //Set up HTML sanitizers to allow inline formatting and links only
            //Source: OWASP Java HTML Sanitizer Project
            PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS).and(Sanitizers.TABLES);

            //Retrieve all values, sanitize them
            String regName = policy.sanitize(request.getParameter("newusername"));
            String regPass = policy.sanitize(request.getParameter("newpassword"));
            String regEmail = policy.sanitize(request.getParameter("email")).replaceAll("&#64;", "@");
            
            
            //Confirm if email and pass meet expected format, return error message if not
            boolean correctEmail = regEmail.matches("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}");
            boolean correctPassword = regPass.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(.{8,15})$");

            try {
                if (correctEmail && correctPassword) {
                    //Connect do DB
                    String dbName, dbPassword, cmpHost, dbURL;
                    Class.forName("org.postgresql.Driver");
                    dbName = "groupcz";
                    dbPassword = "groupcz";
                    cmpHost = "cmpstudb-02.cmp.uea.ac.uk";
                    dbURL = ("jdbc:postgresql://" + cmpHost + "/" + dbName);
                    Connection connection = DriverManager.getConnection(dbURL, dbName, dbPassword);

                    //Check if email exists
                    PreparedStatement emailCheck = connection.prepareStatement("SELECT * from musicweb.dbuser WHERE email = ?");
                    emailCheck.setString(1, regEmail);
                    ResultSet email = emailCheck.executeQuery();
                    boolean emailExists = email.next();

                    //Check if username exists
                    PreparedStatement usernameCheck = connection.prepareStatement("SELECT * from musicweb.dbuser WHERE username = ?");
                    usernameCheck.setString(1, regName);
                    ResultSet login = usernameCheck.executeQuery();
                    boolean loginExists = login.next();

                    //If login or email or both already exist, return with error
                    if (loginExists || emailExists) {
                        request.setAttribute("registerMessage", "Username or email already taken.");
                    } else {
                        //Generate salt for user
                        Salt salt = new Salt();
                        String genSalt = salt.saltToString(salt.generateSalt());
                        String saltedPass = genSalt + regPass;
                        String hashedPass = hashPass(saltedPass);

                        //Insert user into db, including salt and hashed pass
                        PreparedStatement ps = connection.prepareStatement("INSERT INTO musicweb.dbuser VALUES (?, ?, ?, ?, ?, ?)");
                        ps.setString(1, regName);
                        ps.setString(2, hashedPass);
                        ps.setString(3, regEmail);
                        ps.setInt(4, 0);
                        ps.setLong(5, System.currentTimeMillis());
                        ps.setString(6, genSalt);
                        ps.executeUpdate();
                        request.setAttribute("registerMessage", "Successfully registered");
                    }
                } else {
                    request.setAttribute("registerMessage", "Email or password do not match requirements");
                }
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            //
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
    }
}
