/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbServlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

/**
 *
 * @author Nick
 */
@WebServlet(name = "ChangeEmail", urlPatterns = {"/ChangeEmail"})
public class ChangeEmail extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();

        //Set up HTML sanitizers to allow inline formatting and links only
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

        //Initialise variable to check if emails match
        boolean emailConfirmed = false;

        try {
            //Connect to database
            String dbName, dbPassword, cmpHost, dbURL;
            Class.forName("org.postgresql.Driver");
            dbName = "groupcz";
            dbPassword = "groupcz";
            cmpHost = "cmpstudb-02.cmp.uea.ac.uk";
            dbURL = ("jdbc:postgresql://" + cmpHost + "/" + dbName);
            Connection connection = DriverManager.getConnection(dbURL, dbName, dbPassword);

            //Request data from password change form, sanitize it, hash passwords
            String username = session.getAttribute("username").toString();
            String currentEmail = policy.sanitize(request.getParameter("currentemail")).replaceAll("&#64;", "@");
            String newEmail = policy.sanitize(request.getParameter("newemail")).replaceAll("&#64;", "@");
            String confirmEmail = policy.sanitize(request.getParameter("confirmemail")).replaceAll("&#64;", "@");

            //Retrieve password 
            PreparedStatement ps = connection.prepareStatement("SELECT email FROM musicweb.dbuser WHERE username =? ");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String dbEmail = rs.getString("email");

            //Check password matches with database and new passwords match too
            emailConfirmed = currentEmail.equals(dbEmail);
            boolean matchingEmail = newEmail.equals(confirmEmail);

            //If db password matches and new passwords match each other
            if (emailConfirmed && matchingEmail) {
                //Change password if both checks pass                
                PreparedStatement ps2 = connection.prepareStatement("UPDATE musicweb.dbuser SET email =? WHERE username =?");
                ps2.setString(1, newEmail);
                ps2.setString(2, username);
                ps2.executeUpdate();
                request.setAttribute("confirmEmailMessage", "Email changed successfully");
            } else {
                //Redirect to try again
                request.setAttribute("confirmEmailMessage", "Incorrect details - try again");
            }
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
