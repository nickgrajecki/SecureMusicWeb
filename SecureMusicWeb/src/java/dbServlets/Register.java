/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbServlets;

import static SecurityClasses.PasswordHash.hashPass;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

@WebServlet(urlPatterns = {"/Register"})
public class Register extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            
            //Set up HTML sanitizers to allow inline formatting and links only
            //Source: OWASP Java HTML Sanitizer Project
            PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS).and(Sanitizers.TABLES);
            
            String regName = policy.sanitize(request.getParameter("newusername"));
            String regPass = hashPass(policy.sanitize(request.getParameter("newpassword")));
            String regEmail = policy.sanitize(request.getParameter("email")).replaceAll("&#64;", "@");
            String dbName, dbPassword, cmpHost, dbURL;
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
                
                PreparedStatement ps = connection.prepareStatement("INSERT INTO dbuser VALUES (?, ?, ?, ?, ?)");
                ps.setString(1, regName);
                ps.setString(2, regPass);
                ps.setString(3, regEmail);
                ps.setInt(4, 0);
                ps.setLong(5, System.currentTimeMillis());
                ps.executeUpdate();
                response.sendRedirect("index.jsp");
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            //
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
    }
    public static void main(String[] args) {
        String password = "password";
        try {
            System.out.println(hashPass(password));
        } catch(NoSuchAlgorithmException e) {
            System.out.println(e);
        }
    }
}