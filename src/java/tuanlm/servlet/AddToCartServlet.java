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
import tuanlm.model.ItemObject;

/**
 *
 * @author MINH TUAN
 */
@WebServlet(name = "AddToCartServlet", urlPatterns = {"/AddToCartServlet"})
public class AddToCartServlet extends HttpServlet {

    private final String LOGIN_PAGE = "login.jsp";
    private final String SEARCH_SERVLET = "SearchServlet";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = LOGIN_PAGE;
        String nameAdd = request.getParameter("txtNameAdd");
        String dateFromTextSearch = request.getParameter("txtDateFrom");
        String dateToTextSearch = request.getParameter("txtDateTo");
        String amountSearch = request.getParameter("txtAmount");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("EMAIL") == null) {

            } else {
                if (dateFromTextSearch != null && dateToTextSearch != null
                        && !dateFromTextSearch.equals("") && !dateToTextSearch.equals("")
                        && amountSearch.matches("[0-9]{1,}")) {
                    Date dateFrom = sdf.parse(dateFromTextSearch);
                    Date dateTo = sdf.parse(dateToTextSearch);
                    if (session.getAttribute("EMAIL") != null
                            && session.getAttribute("STATUS") != null
                            && ((String) session.getAttribute("STATUS")).equals("ACTIVE")) {

                        CartObject cart = (CartObject) session.getAttribute("CUST_CART");
                        if (cart == null) {
                            cart = new CartObject();
                        }

                        CarsDAO carsDAO = new CarsDAO();
                        int maxQuantity = carsDAO.getQuantityForCar(nameAdd);

                        //check number rented car inDB
                        OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAO();
                        OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO();
                        CarsDTO carsDTO = new CarsDTO();
                        carsDTO.setName(nameAdd);
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

                        ItemObject itemObject = new ItemObject(nameAdd, dateFrom, dateTo);
                        cart.addToCart(itemObject, maxQuantity);
                        session.setAttribute("CUST_CART", cart);
                        url = SEARCH_SERVLET;
                    }
                } 
                else {
                    url = SEARCH_SERVLET;
                    request.setAttribute("NOT_SEARCH", "Please search category or name of car, amount of car and time for renting");
                }

            }

        } catch (ParseException ex) {
            Logger.getLogger(AddToCartServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AddToCartServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(AddToCartServlet.class.getName()).log(Level.SEVERE, null, ex);
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
