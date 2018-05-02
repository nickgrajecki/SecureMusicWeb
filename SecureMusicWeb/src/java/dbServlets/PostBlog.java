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
import java.sql.PreparedStatement;
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
@WebServlet(urlPatterns = {"/PostBlog"})
public class PostBlog extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String dbName, dbPassword, cmpHost, dbURL;
        HttpSession session = request.getSession();
        if (session.getAttribute("isLoggedIn") != null) {
            String blogTitle = request.getParameter("blogtitle");
            String blogContent = request.getParameter("blogcontent");
            String username = session.getAttribute("username").toString();
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
                String SQL2 = "INSERT INTO blogs (username, content, title) VALUES ('" + username + "', '" + blogContent + "', '" + blogTitle + "')";
                stmt.executeUpdate(SQL2);
            } catch (ClassNotFoundException | SQLException e) {
            }
            response.sendRedirect("index.jsp");
        }
    }
}