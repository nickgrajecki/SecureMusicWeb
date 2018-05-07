package dbServlets;

import java.sql.*;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author uhv14amu
 */
public class UserCheck extends HttpServlet {

    public static boolean verifyUser(String logName, String logPass) throws ClassNotFoundException, SQLException {

        //Initialize variable to verify
        boolean verified = false;

        try {
            //Connect to DB
            String dbName, dbPassword, cmpHost, dbURL;
            Class.forName("org.postgresql.Driver");
            dbName = "groupcz";
            dbPassword = "groupcz";
            cmpHost = "cmpstudb-02.cmp.uea.ac.uk";
            dbURL = ("jdbc:postgresql://" + cmpHost + "/" + dbName);
            Connection connection = DriverManager.getConnection(dbURL, dbName, dbPassword);

            //Check if username and password are right
            PreparedStatement ps = connection.prepareStatement("SELECT * from musicweb.dbuser WHERE username =? AND password = ?");
            ps.setString(1, logName);
            ps.setString(2, logPass);
            ResultSet rs = ps.executeQuery();
            verified = rs.next();
        } catch (ClassNotFoundException | SQLException e) {
        }
        return verified;
    }
}
