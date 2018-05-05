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
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

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
        
        //Set up HTML sanitizers to allow inline formatting and links only
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
        
        if (session.getAttribute("isLoggedIn") != null) {
            String blogTitle = policy.sanitize(request.getParameter("blogtitle"));
            String blogContent = policy.sanitize(request.getParameter("blogcontent"));
            String username = policy.sanitize(session.getAttribute("username").toString());
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
                
                String SQL2 = "INSERT INTO blogs (username, content, title) VALUES (?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(SQL2);
                ps.setString(1, username);
                ps.setString(2, blogContent);
                ps.setString(3, blogTitle);
                ps.executeUpdate();
                response.sendRedirect("index.jsp");
            } catch (ClassNotFoundException | SQLException e) {
            }
            
        }
    }
}
