/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import mapping.BddObject;
import model.Check_parcel;
import model.Parcel;
import utilities.Ordering;

/**
 *
 * @author rango
 */
@WebServlet(name = "Checking_parcel_ctrl", urlPatterns = {"/Checking_parcel_ctrl"})
public class Checking_parcel_ctrl extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Checking_parcel_ctrl</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Checking_parcel_ctrl at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      //  processRequest(request, response);
      
        try {
            // Trait the parcel
            String id_parcel = request.getParameter("parcel_id");
            List<Parcel> parcels = BddObject.findByOrder("v_parcel_responsible", new Parcel(), "id_parcel", Ordering.ASC, null);
            request.setAttribute("parcels", parcels);
            
            Parcel parcel = new Parcel();
            if(id_parcel == null){
                id_parcel = parcels.get(0).getId_parcel();
            } 
            parcel.setId_parcel(id_parcel);
            parcel = BddObject.findById("v_parcel_responsible", parcel, null);
            request.setAttribute("parcel", parcel);
            
            // Trait all checking of the parcelt
            Check_parcel cp = new Check_parcel();
            cp.setId_parcel(id_parcel);
            
            List<Check_parcel> all_check = BddObject.findByOrder("check_parcel", cp, "date_check", Ordering.DESC, null);
            request.setAttribute("checks", all_check);
                
            // Get the checking anomalies 
            String id_checking_data = request.getParameter("checking_data_id");
            if(id_checking_data == null && all_check.size() > 0){
                id_checking_data = all_check.get(0).getId_check_parcel();
            }
            Check_parcel main_check = new Check_parcel();
            main_check.setId_check_parcel(id_checking_data);
            main_check = BddObject.findById("check_parcel", main_check, null);
            request.setAttribute("main_check", main_check);
            
            HashMap<String, String[]> anomalies = main_check.get_anomalies(null);
            request.setAttribute("anomalies", anomalies);
            
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        } finally{
            RequestDispatcher dispat = request.getRequestDispatcher("home.jsp?page=checking_data");
            dispat.forward(request, response);
        }
      
      
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       // processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
