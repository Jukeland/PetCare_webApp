<%@ page import="mainClasses.PetKeeper" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="database.tables.EditPetKeepersTable" %>
<%@ page import="java.sql.SQLException" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Pet Keepers</title>
    <link rel="stylesheet" href="../css/style.css">
    <style>
         
        #wrapper{
            height: 100%;
            width: fit-content;
            display: flex;

            text-align: center;
            margin:auto;
            font-family: fantasy;
            font-size: 20px;
        }
        
        h3{
            height: 100%;
            width: fit-content;
            display: flex;

            text-align: center;
            margin:auto;
            font-family: fantasy;
            font-size: 40px;
        }
        
    </style>
</head>
<body>
    <div>
      <div> <!-- A div for the header of the page-->
        <header class="headers" id="title">
          <h1>Pet Care</h1>
        </header>
      </div>
  
  
      <div class="black_div"> <!-- A div for the navigation bar of the page-->
        <nav id="navigator">
          <ul>
            <li class="nav_list headers"><a href="../index.html">Home</a></li>
            <li class="nav_list headers"><a href="../register.html">Register</a></li>
            <li class="nav_list headers"><a href="../login.html">Login</a></li>
            <li class="nav_list headers"><a href="keepers_table_page.jsp">Pet Keepers</a></li>
          </ul>
        </nav>
      </div>
    </div>
    <h3>Pet Keepers</h3>
    <div id="wrapper">
        <%
            ArrayList<PetKeeper> keepers;
            EditPetKeepersTable epk = new EditPetKeepersTable();
            
            String type = "";
            String prices = "";
            
            try{
                keepers = epk.databaseToPetKeepers();
        %>
        
        <table>
            <thead>
                <tr>
                    <th>Username</th>
                    <th>Address</th>
                    <th>Property</th>
                    <th>Pet Type</th>
                    <th>Price</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for(PetKeeper pk : keepers){
                %>
                <tr>
                    <td><%=pk.getUsername()%></td>
                    <td><%=pk.getAddress()%></td>
                    <td><%=pk.getProperty()%></td>
                    <%
                        if(pk.getCatkeeper().equals("true") && pk.getDogkeeper().equals("true")){
                            type = "Both cat and dog";
                            prices = "Cat: " + pk.getCatprice() + "€, Dog: " + pk.getDogprice() + "€";
                        }else if(pk.getCatkeeper().equals("true")){
                            type = "Cat";
                            prices = "Cat: " + pk.getCatprice() + "€";
                        }else if(pk.getDogkeeper().equals("true")){
                            type = "Dog";
                            prices = "Dog: " + pk.getDogprice() + "€";
                        }
                    %>
                    <td><%=type%></td>
                    <td><%=prices%></td>
                </tr>
                <%
                        }
                    }catch(SQLException | ClassNotFoundException e){
                        e.printStackTrace();
                    }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>
