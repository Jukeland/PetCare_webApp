/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mainClasses.JSON_Converter;
import org.json.JSONObject;
import database.tables.EditReviewsTable;
import database.tables.EditPetKeepersTable;
import database.tables.EditPetOwnersTable;
//import database.tables.EditBookingsTable;
import mainClasses.Review;
//import mainClasses.Booking;
import org.json.JSONArray;
import java.util.ArrayList;

public class GetReview extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        JSON_Converter jc = new JSON_Converter();
        JSONObject data = new JSONObject(jc.getJSONFromAjax(request.getReader()));
        
        ArrayList<Review> reviews;
        JSONArray list = new JSONArray();
        try{
            
            EditReviewsTable ert = new EditReviewsTable();
            EditPetKeepersTable epk = new EditPetKeepersTable();
            EditPetOwnersTable epo = new EditPetOwnersTable();
            //EditBookingsTable ebt = new EditBookingsTable();
            
            if(data.getString("userType").equals("keeper")){
                
                int keeper_id = epk.databaseGetPetKeeperId(data.getString("username"));
                reviews  = ert.databaseTokeeperReviews(Integer.toString(keeper_id));  

                for(Review r : reviews){
                    list.put("\"username\":\"" + epo.databaseGetPetOwnerUsername(r.getOwner_id()) + "\", \"review\":\"" + r.getReviewText() + "\", \"review_score\":\"" + r.getReviewScore() + "\"" );
                }

                System.out.println("\n=== GetReview Servlet says: list = " + list + " ===\n");

                PrintWriter out = response.getWriter();
                out.print(list);
                
            }
            /*
            else{
                
                Booking b = ebt.databaseToBooking(data.getInt("booking_id"));
                int keeper_id = b.getKeeper_id();
                int owner_id = b.getOwner_id();
             
                if(ert.review_exists(keeper_id, owner_id))
                    response.setStatus(403);
                else
                    response.setStatus(200);
                
            }*/
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        
    }

}
