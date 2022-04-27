/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.servlet;

import java.io.IOException;
import java.sql.SQLException;
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
import tuanlm.dao.OrderHistoryDAO;

/**
 *
 * @author MINH TUAN
 */
@WebServlet(name = "DeleteHistoryServlet", urlPatterns = {"/DeleteHistoryServlet"})
public class DeleteHistoryServlet extends HttpServlet {

    private final String LOGIN_PAGE = "login.jsp";
    private final String ONLOAD_HISTORY = "OnloadHistoryServlet";
    private final String NOT_FOUND = "notfound.jsp";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = LOGIN_PAGE;
        String orderId = request.getParameter("txtOrderId");
        try {
            HttpSession session = request.getSession(false);
            if(session != null) {
                url = ONLOAD_HISTORY;
                if(orderId.matches("[0-9]{1,}")) {
                    Integer orderIdNum = Integer.parseInt(orderId);
                    OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO();
                    boolean checkDelete = orderHistoryDAO.deleteHistory(orderIdNum);
                    if(checkDelete) {
                        
                    }
                    else {
                        url = NOT_FOUND;
                    }
                }
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(DeleteHistoryServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NamingException ex) {
            Logger.getLogger(DeleteHistoryServlet.class.getName()).log(Level.SEVERE, null, ex);
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
