package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import database.tables.EditPetOwnersTable;
import database.tables.EditPetKeepersTable;
import mainClasses.JSON_Converter;
import org.json.JSONObject;
import java.sql.SQLException;
import database.tables.EditBookingsTable;

/**
 *
 * @author anton
 */
public class UpdateGetInfo extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSON_Converter jc = new JSON_Converter();
        JSONObject user = new JSONObject(jc.getJSONFromAjax(request.getReader()));
        
        try{
            String res = "";
            if(user.getString("userType").equals("keeper")){
                EditPetKeepersTable epk = new EditPetKeepersTable();
                res = epk.databasePetKeeperToJSON(user.getString("username"), user.getString("password"));
                res = appendKeeperStats(res, epk.databaseGetPetKeeperId(user.getString("username")));
                System.out.println("UpdateGetInfo Servlet says: dummy = " + res);
            }else if(user.getString("userType").equals("owner")){
                EditPetOwnersTable epo = new EditPetOwnersTable();
                res = epo.databasePetOwnerToJSON(user.getString("username"), user.getString("password"));
            }
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(res);
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public String appendKeeperStats(String str, int keeper_id) throws SQLException, ClassNotFoundException{
        EditBookingsTable ebt = new EditBookingsTable();
        String stats = ebt.getKeeperStats(keeper_id).substring(1);
        String pl = str.substring(0, str.length() - 1);
        return pl + "," + stats;
        
    }
    
}
