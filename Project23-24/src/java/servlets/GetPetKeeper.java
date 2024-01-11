/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.tables.EditPetKeepersTable;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.PetKeeper;

/**
 *
 * @author micha
 */
public class GetPetKeeper extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            EditPetKeepersTable eut = new EditPetKeepersTable();
        PetKeeper su = eut.databaseToPetKeeper(username, password);
        if(su==null){
             response.setStatus(403);
        }
        else{
        String json = eut.petKeeperToJSON(su);
        out.println(json); 
            response.setStatus(200);
        }
        } catch (SQLException ex) {
            Logger.getLogger(GetPetKeeper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GetPetKeeper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

}
