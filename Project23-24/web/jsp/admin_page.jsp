<%@ page import="mainClasses.PetOwner" %>
<%@ page import="mainClasses.PetKeeper" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="database.tables.EditPetOwnersTable" %>
<%@ page import="database.tables.EditPetKeepersTable" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="mainClasses.Pet" %>
<%@ page import="database.tables.EditPetsTable" %>
<%@ page import="mainClasses.Booking" %>
<%@ page import="database.tables.EditBookingsTable" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>admin</title>
    <style>
        tr th {
            font-weight: bold
        }
 
        h3 {
            font-weight: bold;
            margin-top: 20px;
            margin-bottom: 40px;
        }

        table {
            margin-bottom: 150px;
            width: 100%;
        }
    </style>
</head>

<body>
<%
    // number of owners/keepers stuff
    ArrayList<PetOwner> owners;
    ArrayList<PetKeeper> keepers;
    EditPetOwnersTable epo = new EditPetOwnersTable();
    EditPetKeepersTable epk = new EditPetKeepersTable();
    int nopo = 0;
    int nopk = 0;
    
    // number of cats/dogs stuff
    ArrayList<Pet> pets;
    EditPetsTable ept = new EditPetsTable();
    int noc = 0;
    int nod = 0;
    
    // money gained from keepers/app stuff
    ArrayList<Booking> bookings;
    EditBookingsTable ebt = new EditBookingsTable();
    float mfk = 0;
    float mfa = 0;
    
    
    try{
        owners = epo.databaseToPetOwners();
        keepers = epk.databaseToPetKeepers();
        pets = ept.databaseToPets();
        bookings = ebt.databaseToBookings();
%>
<div>
    <h3>Pet Owners</h3>
    <table id="owners_table">
        <thead>
            <tr>
                <th>Username</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <%
                for(PetOwner po : owners){
                    nopo++;
            %>
            <tr>
                <td><%=po.getUsername()%></td>
                <td><%=po.getFirstname()%></td>
                <td><%=po.getLastname()%></td>
                <td>
                    <button type="button" onclick="admin_delete('<%=po.getId()%>', 'owner')">Delete</button>
                    
                </td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>

        <br>

    <h3>Pet Keepers</h3>
    <table id="keepers_table">
        <thead>
            <tr>
                <th>Username</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <%
                for(PetKeeper pk : keepers){
                    nopk++;
            %>
            <tr>
                <td><%=pk.getUsername()%></td>
                <td><%=pk.getFirstname()%></td>
                <td><%=pk.getLastname()%></td>
                <td>
                    <button type="button" onclick="admin_delete('<%=pk.getId()%>', 'keeper')">Delete</button>
                </td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>

<%
        for(Pet p : pets){
            if(p.getType().equals("cat"))
                noc++;
            else if(p.getType().equals("dog"))
                nod++;
        }
        
        for(Booking b : bookings){
            if(b.getStatus().equals("finished")){
                mfk += 0.85 * b.getPrice();
                mfa += 0.15 * b.getPrice();
            } 
        }
    }catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
    }
%>
    <h3>App Stats</h3>
    <table id="stats_table">
        <thead>
            <tr>
                <th>Number of Pets</th>
                <th>Money gained</th>
                <th>Number of Users</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>Cats: <%=noc%></td>
                <td>From Keepers: <%=mfk%>€</td>
                <td>Pet Owners: <%=nopo%></td>
            </tr>
            <tr>
                <td>Dogs: <%=nod%></td>
                <td>From App: <%=mfa%>€</td>
                <td>Pet Keepers: <%=nopk%></td>
            </tr>
        </tbody>
    </table>
</div>
        
</body>

</html>
