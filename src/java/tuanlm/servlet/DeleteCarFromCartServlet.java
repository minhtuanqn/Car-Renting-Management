/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tuanlm.model.CartObject;

/**
 *
 * @author MINH TUAN
 */
@WebServlet(name = "DeleteCarFromCartServlet", urlPatterns = {"/DeleteCarFromCartServlet"})
public class DeleteCarFromCartServlet extends HttpServlet {

    private final String ONLOAD_CART = "OnloadCartPageServlet";
    private final String LOGIN_PAGE = "login.jsp";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String name = request.getParameter("txtName");
        String dateTextFrom = request.getParameter("txtDateFrom");
        String dateTextTo = request.getParameter("txtDateTo");
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String url = LOGIN_PAGE;
        try {
            
            
            HttpSession session = request.getSession(false);
            if(session != null && session.getAttribute("CUST_CART") != null) {
                url = ONLOAD_CART;
                CartObject cart = (CartObject) session.getAttribute("CUST_CART");
                if(cart != null) {
                    if(dateTextFrom != null && dateTextFrom.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}")
                            && dateTextTo != null && dateTextTo.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}")) {
                        Date dateFrom = sdf.parse(dateTextFrom);
                        Date dateTo = sdf.parse(dateTextTo);
                        cart.deleteFromCart(name, dateFrom, dateTo);
                    }
                }
                session.setAttribute("CUST_CART", cart);
                
            }
        }
        catch (ParseException ex) {
            Logger.getLogger(DeleteCarFromCartServlet.class.getName()).log(Level.SEVERE, null, ex);
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
