<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

    <head>
        <!--Meta data-->
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

    <!--when page loads, JavaScript also loads-->
    <body onload='scrollFade();' onload='showSlides();'>

        <!--nav div containing links to all pages-->
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

        <!--main div containing all page content-->
        <div class="main">

            <!--Login form requiring username and password-->
            <div class="loginSection">
                <!--Checks if user is logged in - if he isn't, run code under-->
                <% if ((session.getAttribute("isLoggedIn") == null)) { %>

                <!--Uses JavaScript to swap between login and register tabs-->
                <div class="loginRegister">
                    <button id="log" onclick="switchToLogin()"> Login </button>
                    <button id="reg" onclick="switchToRegister()"> Register </button>
                </div>

                <!--Login Form-->
                <form id="login" class="tabContent" method="post" action="Login">
                    <fieldset>
                        <!--Form only allows certain characters to be entered-->
                        <div class="loginreg">
                            <h3>Enter your username and password</h3>
                            <form action="Login">
                                <label>Username: </label><input type="text" pattern="\w+" name="username" id="user" placeholder="Enter Username" required><br/>
                                <label>Password: </label><input type="password" required pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" name="pass" placeholder="Enter Password" id="password"><br/>
                                <p><a href="reset.html">Forgotten Password?</a></p>
                                <input class="loginsubmission" type="submit" value="Login" />
                                <div class="formmessage">${invalidMessage}</div>
                            </form>
                        </div>
                    </fieldset>
                </form>

                <!--Register Form-->
                <form id="register" class="tabContent" method="post" action="Register">
                    <fieldset>
                        <div class="loginreg">
                            <h3>Create your username and password</h3>
                            <!--Form only allows certain characters to be entered-->
                            <label>Username: </label><input type="text" pattern="\w+" name="newusername" id="newuser" placeholder="Enter Username" required onclick = hideSetPassword()><br/>
                            <label>Email: </label><input type="text" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}" name="email" placeholder="nickgrajecki@compuserve.com" id="email" required><br/>

                            <div class="popup" onclick="popupPass()">
                                <label>Password: </label><input type="password" required pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" name="newpassword" placeholder="Enter Password" id="newpass" onclick = setPassword()><br/>
                                <span class="popuptext" id="popupReminder">                            
                                    Password must contain at least:
                                    <ul id="passwordList">
                                        <li>One uppercase letter</li>
                                        <li>One lowercase letter</li>
                                        <li>One number</li>
                                        <li>Eight characters</li>
                                    </ul>
                                </span>
                            </div>

                            ${registerMessage}
                            <input class="newusersubmission" type="submit" value="Register" />
                        </div>
                    </fieldset>
                </form>

                <!--Checks if user is logged in - if so, run code under-->
                <% } else {%>
                <div class="loginregIn">
                    <p> > Welcome, ${username}!</p>
                    <form id="loggedF" action="Logout" method="post">
                        <button id="logoutB" type="submit" name="logout" value="logout">Log out</button>
                    </form>
                    <form id="loggedF" action="profile.jsp" method="post">
                        <button id="profileB" type="submit" name="profile" value="profile">Profile</button>
                    </form>
                </div>
            </div>
            <% }%>
        </div> 

        <!--dummy content for the purpose of aesthetics-->
        <h1>WHAT'S ON?</h1>

        <div class="forumDirectory">

            <!--javascript 'news' slideshow-->
            <div class="newsSlideshow">
                <div class="newsFade" id='fade1'>
                    <div class="number"></div>
                    <img src="images\Concert1.png">
                    <div class="text">Lotus Festival (2017)</div>
                </div>

                <div class="newsFade">
                    <div class="number"></div>
                    <img src="images\Preview1.png">
                    <div class="text">One industry expert's opinion on rising in the ranks.</div>
                </div>

                <div class="newsFade">
                    <div class="number"></div>
                    <img src="images\Preview2.png">
                    <div class="text">Pitch perfection: mixing secrets of the Gods.</div>
                </div>

                <a class="previous" onlick="plusSlides(-1)">&#10094;</a>
                <a class="next" onclick="plusSlides(1)">&#10095;</a>

            </div>

            <div style="text-align:center">
                <span class="dot" onclick="currentSlide(1)"></span> 
                <span class="dot" onclick="currentSlide(2)"></span> 
                <span class="dot" onclick="currentSlide(3)"></span> 
            </div>

            <!--javascript hover buttons previewing aspects of discussion-->
            <h1>TRENDING TOPICS</h1>

            <div class="trend">
                <a href="#">
                    <img src="images\Shot1.png" /> 
                    <div class="overlay">
                        <div class="overlayText">Highlights and Best Bits: Our most popular posts</div>
                    </div>
                </a>
            </div>

            <div class="trend">
                <a href="#">
                    <img src="images\Shot2.png" />
                    <div class="overlay" style="background-color: #3d3d3d">
                        <div class="overlayText">Top 10 worldwide venues: where have you been?</div>
                    </div>
                </a>
            </div>

            <div class="trend">
                <a href="#">
                    <img src="images\Shot3.png" />
                    <div class="overlay" style="background-color: #595959">
                        <div class="overlayText">The best ways you can support your local artists</div>
                    </div>
                </a>
            </div>
        </div>
        <!--div containing forum directory in the form of links-->
        <br>
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
                    <li><a href="blog.jsp">Blog</a></li>
                    <li><a href="news.html">News</a></li>
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
