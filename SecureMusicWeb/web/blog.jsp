<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<!DOCTYPE html>

<html>
    <head>
        <!--Meta data-->
        <title>Secure Music Blog</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!--StyleSheets: CSS and JS-->
        <link rel="stylesheet" type="text/css" href="SecureMusicCSS.css" />
        <link href="https://fonts.googleapis.com/css?family=Gugi" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script type="text/javascript" src="SecureMusicJS.js"></script>
        <script src="https://cloud.tinymce.com/stable/tinymce.min.js?apiKey=p6szcpxygu55ycfq39yga5wgs2vustj56sthnuidnu5heuja"></script>
        <script>tinymce.init({
                selector: 'textarea',
                width: '480',
                resize: 'both'});
        </script>
    </head>

    <body onload='scrollFade();'>

        <!--nav div containing links to all pages-->
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

        <!--header div containing title and tagline-->
        <div class="header">
            <h1>SECURE MUSIC</h1>
            <h3 class="tagline">Music Blogging without the Treble.</h3>
        </div>

        <!--header background is separate from the div for easier styling-->
        <img class="headerBackground" src="images\header1.jpg" alt="Header Background">

        <!--main div containing all page content-->
        <div class="main">
            <!--Checks if user is logged in - if so, run code under-->
            <% }

                
                

                else {%>
            > Welcome, ${username}!<br>
            <form action="Logout" method="post">
                <button type="submit" name="logout" value="logout" class="btn-link">Log out</button>
            </form>
            <form action="profile.jsp" method="post">
                <button type="submit" name="profile" value="profile" class="btn-link">Profile</button>
            </form>
        </div>
        <% }%>

        <!--div containing forum directory in the form of links-->

        <!-- Checks if logged in, if not, displays message, otherwise shows posts and allows to make new one --> 
        <div class="blogs">
            <% if ((session.getAttribute (
                         
                

                
                    "isLoggedIn") == null)) { %>
            <p id="loginrequest">Please log in or register to view or create new blog posts</p>
            <% } else {%>
            <form id ="blogSubmit" method="post" action="PostBlog">
                <h3>Create your blog post</h3>
                <!--Form only allows certain characters to be entered-->
                <label>Blog title: </label><input type="text" name="blogtitle" id="blogtitle" placeholder="Enter Blog Title"><br/>
                <textarea type="text" name="blogcontent"></textarea>
                <input class="button" type="submit" value="Post" />
            </form><br>
            <div>
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
                        String SQL2 = "SELECT * FROM blogs";
                        ResultSet rs = stmt.executeQuery(SQL2);
                        while (rs.next()) {

                %>
                <div class="blogposts">
                    <p> <%=rs.getString("username")%>
                        || Posted at:
                        <%=rs.getString("time").substring(0, 16)%></p>
                    <%=rs.getString("title")%><br>
                    <%=rs.getString("content")%><br>

                </div>
                <%
                    }
                %>
                <%
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                %>
                <% }%>
            </div>

        </div>
        <br>
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