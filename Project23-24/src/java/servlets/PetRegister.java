package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.tables.EditPetsTable;
import mainClasses.JSON_Converter;
import org.json.JSONObject;
import database.tables.EditPetOwnersTable;
import java.sql.SQLException;

@WebServlet(name = "PetRegister", urlPatterns = {"/PetRegister"})
public class PetRegister extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSON_Converter jc = new JSON_Converter();
        JSONObject pet = new JSONObject(jc.getJSONFromAjax(request.getReader()));
        
        EditPetsTable ept = new EditPetsTable();
        EditPetOwnersTable epo = new EditPetOwnersTable();
        
        try{
            int owner_id = epo.databaseGetPetOwnerId(pet.getString("username"));
            pet.put("owner_id", owner_id);
            System.out.println("PetRegister Servlet says: pet Json = " + pet.toString());
            ept.addPetFromJSON(pet.toString());
            
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    
}
