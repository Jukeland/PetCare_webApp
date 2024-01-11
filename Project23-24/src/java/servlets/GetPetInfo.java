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
import java.sql.SQLException;
import mainClasses.Pet;


public class GetPetInfo extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        JSON_Converter jc = new JSON_Converter();
        JSONObject user = new JSONObject(jc.getJSONFromAjax(request.getReader()));
        
        try{
            EditPetOwnersTable epo = new EditPetOwnersTable();
            int owner_id = epo.databaseGetPetOwnerId(user.getString("username"));
            
            EditPetsTable ept = new EditPetsTable();
            Pet pet = ept.petOfOwner(Integer.toString(owner_id));
            String res = ept.petToJSON(pet);
            PrintWriter out = response.getWriter();
            out.print(res);
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        
    }

}
