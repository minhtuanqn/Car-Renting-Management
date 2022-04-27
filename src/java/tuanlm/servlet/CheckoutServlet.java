/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
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
import tuanlm.dao.OrderDetailsDAO;
import tuanlm.dao.OrderHistoryDAO;
import tuanlm.dto.CarsDTO;
import tuanlm.dto.DiscountsDTO;
import tuanlm.dto.OrderDetailsDTO;
import tuanlm.dto.OrderHistoryDTO;
import tuanlm.model.CartObject;
import tuanlm.model.ItemObject;

/**
 *
 * @author MINH TUAN
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = {"/CheckoutServlet"})
public class CheckoutServlet extends HttpServlet {

    private final String ONLOAD_CART = "OnloadCartPageServlet";
    private final String LOGIN_PAGE = "login.jsp";
    private final String NOT_FOUND = "notfound.jsp";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = LOGIN_PAGE;
        CarsDAO carsDAO = new CarsDAO();
        try  {
            List<OrderDetailsDTO> carInCart = new ArrayList<>();
            HttpSession session = request.getSession(false);
            if(session != null) {
                url = ONLOAD_CART;
                if(session.getAttribute("CUST_CART") != null) {
                    CartObject cart = (CartObject) session.getAttribute("CUST_CART");
                    List<ItemObject> items = cart.getCars();
                    if(items != null) {
                        boolean checkValidate = true;
                        float priceOfAllCar = 0;
                        for (ItemObject item : items) {
                            
                            int maxQuantity = carsDAO.getQuantityForCar(item.getName());
                            //check number rented car inDB
                            OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAO();
                            OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO();
                            CarsDTO carsDTO = new CarsDTO();
                            carsDTO.setName(item.getName());
                            
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
                                    if (orderDetailsDTO.getDateFrom().getTime() > item.getDateTo().getTime()
                                            || orderDetailsDTO.getDateTo().getTime() < item.getDateFrom().getTime()) {

                                    } else {
                                        maxQuantity -= orderDetailsDTO.getQuantity();
                                    }
                                }
                            }
                            if(maxQuantity < item.getQuantity()) {
                                checkValidate = false;
                                item.setRemainString("Item " + item.getName() + " is out of stock");
                            }
                            else {
                                item.setRemainString("");
                                Float price = carsDAO.getCarPriceByName(item.getName());
                                float totalPrice = item.getQuantity() * price * ((item.getDateTo().getTime() - item.getDateFrom().getTime())/24/60/60/1000 + 1);
                                String category = carsDAO.getCarCategoryByName(item.getName());
                                OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO(item.getName(), item.getQuantity(), price, item.getDateFrom(), item.getDateTo(), category);
                                orderDetailsDTO.setTotalPrice(totalPrice);
                                carInCart.add(orderDetailsDTO);
                                priceOfAllCar += totalPrice;
                            }
                        }
                        
                        if(checkValidate) {
                            float finalTotalBeforeDiscount = priceOfAllCar;
                            float finalTotalAfterDiscount = priceOfAllCar;
                            String discountCode = null;

                            //caculate total after discount
                            if(session.getAttribute("DISCOUNT_INFO") != null) {
                                DiscountsDTO dto = (DiscountsDTO) session.getAttribute("DISCOUNT_INFO");
                                if(dto.getDateFrom().getTime() <= new Date().getTime() &&
                                    dto.getDateTo().getTime() >= new Date().getTime()) {
                                    float percentOfDiscount = dto.getPercenOfDiscount() * 1.0f;
                                    float totalAfterDiscount = (100 - percentOfDiscount) / 100 * priceOfAllCar;
                                    finalTotalAfterDiscount = totalAfterDiscount;
                                    discountCode = dto.getCode();
                                }
                            }

                            //Insert to DB
                            if(session.getAttribute("EMAIL") != null) {
                                String email = (String) session.getAttribute("EMAIL");
                                OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO();
                                orderHistoryDTO.setUserEmail(email);
                                orderHistoryDTO.setTotalBeforeDiscount(finalTotalBeforeDiscount);
                                orderHistoryDTO.setTotalAfterDiscount(finalTotalAfterDiscount);
                                orderHistoryDTO.setOrderStatus(true);
                                orderHistoryDTO.setDiscountId(discountCode);
                                orderHistoryDTO.setCreateDate(new Date());

                                OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO();
                                boolean checkInsertOrderHeader = orderHistoryDAO.createOrder(orderHistoryDTO);
                                if(checkInsertOrderHeader) {
                                    OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAO();
                                    boolean checkInsertDetail = orderDetailsDAO.insertOrderDetail(carInCart, orderHistoryDAO.getCurOrderId());
                                    if(checkInsertDetail == false) {
                                        url = NOT_FOUND;
                                    }
                                    else {
                                        //Delete cart
                                        if(session.getAttribute("DISCOUNT_INFO") != null) {
                                            session.removeAttribute("DISCOUNT_INFO");
                                        }
                                        if(session.getAttribute("CUST_CART") != null) {
                                            session.removeAttribute("CUST_CART");
                                        }
                                        request.setAttribute("CHECK_OUT_SUCCESS", "Check out successfuly");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NamingException ex) {
            Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ParseException ex) {
            Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE, null, ex);
        }        
        finally {
            RequestDispatcher rd = request.getRequestDispatcher(url) ;
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
