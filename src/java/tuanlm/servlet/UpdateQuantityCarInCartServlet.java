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
import java.util.List;
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
import tuanlm.dao.CarsDAO;
import tuanlm.dao.OrderDetailsDAO;
import tuanlm.dao.OrderHistoryDAO;
import tuanlm.dto.CarsDTO;
import tuanlm.dto.OrderDetailsDTO;
import tuanlm.model.CartObject;

/**
 *
 * @author MINH TUAN
 */
@WebServlet(name = "UpdateQuantityCarInCartServlet", urlPatterns = {"/UpdateQuantityCarInCartServlet"})
public class UpdateQuantityCarInCartServlet extends HttpServlet {

    private final String ONLOAD_CART = "OnloadCartPageServlet";
    private final String LOGIN_PAGE = "login.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = LOGIN_PAGE;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String name = request.getParameter("txtName");
        String dateFromText = request.getParameter("txtDateFrom");
        String dateToText = request.getParameter("txtDateTo");
        String quantityText = request.getParameter("txtQuantity");

        try {
            //
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("CUST_CART") != null) {
                url = ONLOAD_CART;
                CartObject cart = (CartObject) session.getAttribute("CUST_CART");
                if (quantityText != null && quantityText.matches("[0-9]{1,}")
                        && dateFromText != null && dateToText != null
                        && dateFromText.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}")
                        && dateToText.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}")) {

                    Integer quantityNum = Integer.parseInt(quantityText);
                    Date dateFrom = sdf.parse(dateFromText);
                    Date dateTo = sdf.parse(dateToText);

                    if (quantityNum > 0) {
                        CarsDAO carsDAO = new CarsDAO();
                        Integer maxQuantity = carsDAO.getQuantityForCar(name);

                        //check number rented car inDB
                        OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAO();
                        OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO();
                        CarsDTO carsDTO = new CarsDTO();
                        carsDTO.setName(name);
                        List<OrderDetailsDTO> detailsList = orderDetailsDAO.getCarListByCarNameOrCategory(carsDTO);
                        if (detailsList != null && detailsList.size() > 0) {

                            int count = 0;
                            while (count < detailsList.size()) {
                                if (!orderHistoryDAO.checkActiveOfOrder(detailsList.get(count).getOrderId())) {
                                    detailsList.remove(count);
                                    count--;
                                }
                                count++;
                            }

                            //getAvailable quantity  after substract quantity in history detail
                            for (OrderDetailsDTO orderDetailsDTO : detailsList) {
                                if (orderDetailsDTO.getDateFrom().getTime() > dateTo.getTime()
                                        || orderDetailsDTO.getDateTo().getTime() < dateFrom.getTime()) {

                                } else {
                                    maxQuantity -= orderDetailsDTO.getQuantity();
                                }
                            }
                        }

                        String status = cart.updateCarQuantity(name, dateFrom, dateTo, maxQuantity, quantityNum);
                        if (status.equals("notEnough")) {
                            url += "?Status=" + "Not enough available quantity";
                        }
                    } 
                    else if (quantityNum == 0) {
                        cart.deleteFromCart(name, dateFrom, dateTo);
                    }
                } 
                else {
                    url += "?Status=" + "Quantity must be a integer number";
                }

            }

        } catch (ParseException ex) {
            Logger.getLogger(UpdateQuantityCarInCartServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UpdateQuantityCarInCartServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(UpdateQuantityCarInCartServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
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
