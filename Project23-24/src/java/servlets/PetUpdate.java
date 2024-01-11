package servlets;

import database.tables.EditPetOwnersTable;
import database.tables.EditPetsTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mainClasses.JSON_Converter;
import mainClasses.Pet;
import org.json.JSONObject;

public class PetUpdate extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        JSON_Converter jc = new JSON_Converter();
        JSONObject pet = new JSONObject(jc.getJSONFromAjax(request.getReader()));
        
        try{
            EditPetOwnersTable epo = new EditPetOwnersTable();
            int owner_id = epo.databaseGetPetOwnerId(pet.getString("username"));
            
            pet.put("owner_id", owner_id);
            
            EditPetsTable ept = new EditPetsTable();
            ept.updatePet(pet);
            
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        
    }
    
}
