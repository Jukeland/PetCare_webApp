package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import database.tables.EditPetKeepersTable;
import database.tables.EditPetOwnersTable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import mainClasses.JSON_Converter;
import mainClasses.PetKeeper;
import mainClasses.PetOwner;



public class Register extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSON_Converter jc = new JSON_Converter();
        String user = jc.getJSONFromAjax(request.getReader());
        String userType = "";
        
        if(user.contains("Keeper")){
            userType = "keeper";
        }else if(user.contains("Owner")){
            userType = "owner";
        }
        
        //user = appendLatLon(user);
        if(userType.equals("keeper")){
            user = appendAnimal(user);
        }

        if(userType.equals("keeper")){
            EditPetKeepersTable epk = new EditPetKeepersTable();
            try{
                epk.addPetKeeperFromJSON(user);
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }else if(userType.equals("owner")){
            EditPetOwnersTable epo = new EditPetOwnersTable();
            try{
                epo.addPetOwnerFromJSON(user);
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }

        // PrintWriter out = response.getWriter();
        //
        // if (Resources.registeredUsers.containsKey(p.getUsername())) {
        //     response.setStatus(409);
        //     Gson gson = new Gson();
        //     JsonObject jo = new JsonObject();
        //     jo.addProperty("error", "Username Already Taken");
        //     response.getWriter().write(jo.toString());
        // } else {
        //     Resources.registeredUsers.put(p.getUsername(), p);
        //     response.setStatus(200);
        //     response.getWriter().write(JsonString);
  
        // }
    }
    
    //private String appendLatLon(String str){
    //    String pl = str.substring(0, str.length() - 1);
    //    return pl + ",\"lat\":\"0\",\"lon\":\"0\"}";
    //}
    
    private String appendAnimal(String str){
        String pl = str.substring(0, str.length() - 1);
        if(str.contains("\"animal\":\"cat\"")){
            return pl + ",\"catkeeper\":\"true\",\"dogkeeper\":\"false\"}";
        }else if(str.contains("\"animal\":\"dog\"")){
            return pl + ",\"catkeeper\":\"false\",\"dogkeeper\":\"true\"}";
        }else if(str.contains("\"animal\":\"both\"")){
            return pl + ",\"catkeeper\":\"true\",\"dogkeeper\":\"true\"}";
        }else
            return "error";
    }

}
