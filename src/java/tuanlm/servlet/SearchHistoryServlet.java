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
import java.util.ArrayList;
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
import tuanlm.dao.DiscountsDAO;
import tuanlm.dao.OrderDetailsDAO;
import tuanlm.dao.OrderHistoryDAO;
import tuanlm.dto.DiscountsDTO;
import tuanlm.dto.OrderDetailsDTO;
import tuanlm.dto.OrderHistoryDTO;

/**
 *
 * @author MINH TUAN
 */
@WebServlet(name = "SearchHistoryServlet", urlPatterns = {"/SearchHistoryServlet"})
public class SearchHistoryServlet extends HttpServlet {

    private final String HISTORY_PAGE = "history.jsp";
    private final String ONLOAD_PAGE = "OnloadHistoryServlet";
    private final String LOGIN_PAGE = "login.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = LOGIN_PAGE;
        String rentingDateText = request.getParameter("txtRentingDate");
        String returnDateText = request.getParameter("txtReturnDate");
        String name = request.getParameter("txtOrderId");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("EMAIL") != null) {
                String email = (String) session.getAttribute("EMAIL");

                if ((rentingDateText == null || rentingDateText.length() == 0)
                        && (returnDateText == null || rentingDateText.length() == 0) &&(name == null || name.equals(""))) {
                    url = ONLOAD_PAGE;
                } 
                else {
                    url = HISTORY_PAGE;

                    OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAO();
                    OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO();
                    DiscountsDAO discountsDAO = new DiscountsDAO();
                    CarsDAO carsDAO = new CarsDAO();
                    List<OrderHistoryDTO> orderHeaders = new ArrayList<>();
                    List<Integer> orderIdList = new ArrayList<>();

                    boolean checkValid = false;
                    if ((rentingDateText != null && rentingDateText.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")
                            && returnDateText != null && returnDateText.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}"))) {
                        Date rentingDate = sdf.parse(rentingDateText);
                        Date returnDate = sdf.parse(returnDateText);
                        orderIdList = orderDetailsDAO.searchHistoryByDate(rentingDate, returnDate);
                        checkValid = true;

                    }
                    if (name.length() > 0) {
                        orderIdList = orderDetailsDAO.searchHistoryLikeId(name, orderIdList);
                        checkValid = true;
                    }

                    if (checkValid == true && orderIdList.size() > 0) {
                        for (Integer integer : orderIdList) {

                            OrderHistoryDTO dto = orderHistoryDAO.getOrderHeaderByEmailAndId(email, integer);
                            if (dto != null) {
                                orderHeaders.add(dto);
                            }

                        }

                        if (orderHeaders.size() > 0) {
                            //set discount percent
                            for (OrderHistoryDTO orderHeader : orderHeaders) {
                                if (orderHeader.getDiscountId() != null && !orderHeader.getDiscountId().equals("")) {
                                    DiscountsDTO discountInfo = discountsDAO.getDiscountInfo(orderHeader.getDiscountId());
                                    orderHeader.setPercenOfDiscount(discountInfo.getPercenOfDiscount());
                                }
                            }

                            List headerAndDetails = new ArrayList<>();
                            for (OrderHistoryDTO orderHeader : orderHeaders) {
                                List<OrderDetailsDTO> details = orderDetailsDAO.getDetailByHeaderId(orderHeader.getId());
                                if (details != null) {
                                    for (OrderDetailsDTO detail : details) {
                                        String carCategory = carsDAO.getCarCategoryByName(detail.getCarId());
                                        detail.setCategory(carCategory);
                                        long numberOfRentDate = (detail.getDateTo().getTime() - detail.getDateFrom().getTime()) / 1000 / 60 / 60 / 24;
                                        float totalPrice = numberOfRentDate * detail.getPrice() * detail.getQuantity();
                                        detail.setTotalPrice(totalPrice);
                                    }
                                    List headerAndDetail = new ArrayList();
                                    headerAndDetail.add(orderHeader);
                                    headerAndDetail.add(details);
                                    headerAndDetails.add(headerAndDetail);
                                }
                            }
                            request.setAttribute("HISTORY", headerAndDetails);
                        } 
                        else {
                            request.setAttribute("NOT_FOUND", "Not found any result");
                        }
                    }
                }
            }
        } 
        catch (ParseException ex) {
            Logger.getLogger(SearchHistoryServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NamingException ex) {
            Logger.getLogger(SearchHistoryServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SQLException ex) {
            Logger.getLogger(SearchHistoryServlet.class.getName()).log(Level.SEVERE, null, ex);
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
