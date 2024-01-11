/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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
import database.tables.EditPetKeepersTable;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author anton
 */
public class Update extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSON_Converter jc = new JSON_Converter();
        JSONObject user = new JSONObject(jc.getJSONFromAjax(request.getReader()));
   
        try{
            if(user.getString("userType").equals("keeper")){
                EditPetKeepersTable epk = new EditPetKeepersTable();
                epk.updatePetKeeper(user);
            }else if(user.getString("userType").equals("owner")){
                EditPetOwnersTable epo = new EditPetOwnersTable();
                epo.updatePetOwner(user);
            }
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

}
