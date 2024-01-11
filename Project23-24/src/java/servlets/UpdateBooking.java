package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mainClasses.JSON_Converter;
import org.json.JSONObject;
import database.tables.EditBookingsTable;
import java.sql.SQLException;

public class UpdateBooking extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSON_Converter jc = new JSON_Converter();
        JSONObject data = new JSONObject(jc.getJSONFromAjax(request.getReader()));
        
        try{
            
            EditBookingsTable ebt = new EditBookingsTable();
            if(!ebt.databaseToBooking(data.getInt("booking_id")).getStatus().equals("rejected") && !ebt.databaseToBooking(data.getInt("booking_id")).getStatus().equals("finished"))
                ebt.updateBooking(data.getInt("booking_id"), data.getString("new_status"));
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        
    }

}
