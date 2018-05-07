package dbServlets;

import static SecurityClasses.PasswordHash.hashPass;
import java.io.IOException;
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
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

@WebServlet(name = "ChangePass", urlPatterns = {"/ChangePass"})
public class ChangePass extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();

        //Set up HTML sanitizers to allow inline formatting and links only
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

        //Initialise variable to check if passwords match
        boolean passConfirmed = false;

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
            String currentPass = hashPass(policy.sanitize(request.getParameter("currentpass")));
            String newPass = policy.sanitize(request.getParameter("newpass"));
            String confirmPass = policy.sanitize(request.getParameter("confirmpass"));

            //Retrieve password 
            PreparedStatement ps = connection.prepareStatement("SELECT password FROM musicweb.dbuser WHERE username =? ");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String dbPass = rs.getString("password");

            //Check password matches with database and new passwords match too
            passConfirmed = currentPass.equals(dbPass);
            boolean matchingPass = newPass.equals(confirmPass);

            //If db password matches and new passwords match each other
            if (passConfirmed && matchingPass) {
                //Change password if both checks pass                
                PreparedStatement ps2 = connection.prepareStatement("UPDATE musicweb.dbuser SET password =? WHERE username =?");
                ps2.setString(1, hashPass(newPass));
                ps2.setString(2, username);
                ps2.executeUpdate();
                request.setAttribute("confirmPassMessage", "Password changed successfully");
            } else {
                //Redirect to try again
                request.setAttribute("confirmPassMessage", "Incorrect details - try again");
            }
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ChangePass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
