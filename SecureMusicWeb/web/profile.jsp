<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
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
                            String SQL2 = "SELECT * FROM blogs WHERE username = '"+username+"'";
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
    </body>
</html>
