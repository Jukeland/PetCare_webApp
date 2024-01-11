package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.tables.EditReviewsTable;
import database.tables.EditBookingsTable;
import java.sql.SQLException;
import mainClasses.JSON_Converter;
import org.json.JSONObject;
import mainClasses.Booking;

public class ReviewRegister extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        JSON_Converter jc = new JSON_Converter();
        JSONObject jo = new JSONObject(jc.getJSONFromAjax(request.getReader()));
        
        EditReviewsTable ert = new EditReviewsTable();
        EditBookingsTable ebt = new EditBookingsTable();
        try{
            
            Booking b = ebt.databaseToBooking(jo.getInt("booking_id"));
            jo.put("owner_id", b.getOwner_id());
            jo.put("keeper_id", b.getKeeper_id());
            ert.addReviewFromJSON(jo.toString());
            
        }catch(SQLException | ClassNotFoundException e){
            response.setStatus(403);
            e.printStackTrace();
        }
        
      
        
        
    }

}
