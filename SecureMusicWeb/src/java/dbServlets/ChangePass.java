/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbServlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Nick
 */
@WebServlet(name = "ChangePass", urlPatterns = {"/ChangePass"})
public class ChangePass extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String username = session.getAttribute("username").toString();
        String currentPass = request.getParameter("currentpass");
        String newPass = request.getParameter("newpass");
        String confirmPass = request.getParameter("confirmpass");
        boolean passConfirmed = false;
        try {
            String dbName, dbPassword, cmpHost, dbURL;
            Class.forName("org.postgresql.Driver");
            dbName = "groupcz";
            dbPassword = "groupcz";
            cmpHost = "cmpstudb-02.cmp.uea.ac.uk";
            dbURL = ("jdbc:postgresql://" + cmpHost + "/" + dbName);
            Connection connection = DriverManager.getConnection(dbURL, dbName, dbPassword);
            Statement stmt = connection.createStatement();

            String SQL1 = "SET search_path TO musicweb";
            stmt.executeUpdate(SQL1);

            String SQL2 = "SELECT password FROM dbuser WHERE username ='" + username + "'";
            ResultSet rs = stmt.executeQuery(SQL2);
            rs.next();
            String dbPass = rs.getString("password");
            passConfirmed = currentPass.equals(dbPass);
            boolean matchingPass = newPass.equals(confirmPass);
            if (passConfirmed && matchingPass) {
                String SQL3 = "UPDATE dbuser SET password = '" + newPass + "' WHERE username = '" + username + "'";
                stmt.executeUpdate(SQL3);
                request.setAttribute("confirmPassMessage", "Password changed successfully");
            } else {
                request.setAttribute("confirmPassMessage", "Incorrect details - try again");
            }
        } catch (ClassNotFoundException | SQLException e) {
        }

    }
}
