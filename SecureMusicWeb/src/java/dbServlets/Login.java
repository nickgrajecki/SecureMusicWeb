/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbServlets;

import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.mail.*;
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
        response.setContentType("text/html;charset=UTF-8");
        
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
        
        String logName = policy.sanitize(request.getParameter("username"));
        String logPass = policy.sanitize(request.getParameter("pass"));
        HttpSession session = request.getSession();

        try {
            if (UserCheck.verifyUser(logName, logPass)) {
                RequestDispatcher rs = request.getRequestDispatcher("Verified");
                rs.forward(request, response);
                session.setAttribute("username", logName);
                request.setAttribute("username", logName);
                session.setAttribute("isLoggedIn", true);
            } else {
                request.setAttribute("invalidMessage", "Password or username invalid"); // Will be available as ${message}
                request.getRequestDispatcher("index.jsp").forward(request, response);
//                RequestDispatcher rs = request.getRequestDispatcher("index.jsp");
//                rs.include(request, response);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
    }
}
