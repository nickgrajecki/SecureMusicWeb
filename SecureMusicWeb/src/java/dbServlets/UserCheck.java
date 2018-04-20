/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbServlets;

import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author uhv14amu
 */
public class UserCheck extends HttpServlet {

    @SuppressWarnings("empty-statement")
    public static boolean verifyUser(String logName, String logPass) throws ClassNotFoundException, SQLException {
        String dbName, dbPassword, cmpHost, dbURL;
        boolean verified = false;
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
            PreparedStatement ps = connection.prepareStatement("SELECT * from dbuser WHERE username =? AND password = ?");
            ps.setString(1, logName);
            ps.setString(2, logPass);
            ResultSet rs = ps.executeQuery();
            verified = rs.next();
        } catch (ClassNotFoundException | SQLException e) {
        }
        return verified;
    }
}
