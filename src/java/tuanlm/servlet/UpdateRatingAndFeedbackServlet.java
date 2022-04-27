/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tuanlm.dao.OrderDetailsDAO;

/**
 *
 * @author MINH TUAN
 */
@WebServlet(name = "UpdateRatingAndFeedbackServlet", urlPatterns = {"/UpdateRatingAndFeedbackServlet"})
public class UpdateRatingAndFeedbackServlet extends HttpServlet {

    private final String ONLOAD_HISTORY = "OnloadHistoryServlet";
    private final String LOGIN_PAGE = "login.jsp";
    private final String NOT_FOUND = "notfound.jsp";
    private final String SEARCH_SERVLET = "SearchHistoryServlet";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = LOGIN_PAGE;
        String ratingText = request.getParameter("cbbRating");
        Integer ratingNum = null;
        String feedback = request.getParameter("txtFeedback");
        String orderId = request.getParameter("txtId");
        String carId = request.getParameter("txtCarId");
        String dateFromText = request.getParameter("txtDateFrom");
        String dateToText = request.getParameter("txtDateTo");
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        try  {
            HttpSession session = request.getSession(false);
            if(session != null) {
                url = ONLOAD_HISTORY;
                if(ratingText == null || (!ratingText.equals("-1") && 
                        !ratingText.matches("[0-9]") && !ratingText.equals("10")) || 
                        dateFromText == null || dateToText == null
                        || !dateFromText.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}")
                        || !dateToText.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}")
                        || orderId == null || !orderId.matches("[0-9]{1,}")) {
                    
                }
                else {
                    ratingNum = Integer.parseInt(ratingText);
                    if(feedback.equals("")) {
                        feedback = null;
                    }
                    Date dateFrom = sdf.parse(dateFromText);
                    Date dateTo = sdf.parse(dateToText);
                    Integer orderIdNum = Integer.parseInt(orderId);
                    OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAO();
                    boolean checkUpdate = orderDetailsDAO.updateRatingAndFeedback(ratingNum, feedback, orderIdNum, dateFrom, dateTo, carId);
                    if(checkUpdate) {
                        url = SEARCH_SERVLET;
                    }
                    else {
                        url = NOT_FOUND;
                    }
                }
            }
        }
        catch (ParseException ex) {
            Logger.getLogger(UpdateRatingAndFeedbackServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NamingException ex) {
            Logger.getLogger(UpdateRatingAndFeedbackServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SQLException ex) {
            Logger.getLogger(UpdateRatingAndFeedbackServlet.class.getName()).log(Level.SEVERE, null, ex);
        }        
        finally {
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
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
        processRequest(request, response);
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
