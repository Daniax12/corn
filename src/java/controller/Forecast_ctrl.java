/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import mapping.BddObject;
import model.Harvest;
import model.Harvest_data;
import model.Npk;
import model.Parcel;

/**
 *
 * @author rango
 */
@WebServlet(name = "Forecast_ctrl", urlPatterns = {"/Forecast_ctrl"})
public class Forecast_ctrl extends HttpServlet {

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
            out.println("<title>Servlet Forecast_ctrl</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Forecast_ctrl at " + request.getContextPath() + "</h1>");
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
      
      String harvest_id = request.getParameter("harvest_id");
      
        try {
            Harvest harvest = new Harvest();
            harvest.setId_harvest(harvest_id);
            
            // GET THE HARVEST
            harvest = BddObject.findById("harvest", harvest, null);
            request.setAttribute("harvest", harvest);
            
            // SEND ALL PARCELS NOT IN DB
            List<Parcel> parcel_not_in_base = harvest.parcels_not_in_harvest(null);
            request.setAttribute("parcels", parcel_not_in_base);
            
            // SEND ALL HARVEST_DATA
            Harvest_data hd = new Harvest_data();
            hd.setId_harvest(harvest_id);
            List<Harvest_data> datas = BddObject.find("harvest_data", hd, null);
            request.setAttribute("data", datas);
            
            // SEND ALL PRODUCTION GOT
            float alreasy_got = harvest.get_sum_harvest(null);
            request.setAttribute("got", alreasy_got);
            
            // SEND THE FORECAST
            float forecast = harvest.get_forecast(null);
            request.setAttribute("forecast", forecast);
            
            // SEND ALL FERTILIZER RANK
            HashMap<String, Float> fertilizer_rank = harvest.fertilizer_rank();
            List<Npk> npk_rank = harvest.npk_rank();
            
            request.setAttribute("npk_rank", npk_rank);
            request.setAttribute("npk_hash", fertilizer_rank);
            
            // PARCEL RANK
            List<Parcel> parcel_rank = harvest.rank_parcel_ratio(null);
            request.setAttribute("parcel_rank", parcel_rank);
            
            // NPK RANK PRICE
            List<Npk> npk_price_rank = harvest.npk_rank_qty_price();
            request.setAttribute("npk_price_rank", npk_price_rank);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        } finally{
            RequestDispatcher dispat = request.getRequestDispatcher("home.jsp?page=forecast");
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
        processRequest(request, response);
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
