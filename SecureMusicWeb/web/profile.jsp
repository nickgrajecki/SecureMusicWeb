<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <!--Meta data-->
        <title>Your Profile</title>
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <!--StyleSheets: CSS and JS-->
        <link rel="stylesheet" type="text/css" href="SecureMusicCSS.css" />
        <link href="https://fonts.googleapis.com/css?family=Gugi" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script type="text/javascript" src="SecureMusicJS.js"></script>
    </head>
    <body onload='scrollFade();'>

        <div class="nav">
            <ul class="navigationBar">
                <li id='navli'><a href="index.jsp">Home</a></li>
                <li id='navli'><a href="blog.jsp">Blog</a></li>
                <li id='navli'><a href="news.html">News</a></li>
                <li id='navli'><a href="profile.jsp">Profile</a></li>
                <li id='navli'><a href="contact.html">Contact Us</a></li>
            </ul>
        </div>

        <!--header div containing title and tagline-->
        <div class="header">
            <h1>SECURE MUSIC</h1>
            <h3 class="tagline">Music Blogging without the Treble.</h3>
        </div>

        <!--header background is separate from the div for easier styling-->
        <img class="headerBackground" src="images\header1.jpg" alt="Header Background">

        <div class="profile">
            <div class="change">
                <h3 onload="greeting()"> Welcome to your profile page,
                    <%=session.getAttribute("username")%>! </h3>
                <div id= "changepass">
                    <h2>Change password below</h2>
                    <form id="passchange" class="tabContent" method="post" action="ChangePass">
                        <fieldset>
                            <label>Current password: </label><input type="password" required pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" name="currentpass" placeholder="Enter Current Password" id="password"><br/>
                            <label>New Password: </label><input type="password" required pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" name="newpass" placeholder="Enter New Password" id="password"><br/>
                            <label>Confirm New Password: </label><input type="password" required pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" name="confirmpass" placeholder="Confirm New Password" id="password"><br/>
                            <input class="loginsubmission" type="submit" value="Confirm" style="width: 7em; height: 3em; padding-top: 0.7em;"/>
                            ${confirmPassMessage}
                        </fieldset>
                    </form>
                </div>      

                <div id= "changeemail">
                    <h2>Change email below</h2>
                    <form id="passchange" class="tabContent" method="post" action="ChangeEmail">
                        <fieldset>
                            <label>Current Email: </label><input type="text" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}" name="currentemail" id="email" required><br/>
                            <label>New Email: </label><input type="text" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}" name="newemail" id="email" required><br/>
                            <label>Confirm Email: </label><input type="text" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}" name="confirmemail" id="email" required><br/>
                            <input class="loginsubmission" type="submit" value="Confirm" style="width: 7em; height: 3em; padding-top: 0.7em;"/>
                            ${confirmEmailMessage}
                        </fieldset>
                    </form>
                </div> 
            </div>

            <div class="yourPosts">
                <h3>Your posts:</h3>
                
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

                <div class="past">
                    <p class="pastPosts"><%=rs.getString("content")%></p>
                    <p class="pastPostsTime"><%=rs.getString("time").substring(0, 16)%></p>
                </div>

                <%
                    }
                %>
           
                <%
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                %>
            </div>

        </div>
        <!--contains footer information, i.e.pages, social media links, contact-->
        <div class="footer">

            <!--all social media links-->
            <div class="socialMedia">
                <p>Social Media Links:</p><br/>
                <a href="#"><img id="facebook" src="images\SM6.png" alt="Facebook Link"></a>
                <a href="#"><img id="twitter" src="images\SM2.png" alt="Twitter Link"></a>
                <a href="#"><img id="music" src="images\SM1.png" alt="Music Link"></a>
                <a href="#"><img id="youtube" src="images\SM3.png" alt="YouTube Link"></a>
                <a href="#"><img id="insta" src="images\SM4.png" alt="Instagram Link"></a>
                <a href="#"><img id="camera" src="images\SM5.png" alt="Camera Link"></a>
            </div>

            <!--dummy copyright text-->
            <p id="policy"><a href="#">Privacy Policy</a> | <a href="#">Terms of Service</a> | © <i>SecureMusic Int. Lmt. 2018</i></p>

            <!--links to other pages on the website-->
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

            <!--additional contact links, including mailto link-->
            <div class="contactLinks">
                <ul>
                    <li>Phone: 0344-000-0000</li>
                    <li>Email: <a href="mailto:customersupport@securemusic.com" target="_top">customersupport@securemusic.com</a></li>
                </ul>
            </div>

        </div>
    </body>
</html>
