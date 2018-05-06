package dbServlets;

import static SecurityClasses.PasswordHash.hashPass;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import javax.servlet.http.HttpServlet;

public class UserCheck extends HttpServlet {

    @SuppressWarnings("empty-statement")
    public static boolean verifyUser(String logName, String logPass) throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {
        
        String dbName, dbPassword, cmpHost, dbURL;
        boolean verified = false;
        try {
            Class.forName("org.postgresql.Driver");
            dbName = "groupcz";
            dbPassword = "groupcz";
            cmpHost = "cmpstudb-02.cmp.uea.ac.uk";
            dbURL = ("jdbc:postgresql://" + cmpHost + "/" + dbName);
            Connection connection = DriverManager.getConnection(dbURL, dbName, dbPassword);
            
            PreparedStatement ps = connection.prepareStatement("SET search_path TO musicweb");
            ps.executeUpdate();
            
            PreparedStatement saltMatch = connection.prepareStatement("SELECT salt FROM dbuser WHERE username = ?");
            saltMatch.setString(1, logName);
            ResultSet resultSalt = saltMatch.executeQuery();
            resultSalt.next();
            String salt = resultSalt.getString("salt");
            
            ps = connection.prepareStatement("SELECT * from dbuser WHERE username =? AND password = ?");
            ps.setString(1, logName);
            ps.setString(2, hashPass(salt + logPass));
            ResultSet rs = ps.executeQuery();
            verified = rs.next();
        } catch (ClassNotFoundException | SQLException e) {
        }
        return verified;
        //
    }
}
