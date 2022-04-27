/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.servlet;

import java.io.IOException;
import java.sql.SQLException;
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
import tuanlm.dto.DiscountsDTO;
import tuanlm.dto.OrderDetailsDTO;
import tuanlm.model.CartObject;
import tuanlm.model.ItemObject;

/**
 *
 * @author MINH TUAN
 */
@WebServlet(name = "OnloadCartPageServlet", urlPatterns = {"/OnloadCartPageServlet"})
public class OnloadCartPageServlet extends HttpServlet {

    private final String CART_PAGE = "cart.jsp";
    private final String LOGIN_PAGE = "login.jsp";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = LOGIN_PAGE;
        
        try  {
            List<OrderDetailsDTO> carInCart = new ArrayList<>();
            CarsDAO carsDAO = new CarsDAO();
            HttpSession session= request.getSession(false);
            if(session != null) {
                url = CART_PAGE;
                if(request.getAttribute("CHECK_OUT_SUCCESS") != null) {
                    String checkoutStatus = (String) request.getAttribute("CHECK_OUT_SUCCESS");
                    request.setAttribute("CHECK_OUT_SUCCESS", checkoutStatus);
                }
            }
            if(session != null && session.getAttribute("EMAIL") != null
                    && session.getAttribute("CUST_CART") != null) {
                CartObject cart = (CartObject) session.getAttribute("CUST_CART");
                if(cart != null) {
                    List<ItemObject> items = cart.getCars();
                    if(items != null) {
                        double priceOfAllCar = 0;
                        for (ItemObject item : items) {
                            Float price = carsDAO.getCarPriceByName(item.getName());
                            float totalPrice = item.getQuantity() * price * ((item.getDateTo().getTime() - item.getDateFrom().getTime())/24/60/60/1000 + 1);
                            String category = carsDAO.getCarCategoryByName(item.getName());
                            OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO(item.getName(), item.getQuantity(), price, item.getDateFrom(), item.getDateTo(), category);
                            orderDetailsDTO.setTotalPrice(totalPrice);
                            orderDetailsDTO.setStatusRemain(item.getRemainString());
                            carInCart.add(orderDetailsDTO);
                            priceOfAllCar += totalPrice;
                        }
                        request.setAttribute("ITEMS", carInCart);
                        request.setAttribute("TOTAL", priceOfAllCar);
                        
                        //caculate total after discount
                        if(session.getAttribute("DISCOUNT_INFO") != null) {
                            DiscountsDTO dto = (DiscountsDTO) session.getAttribute("DISCOUNT_INFO");
                            if(dto.getDateFrom().getTime() <= new Date().getTime() &&
                                dto.getDateTo().getTime() >= new Date().getTime()) {
                                double percentOfDiscount = dto.getPercenOfDiscount() * 1.0;
                                double totalAfterDiscount = (100 - percentOfDiscount) / 100 * priceOfAllCar;
                                request.setAttribute("DISCOUNT_PERCENT", dto.getPercenOfDiscount());
                                request.setAttribute("TOTAL_AFTER_DISCOUNT", totalAfterDiscount);
                                request.setAttribute("CODE", dto.getCode());
                            }
                            else {
                                request.setAttribute("TIME_OUT", "Time of this code does not applied");
                            }
                        }
                        else {
                            String code = request.getParameter("txtDiscount");
                            if(code != null && !code.equals("")) {
                                request.setAttribute("CODE", code);
                            }
                            if(request.getAttribute("DELETE_CODE") != null) {
                                
                            }
                            else if(code != null){
                                request.setAttribute("NOT_EXIST_DISCOUNT", "This discount code does not exist");
                            }
                        }
                    }
                }
                
                
                
            }
            
        }
        catch (SQLException ex) {
            Logger.getLogger(OnloadCartPageServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NamingException ex) {
            Logger.getLogger(OnloadCartPageServlet.class.getName()).log(Level.SEVERE, null, ex);
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
