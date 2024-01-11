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
import mainClasses.PetKeeper;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 *
 * @author anton
 */
public class AvailableKeepers extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try{
            
            JSON_Converter jc = new JSON_Converter();
            JSONObject user = new JSONObject(jc.getJSONFromAjax(request.getReader()));
            
            EditPetOwnersTable epo = new EditPetOwnersTable();
            String username = user.getString("username");          
            int owner_id = epo.databaseGetPetOwnerId(username);
            
            ArrayList<PetKeeper> all_keepers;
            ArrayList<PetKeeper> available_keepers = new ArrayList<>();
            ArrayList<Pet> pets;
            ArrayList<Booking> bookings;
            EditPetKeepersTable epk = new EditPetKeepersTable();
            EditPetsTable ept = new EditPetsTable();
            EditBookingsTable ebt = new EditBookingsTable();
            JSONArray list = new JSONArray();
            
            String pet_type = "";
            String price = "";
            boolean is_found;
            
            all_keepers = epk.databaseToPetKeepers();
            pets = ept.databaseToPets();
            bookings = ebt.databaseToBookings();

            for(Pet p : pets){
                if(owner_id == p.getOwner_id()){
                    pet_type = p.getType();
                    break;
                }
            }

            for(PetKeeper pk : all_keepers){
                is_found = false;
                if((pet_type.equals("cat") && pk.getCatkeeper().equals("false")) || (pet_type.equals("dog") && pk.getDogkeeper().equals("false")))
                    continue;
                for(Booking b : bookings){
                    if(pk.getId() == b.getKeeper_id()){
                        is_found = true;
                        if((!b.getStatus().equals("requested") && !b.getStatus().equals("accepted")))   
                            available_keepers.add(pk);
                    }
                }
                if(!is_found)
                    available_keepers.add(pk);
            }
            
            available_keepers = remove_duplicates(available_keepers);
            
            for(PetKeeper pk : available_keepers){
                if(pet_type.equals("cat"))
                    price = "Cat: " + pk.getCatprice() + "€";
                else if(pet_type.equals("dog"))
                    price = "Dog: " + pk.getDogprice() + "€";
                list.put("\"username\":\"" + pk.getUsername() + "\", \"address\":\"" + pk.getAddress() + "\", \"price\":\"" + price + "\"");
            }
            
            System.out.println("\n=== AvailableKeepers Servlet says: list = " + list + " ===\n");
            
            PrintWriter out = response.getWriter();
            out.print(list);
            
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        
    }

    public ArrayList<PetKeeper> remove_duplicates(ArrayList<PetKeeper> keepers){
        ArrayList<PetKeeper> newList = new ArrayList<>();
        for(PetKeeper p : keepers){
            if(!newList.contains(p))
                newList.add(p);
        }
        return newList;
    }

}
