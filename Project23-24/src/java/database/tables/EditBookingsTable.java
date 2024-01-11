/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.tables;

import com.google.gson.Gson;
import mainClasses.Booking;
import database.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import org.json.JSONObject;
import java.time.LocalDate;

/**
 *
 * @author Mike
 */
public class EditBookingsTable {

    public void addBookingFromJSON(String json) throws ClassNotFoundException {
        Booking r = jsonToBooking(json);
        createNewBooking(r);
    }
    
    public String getKeeperStats(int keeper_id) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        
        ResultSet rs;
        JSONObject jo = new JSONObject();
        
        int number_of_acc = 0;
        int total_days = 0;
        
        try{
            
            rs = stmt.executeQuery("SELECT * FROM bookings WHERE keeper_id= '" + keeper_id + "' AND status= 'finished'");
            while(rs.next()){
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Booking book = gson.fromJson(json, Booking.class);
                number_of_acc++;
                LocalDate fromDate = LocalDate.parse(book.getFromDate());
                LocalDate toDate = LocalDate.parse(book.getToDate());
                if(fromDate.getYear() != toDate.getYear())
                    total_days += 365 * (toDate.getYear() - fromDate.getYear());
                if(fromDate.getMonthValue() != toDate.getMonthValue())
                    total_days += 30 * (toDate.getMonthValue() - fromDate.getMonthValue());
                if(fromDate.getDayOfMonth() != toDate.getDayOfMonth())
                    total_days += toDate.getDayOfMonth() - fromDate.getDayOfMonth();
            }
            
            
            jo.put("num_of_acc", Integer.toString(number_of_acc));
            jo.put("total_days", Integer.toString(total_days));
            
            System.out.println("\n=== EditBookingsTable -> getStats says: jo = " + jo.toString() + " ===\n");
            
            return jo.toString();
            
        }catch (Exception e) {
            System.err.println("EditBookingsTable -> getKeepersStats: Got an exception! ");
            System.err.println(e.getMessage());
        }
        
        return "\"num_of_acc\":\"0\", \"total_days\":\"0\"";
        
    }
    
    public ArrayList<Booking> databaseGetBookingFromUserId(int uid, String userType) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        
        ArrayList<Booking> bookings = new ArrayList<>();
        ResultSet rs;
        try {
            if(userType.equals("owner")){
                rs = stmt.executeQuery("SELECT * FROM bookings WHERE owner_id= '" + uid + "'");
            }else{
                rs = stmt.executeQuery("SELECT * FROM bookings WHERE keeper_id= '" + uid + "'");
            }
            
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Booking book = gson.fromJson(json, Booking.class);
                bookings.add(book);
            }
            
            con.close();
            stmt.close();
            return bookings;
        } catch (Exception e) {
            System.err.println("EditBookingsTable -> getBookingFromUserId: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Booking databaseToBooking(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bookings WHERE booking_id= '" + id + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Booking bt = gson.fromJson(json, Booking.class);
            return bt;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Booking jsonToBooking(String json) {
        Gson gson = new Gson();
        Booking r = gson.fromJson(json, Booking.class);
        return r;
    }

    public String bookingToJSON(Booking r) {
        Gson gson = new Gson();

        String json = gson.toJson(r, Booking.class);
        return json;
    }

    public void updateBooking(int bookingID,  String status) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String updateQuery = "UPDATE bookings SET status='"+status+"' WHERE booking_id= '"+bookingID+"'";
        stmt.executeUpdate(updateQuery);
        stmt.close();
        con.close();
    }

    public void createBookingTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE bookings "
                + "(booking_id INTEGER not NULL AUTO_INCREMENT, "
                + " owner_id INTEGER not NULL, "
                + "  pet_id VARCHAR(10) not NULL, "
                + " keeper_id INTEGER not NULL, "
                + " fromdate DATE not NULL, "
                + " todate DATE not NULL, "
                + " status VARCHAR(15) not NULL, "
                + " price INTEGER not NULL, "
                + "FOREIGN KEY (owner_id) REFERENCES petowners(owner_id), "
                + "FOREIGN KEY (pet_id) REFERENCES pets(pet_id), "
                + "FOREIGN KEY (keeper_id) REFERENCES petkeepers(keeper_id), "
                + " PRIMARY KEY (booking_id))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void createNewBooking(Booking bor) throws ClassNotFoundException {
        try {
            
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " bookings (owner_id,pet_id,keeper_id,fromdate,todate,status,price)"
                    + " VALUES ("
                    + "'" + bor.getOwner_id() + "',"
                    + "'" + bor.getPet_id() + "',"
                     + "'" + bor.getKeeper_id()+ "',"
                    + "'" + bor.getFromDate() + "',"
                    + "'" + bor.getToDate() + "',"
                    + "'" + bor.getStatus() + "',"
                     + "'" + bor.getPrice() + "'"
                    + ")";
            //stmt.execute(table);

            stmt.executeUpdate(insertQuery);
            System.out.println("# The booking was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditBookingsTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Booking> databaseToBookings() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Booking> bookings = new ArrayList<>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bookings");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Booking book = gson.fromJson(json, Booking.class);
                bookings.add(book);
            }
            stmt.close();
            con.close();
            return bookings;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
}
