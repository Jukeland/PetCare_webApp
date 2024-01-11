/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.tables;

import mainClasses.PetOwner;
import com.google.gson.Gson;
import mainClasses.PetOwner;
import database.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import mainClasses.Booking;
import org.json.JSONObject;

public class EditPetOwnersTable {
    
    public void updatePetOwner(JSONObject jo) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        try{
            String update = "UPDATE petowners SET "
                + "password='" + jo.getString("password") + "',"
                + "firstname='" + jo.getString("firstname") + "',"
                + "lastname='" + jo.getString("lastname") + "',"
                + "birthdate='" + jo.getString("birthdate") + "',"
                + "gender='" + jo.getString("gender") + "',"
                + "country='" + jo.getString("country") + "',"
                + "city='" + jo.getString("city") + "',"
                + "address='" + jo.getString("address") + "',"
                + "personalpage='" + jo.getString("personalpage") + "',"
                + "job='" + jo.getString("job") + "',"
                + "telephone='" + jo.getString("telephone") + "',"
                + "lat='" + jo.getString("lat") + "',"
                + "lon='" + jo.getString("lon") + "'"
                + "WHERE "
                + "username ='" + jo.getString("username") + "' AND email='" + jo.getString("email") + "'";
            stmt.executeUpdate(update);
        }catch(Exception e){
            System.err.println("updatePetOwner says: Got an exception! ");
            System.err.println(e.getMessage());
        }
        stmt.close();
        con.close();
    }

    
    public void deletePetOwner(int id) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        
        ResultSet rs;
        try{
            stmt.executeUpdate("DELETE FROM reviews WHERE owner_id = " + id);
            rs = stmt.executeQuery("SELECT * FROM bookings WHERE owner_id = " + id);
            if(rs.isBeforeFirst()){
                rs.next();
                String json = DB_Connection.getResultsToJSON(rs); 
                JSONObject jsonObj = new JSONObject(json);
                stmt.executeUpdate("DELETE FROM messages WHERE booking_id = " + jsonObj.getString("booking_id"));
            }
            stmt.executeUpdate("DELETE FROM bookings WHERE owner_id = " + id);
            stmt.executeUpdate("DELETE FROM pets WHERE owner_id = " + id);
            stmt.executeUpdate("DELETE FROM petowners WHERE owner_id = " + id);
            stmt.close();
            con.close();
        }catch(Exception e){
            System.err.println("del petowner says: Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
 
    public void addPetOwnerFromJSON(String json) throws ClassNotFoundException{
         PetOwner user=jsonToPetOwner(json);
         addNewPetOwner(user);
    }
    
     public PetOwner jsonToPetOwner(String json){
         Gson gson = new Gson();

        PetOwner user = gson.fromJson(json, PetOwner.class);
        return user;
    }
    
    public String petOwnerToJSON(PetOwner user){
         Gson gson = new Gson();

        String json = gson.toJson(user, PetOwner.class);
        return json;
    }
    
   
    
    public PetOwner databaseToPetOwner(String username, String password) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM petowners WHERE username = '" + username + "' AND password='"+password+"'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            PetOwner user = gson.fromJson(json, PetOwner.class);
            return user;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    public String databasePetOwnerToJSON(String username, String password) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM petowners WHERE username = '" + username + "' AND password='"+password+"'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            return json;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }


     public void createPetOwnersTable() throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE petowners "
                + "(owner_id INTEGER not NULL AUTO_INCREMENT, "
                + "    username VARCHAR(30) not null unique,"
                + "    email VARCHAR(50) not null unique,	"
                + "    password VARCHAR(32) not null,"
                + "    firstname VARCHAR(30) not null,"
                + "    lastname VARCHAR(30) not null,"
                + "    birthdate DATE not null,"
                + "    gender  VARCHAR (7) not null,"
                + "    country VARCHAR(30) not null,"
                + "    city VARCHAR(50) not null,"
                + "    address VARCHAR(50) not null,"
                + "    personalpage VARCHAR(200) not null,"
                + "    job VARCHAR(200) not null,"
                + "    telephone VARCHAR(14),"
                  + "    lat DOUBLE,"
                + "    lon DOUBLE,"
                + " PRIMARY KEY (owner_id))";
        stmt.execute(query);
        stmt.close();
    }
    
    
    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void addNewPetOwner(PetOwner user) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " petowners (username,email,password,firstname,lastname,birthdate,gender,country,city,address,personalpage,"
                    + "job,telephone,lat,lon)"
                    + " VALUES ("
                    + "'" + user.getUsername() + "',"
                    + "'" + user.getEmail() + "',"
                    + "'" + user.getPassword() + "',"
                    + "'" + user.getFirstname() + "',"
                    + "'" + user.getLastname() + "',"
                    + "'" + user.getBirthdate() + "',"
                    + "'" + user.getGender() + "',"
                    + "'" + user.getCountry() + "',"
                    + "'" + user.getCity() + "',"
                    + "'" + user.getAddress() + "',"
                    + "'" + user.getPersonalpage() + "',"
                    + "'" + user.getJob() + "',"
                    + "'" + user.getTelephone() + "',"
                    + "'" + user.getLat() + "',"
                    + "'" + user.getLon() + "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The pet owner was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditPetOwnersTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean petOwnerExists(String username, String password) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        
        ResultSet rs;
        try{
            rs = stmt.executeQuery("select exists(select * from petowners where username = '" + username + "' and password ='" + password + "')");
            boolean result = false;
            if(rs.next()){
                result = rs.getInt(1) == 1;
            }
            stmt.close();
            con.close();
            return result;
        }catch(Exception e){
            System.err.println("Got an exception");
            System.err.println(e.getMessage());
        }
        return false;
    }
    
    public ArrayList<PetOwner> databaseToPetOwners() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<PetOwner> pet_owners = new ArrayList<>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM petowners");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                PetOwner po = gson.fromJson(json, PetOwner.class);
                pet_owners.add(po);
            }
            stmt.close();
            con.close();
            return pet_owners;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    public int databaseGetPetOwnerId(String username) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        
        ResultSet rs;
        try{
            rs = stmt.executeQuery("SELECT owner_id FROM petowners WHERE username = '" + username + "'");
            int result = -1;
            if(rs.next())
                result = rs.getInt(1);
            stmt.close();
            con.close();
            return result;
        }catch(Exception e){
            System.err.println("EditPetOwnersTable getId says: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return -1;
    }
    
    public String databaseGetPetOwnerUsername(int id) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        
        ResultSet rs;
        try{
            rs = stmt.executeQuery("SELECT username FROM petowners WHERE owner_id = '" + id + "'");
            String result = "";
            if(rs.next())
                result = rs.getString(1);
            stmt.close();
            con.close();
            return result;
        }catch(Exception e){
            System.err.println("EditPetOwnersTable getUsername says: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return "";
    }

}
