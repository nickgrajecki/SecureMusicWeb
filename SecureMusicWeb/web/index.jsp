<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<!DOCTYPE html>
<html>

    <head>
        <title>Secure Music</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!--StyleSheets: CSS and JS-->
        <link rel="stylesheet" type="text/css" href="SecureMusicCSS.css" />
        <link href="https://fonts.googleapis.com/css?family=Gugi" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script type="text/javascript" src="SecureMusicJS.js"></script>
    </head>

    <body>

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

        <!--header div containing title, logo, and background image-->
        <div class="header">
            <h1>SECURE MUSIC</h1>
            <h3 class="tagline">Congratulations! You are our 1 millionth visitor. Click here to claim your prize.</h3>
        </div>

        <div class="slideshowcontainer">
            <img class="headerBackground" src="images\header1.jpg" alt="Header Background">

        </div>

        <!--main div containing all page content-->
        <div class="main">

            <!--Login form requiring username and password-->
            <div class="loginSection">
                <!--Checks if user is logged in - if he isnt, run code under-->
                <% if ((session.getAttribute("isLoggedIn") == null)) { %>

                <!--Uses Javascript to swap between login and register tabs-->
                <div class="loginRegister">
                    <button id="log" onclick="switchToLogin()"> Login </button>
                    <button id="reg" onclick="switchToRegister()"> Register </button>
                </div>

                <!--Login Form-->
                <form id="login" class="tabContent" method="post" action="Login">
                    <fieldset>
                        <h3>Enter your username and password</h3>
                        <!--Form only allows certain characters to be entered-->
                        <label>Username: </label><input type="text" pattern="\w+" name="username" id="user" placeholder="Enter Username" required><br/>
                        <label>Password: </label><input type="password" required pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" name="pass" placeholder="Enter Password" id="password"><br/>
                        <p><a href="reset.html">Forgotten Password?</a></p>
                        ${invalidMessage}
                        <input class="loginsubmission" type="submit" value="Login" />
                    </fieldset>
                </form>

                <!--Register Form-->
                <form id="register" class="tabContent" method="post" action="Register">
                    <fieldset>
                        <h3>Create your username and password</h3>
                        <!--Form only allows certain characters to be entered-->
                        <label>Username: </label><input type="text" pattern="\w+" name="newusername" id="newuser" placeholder="Enter Username" required onclick = hideSetPassword()><br/>
                        <label>Email: </label><input type="text" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}" name="email" placeholder="nickgrajecki@compuserve.com" id="email" required><br/>
                        <label>Password: </label><input type="password" required pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" name="newpassword" placeholder="Enter Password" id="newpass" onclick = setPassword()><br/>
                        <div id="passwordReminder">
                            <p>Password must contain at least:</p>
                            <ul id="passwordList">
                                <li>One uppercase letter</li>
                                <li>One lowercase letter</li>
                                <li>One number</li>
                                <li>Eight characters</li>
                            </ul>
                        </div>
                        <input class="newusersubmission" type="submit" value="Register" />
                    </fieldset>
                </form>

            </div> 
            <!--Checks if user is logged in - if so, run code under-->
            <% } else {%>
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
        <div class="forumDirectory">
            <h1>WHAT'S ON?</h1>
            <h1>TRENDING TOPICS</h1>
            <h1>BUSINESS NEWS</h1>
        </div>
        <!-- Checks if logged in, if not, displays message, otherwise shows posts and allows to make new one --> 
        <div class="blogs">
            <% if ((session.getAttribute("isLoggedIn") == null)) { %>
            Please log in or register to view or create new blog posts
            <% } else {%>
            Please click HERE to create a new blog post.
            <form id ="blogSubmit" method="post" action="PostBlog">
                <h3>Create your blog post</h3>
                <!--Form only allows certain characters to be entered-->
                <label>Blog title: </label><input type="text" name="blogtitle" id="blogtitle" placeholder="Enter Blog Title"><br/>
                <label>Content: </label><input type="text" name="blogcontent" placeholder="Enter your content here" id="blogcontent" required><br/>
                <input class="newblogsubmission" type="submit" value="Post" />
            </form>
            <div>
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
                            String SQL2 = "SELECT * FROM blogs";
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
            <% }%>
        </div>

    </div>
    <br>
    <!--contains footer information, i.e.pages, social media links, contact-->
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
