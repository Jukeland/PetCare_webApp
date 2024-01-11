package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mainClasses.JSON_Converter;
import org.json.JSONObject;
import database.tables.EditPetOwnersTable;
import database.tables.EditPetsTable;
import database.tables.EditPetKeepersTable;
import java.sql.SQLException;
import mainClasses.Pet;
import database.tables.EditBookingsTable;
import java.util.ArrayList;
import mainClasses.Booking;

public class BookRegister extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        JSON_Converter jc = new JSON_Converter();
        JSONObject jo = new JSONObject(jc.getJSONFromAjax(request.getReader()));
        
        EditPetsTable ept = new EditPetsTable();
        EditPetOwnersTable epo = new EditPetOwnersTable();
        EditPetKeepersTable epk = new EditPetKeepersTable();
        EditBookingsTable ebt = new EditBookingsTable();
        
        try{
            int owner_id;
            int keeper_id;
            int pet_id;
            Pet p;
            
            owner_id = epo.databaseGetPetOwnerId(jo.getString("o_username"));
            keeper_id = epk.databaseGetPetKeeperId(jo.getString("k_username"));
            
            ArrayList<Booking> keeper_bookings = ebt.databaseGetBookingFromUserId(keeper_id, "keeper");
            ArrayList<Booking> owner_bookings = ebt.databaseGetBookingFromUserId(owner_id, "owner");
            
            boolean k_flag = false;
            boolean o_flag = false;
            
            for(Booking b : keeper_bookings){
                if(b.getStatus().equals("requested") || b.getStatus().equals("accepted"))
                    k_flag = true;
            }
            
            for(Booking b : owner_bookings){
                if(b.getStatus().equals("requested") || b.getStatus().equals("accepted"))
                    o_flag = true;
            }
            
            if(k_flag || o_flag){
                PrintWriter out = response.getWriter();
                out.print("Booking not successful");
                response.sendError(500);
                return;
            }
            
            p = ept.petOfOwner(Integer.toString(owner_id));
            pet_id = p.getPet_id();
            
            jo.put("owner_id", Integer.toString(owner_id));
            jo.put("pet_id", Integer.toString(pet_id));
            jo.put("keeper_id", Integer.toString(keeper_id));
            
            ebt.addBookingFromJSON(jo.toString());
            
            
            
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        
    }
    

}
