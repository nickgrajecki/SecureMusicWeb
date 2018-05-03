<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Your Profile</title>
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="SecureMusicCSS.css" />
        <link href="https://fonts.googleapis.com/css?family=Gugi" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script type="text/javascript" src="SecureMusicJS.js"></script>
    </head>
    <body onload='scrollFade();'>
        <div class="nav">
            <ul class="navigationBar">
                <li><a href="index.jsp">Home</a></li>
                <li><a href="blog.html">Blog</a></li>
                <li><a href="news.html">News</a></li>
                <li><a href="loginregister.html">Login/Register</a></li>
                <li><a href="profile.jsp">Profile</a></li>
                <li><a href="contact.html">Contact Us</a></li>
            </ul>
        </div>
        <div class="header">
            <h1>SECURE MUSIC</h1>
            <h3 class="tagline">Congratulations! You are our 1 millionth visitor. Click here to claim your prize.</h3>
        </div>

        <div class="slideshowcontainer">
            <img class="headerBackground" src="images\header1.jpg" alt="Header Background">

        </div>
        Welcome ${username}!<br>
        Change password below<br>
        -----------------------<br>        
        <!-- Insert form to change password -->
        Your posts:
        <table>
            <tr>
                <td><b>Title</b></td>
                <td><b>Author</b></td>
                <td><b>Content</b></td>
            </tr>
            <%
                try {
                    Class.forName("org.postgresql.Driver");
                    String dbName, dbPassword, cmpHost, dbURL;
                    dbName = "groupcz";
                    dbPassword = "groupcz";
                    cmpHost = "cmpstudb-02.cmp.uea.ac.uk";
                    dbURL = ("jdbc:postgresql://" + cmpHost + "/" + dbName);
                    Connection connection = DriverManager.getConnection(dbURL, dbName, dbPassword);
                    Statement stmt = connection.createStatement();
                    String SQL1 = "SET search_path TO musicweb";
                    stmt.executeUpdate(SQL1);
                    String username = session.getAttribute("username").toString();
                    String SQL2 = "SELECT * FROM blogs WHERE username = '" + username + "'";
                    ResultSet rs = stmt.executeQuery(SQL2);
                    while (rs.next()) {

            %>
            <tr>
                <td><%=rs.getString("title")%></td>
                <td><%=rs.getString("username")%></td>
                <td><%=rs.getString("content")%></td></tr><br>
                <%
                    }
                %>
        </table>
        <%
            } catch (Exception e) {
                e.printStackTrace();
            }
        %>
    </div>
    <div class="footer">

        <div class="socialMedia">
            <p>Social Media Links:</p><br/>
            <img id="facebook" src="images\SM6.png" alt="Facebook Link">
            <img id="twitter" src="images\SM2.png" alt="Twitter Link">
            <img id="music" src="images\SM1.png" alt="Music Link">
            <img id="youtube" src="images\SM3.png" alt="YouTube Link">
            <img id="insta" src="images\SM4.png" alt="Instagram Link">
            <img id="camera" src="images\SM5.png" alt="Camera Link">
        </div>

        <p id="policy"><a href="#">Privacy Policy</a> | <a href="#">Terms of Service</a> | © <i>SecureMusic Int. Lmt. 2018</i></p>
        <div class="footerLinks">
            <ul>
                <li><a href="index.jsp">Home</a></li>
                <li><a href="blog.html">Blog</a></li>
                <li><a href="news.html">News</a></li>
                <li><a href="loginregister.html">Login/Register</a></li>
                <li><a href="profile.jsp">Profile</a></li>
                <li><a href="contact.html">Contact Us</a></li>
            </ul>
        </div>

        <div class="contactLinks">
            <ul>
                <li>Phone: 0344-000-0000</li>
                <li>Email: customersupport@securemusic.com</li>
            </ul>
        </div>

    </div>
</body>
</html>
