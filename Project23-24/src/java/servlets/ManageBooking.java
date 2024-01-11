package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.tables.EditPetKeepersTable;
import database.tables.EditPetOwnersTable;
import database.tables.EditPetsTable;
import database.tables.EditBookingsTable;
import java.sql.SQLException;
import java.util.ArrayList;
import mainClasses.JSON_Converter;
import mainClasses.Booking;
import mainClasses.Pet;
import mainClasses.PetOwner;
import mainClasses.PetKeeper;
import org.json.JSONObject;
import org.json.JSONArray;

public class ManageBooking extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try{
            
            JSON_Converter jc = new JSON_Converter();
            JSONObject data = new JSONObject(jc.getJSONFromAjax(request.getReader()));
            
            EditBookingsTable ebt = new EditBookingsTable();
            EditPetOwnersTable epo = new EditPetOwnersTable();
            EditPetKeepersTable epk = new EditPetKeepersTable();
            
            ArrayList<Booking> bookings;
            
            int owner_id;
            int keeper_id;
            JSONArray list = new JSONArray();
            
            
            if(data.getString("userType").equals("owner")){

                owner_id = epo.databaseGetPetOwnerId(data.getString("username"));

                bookings = ebt.databaseGetBookingFromUserId(owner_id, data.getString("userType"));
                
                for(Booking b : bookings){
                    list.put("\"username\":\"" + epk.databaseGetPetKeeperUsername(b.getKeeper_id()) + "\", \"fromdate\":\"" + b.getFromDate() + "\", \"todate\":\"" + b.getToDate() + "\", \"price\":\"" + b.getPrice() + "\", \"status\":\"" + b.getStatus() + "\", \"booking_id\":\"" + b.getBooking_id() + "\"");
                }
                
            }else if(data.getString("userType").equals("keeper")){

                keeper_id = epk.databaseGetPetKeeperId(data.getString("username"));
                
                bookings = ebt.databaseGetBookingFromUserId(keeper_id, data.getString("userType"));
                
                for(Booking b : bookings){
                    list.put("\"username\":\"" + epo.databaseGetPetOwnerUsername(b.getOwner_id()) + "\", \"fromdate\":\"" + b.getFromDate() + "\", \"todate\":\"" + b.getToDate() + "\", \"price\":\"" + b.getPrice() + "\", \"status\":\"" + b.getStatus() + "\", \"booking_id\":\"" + b.getBooking_id() + "\"");
                }
                
            }   
            System.out.println("\n=== ManageBooking Servlet says: list = " + list + " ===\n");
            PrintWriter out = response.getWriter();
            out.print(list);
            
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        
    }

}
