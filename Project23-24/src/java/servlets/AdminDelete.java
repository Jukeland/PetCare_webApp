/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.tables.EditPetOwnersTable;
import database.tables.EditPetKeepersTable;
import org.json.JSONObject;
import mainClasses.JSON_Converter;
import java.sql.SQLException;

/**
 *
 * @author anton
 */
@WebServlet(name = "AdminDelete", urlPatterns = {"/AdminDelete"})
public class AdminDelete extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSON_Converter jc = new JSON_Converter();
        JSONObject user = new JSONObject(jc.getJSONFromAjax(request.getReader()));
        
        try{
            if(user.getString("userType").equals("owner")){
                
                EditPetOwnersTable epo = new EditPetOwnersTable();
                System.out.println("adminDel owner: id = " + user.getInt("id"));
                epo.deletePetOwner(user.getInt("id"));
                
            }else if(user.getString("userType").equals("keeper")){
                
                EditPetKeepersTable epk = new EditPetKeepersTable();
                System.out.println("adminDel keeper: id = " + user.getInt("id"));
                epk.deletePetKeeper(user.getInt("id"));
                
            }
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

}
