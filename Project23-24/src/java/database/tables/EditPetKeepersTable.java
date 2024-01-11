/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.tables;

import com.google.gson.Gson;
import mainClasses.PetKeeper;
import database.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author Mike
 */
public class EditPetKeepersTable {
    
    public void updatePetKeeper(JSONObject jo) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        System.out.println("EditPetKeepersTable updatePetKeeper says: user = " + jo);
        try{
            String update = "UPDATE petkeepers SET "
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
                    + "lon='" + jo.getString("lon") + "',"
                    + "property='" + jo.getString("property") + "',"
                    + "propertydescription='" + jo.getString("propertydescription") + "',"
                    + "catkeeper='" + jo.getString("catkeeper") + "',"
                    + "dogkeeper='" + jo.getString("dogkeeper") + "',"
                    + "catprice='" + jo.getString("catprice") + "',"
                    + "dogprice='" + jo.getString("dogprice") + "'"
                    + "WHERE "
                    + "username ='" + jo.getString("username") + "' AND email='" + jo.getString("email") + "'";
            stmt.executeUpdate(update);
        }catch(Exception e){
            System.err.println("updatePetKeeper says: Got an exception! ");
            System.err.println(e.getMessage());
        }
        stmt.close();
        con.close();
    }
    
    public void deletePetKeeper(int id) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try{
            stmt.executeUpdate("DELETE FROM reviews WHERE keeper_id = " + id);
            rs = stmt.executeQuery("SELECT * FROM bookings WHERE keeper_id = " + id);
            if(rs.isBeforeFirst()){
                rs.next();
                String json = DB_Connection.getResultsToJSON(rs); 
                JSONObject jsonObj = new JSONObject(json);
                stmt.executeUpdate("DELETE FROM messages WHERE booking_id = " + jsonObj.getString("booking_id"));
            }
            stmt.executeUpdate("DELETE FROM bookings WHERE keeper_id = " + id);
            stmt.executeUpdate("DELETE FROM petkeepers WHERE keeper_id = " + id);
            stmt.close();
            con.close();
        }catch(Exception e){
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

 
    public void addPetKeeperFromJSON(String json) throws ClassNotFoundException{
         PetKeeper user=jsonToPetKeeper(json);
         addNewPetKeeper(user);
    }
    
     public PetKeeper jsonToPetKeeper(String json){
         Gson gson = new Gson();

        PetKeeper user = gson.fromJson(json, PetKeeper.class);
        return user;
    }
    
    public String petKeeperToJSON(PetKeeper user){
         Gson gson = new Gson();

        String json = gson.toJson(user, PetKeeper.class);
        return json;
    }
    
   
    
    public void printPetKeeperDetails(String username, String password) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM petkeepers WHERE username = '" + username + "' AND password='"+password+"'");
            while (rs.next()) {
                System.out.println("===Result===");
                DB_Connection.printResults(rs);
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    public PetKeeper databaseToPetKeeper(String username, String password) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM petkeepers WHERE username = '" + username + "' AND password='"+password+"'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            PetKeeper user = gson.fromJson(json, PetKeeper.class);
            return user;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
    

    
     public ArrayList<PetKeeper> getAvailableKeepers(String type) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<PetKeeper> keepers = new ArrayList<PetKeeper>();
        ResultSet rs = null;
        try {
            //if(type=="catkeeper")
            if("all".equals(type))     
            rs = stmt.executeQuery("SELECT * FROM `petKeepers` WHERE  `petKeepers`.`keeper_id` not in (select keeper_id "
                 + "from `bookings` where `status`='requested' or  `status`='accepted')\n" +"");
            else if ("catKeepers".equals(type))
                 rs = stmt.executeQuery("SELECT * FROM `petKeepers` WHERE `petKeepers`.`catkeeper`='true' AND `petKeepers`.`keeper_id` not in (select keeper_id "
                 + "from `bookings` where `status`='requested' or  `status`='accepted')");         
             else if ("dogKeepers".equals(type))
                 rs = stmt.executeQuery("SELECT * FROM `petKeepers` WHERE `petKeepers`.`dogkeeper`='true' AND `petKeepers`.`keeper_id` not in (select keeper_id "
                 + "from `bookings` where `status`='requested' or  `status`='accepted')");
        
           
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                PetKeeper keeper = gson.fromJson(json, PetKeeper.class);
                keepers.add(keeper);
            }
            return keepers;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    
    public ArrayList<PetKeeper> getKeepers(String type) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<PetKeeper> keepers = new ArrayList<PetKeeper>();
        ResultSet rs = null;
        try {
            if("catkeeper".equals(type))
                 rs = stmt.executeQuery("SELECT * FROM petkeepers WHERE catkeeper= '" + "true" + "'");
            else if ("dogkeeper".equals(type))
                  rs = stmt.executeQuery("SELECT * FROM petkeepers WHERE dogkeeper= '" + "true" + "'");

           
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                PetKeeper keeper = gson.fromJson(json, PetKeeper.class);
                keepers.add(keeper);
            }
            return keepers;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    
    public String databasePetKeeperToJSON(String username, String password) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM petkeepers WHERE username = '" + username + "' AND password='"+password+"'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            return json;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }


     public void createPetKeepersTable() throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE petkeepers "
                + "(keeper_id INTEGER not NULL AUTO_INCREMENT, "
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
                + "    property VARCHAR(10) not null,"
                + "    propertydescription VARCHAR(200),"
                + "    catkeeper VARCHAR(10) not null,"
                + "    dogkeeper VARCHAR(10) not null,"
                + "    catprice INTEGER,"
                + "    dogprice INTEGER,"
                + " PRIMARY KEY (keeper_id))";
        stmt.execute(query);
        stmt.close();
    }
    
    
    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void addNewPetKeeper(PetKeeper user) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " petkeepers (username,email,password,firstname,lastname,birthdate,gender,country,city,address,personalpage,"
                    + "job,telephone,lat,lon,property,propertydescription,catkeeper,dogkeeper,catprice,dogprice)"
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
                    + "'" + user.getLon() + "',"
                    + "'" + user.getProperty() + "',"
                    + "'" + user.getPropertydescription()+ "',"
                    + "'" + user.getCatkeeper() + "',"
                    + "'" + user.getDogkeeper() + "',"
                    + "'" + user.getCatprice() + "',"
                    + "'" + user.getDogprice() + "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The pet keeper was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditPetKeepersTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean petKeeperExists(String username, String password) throws SQLException, ClassNotFoundException{
        
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try{
            rs = stmt.executeQuery("select exists(select * from petkeepers where username = '" + username + "' and password ='" + password + "')");
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

    public ArrayList<PetKeeper> databaseToPetKeepers() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<PetKeeper> pet_keepers = new ArrayList<>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM petkeepers");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                PetKeeper pk = gson.fromJson(json, PetKeeper.class);
                pet_keepers.add(pk);
            }
            stmt.close();
            con.close();
            return pet_keepers;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    public int databaseGetPetKeeperId(String username) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        
        ResultSet rs;
        try{
            rs = stmt.executeQuery("SELECT keeper_id FROM petkeepers WHERE username = '" + username + "'");
            int result = -1;
            if(rs.next())
                result = rs.getInt(1);
            stmt.close();
            con.close();
            return result;
        }catch(Exception e){
            System.err.println("EditPetKeepersTable getId says: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return -1;
    }
    
    public String databaseGetPetKeeperUsername(int id) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        
        ResultSet rs;
        try{
            rs = stmt.executeQuery("SELECT username FROM petkeepers WHERE keeper_id = '" + id + "'");
            String result = "";
            if(rs.next())
                result = rs.getString(1);
            stmt.close();
            con.close();
            return result;
        }catch(Exception e){
            System.err.println("EditPetKeepersTable getUsername says: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return "";
    }
    
}
