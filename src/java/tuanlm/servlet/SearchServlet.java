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
import java.util.Calendar;
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
import tuanlm.utils.CarPagingModel;

/**
 *
 * @author MINH TUAN
 */
@WebServlet(name = "SearchServlet", urlPatterns = {"/SearchServlet"})
public class SearchServlet extends HttpServlet {

    private final String HOME_PAGE = "home.jsp";
    private final String ONLOAD_HOME_PAGE = "OnloadHomePageServlet";

    private List<CarsDTO> caculateRemainQuantity(List<CarsDTO> carList, List<ItemObject> items, Date dateFrom, Date dateTo) {
        for (CarsDTO carsDTO : carList) {
            int tmpQuantity = carsDTO.getQuantity();
            for (ItemObject item : items) {
                if (carsDTO.getName().equals(item.getName())) {
                    if (item.getDateFrom().getTime() > dateTo.getTime()
                            || item.getDateTo().getTime() < dateFrom.getTime()) {

                    } 
                    else {
                        tmpQuantity -= item.getQuantity();
                    }
                }
            }
            carsDTO.setQuantity(tmpQuantity);
        }
        return carList;
    }
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = HOME_PAGE;
        String name = request.getParameter("txtName");
        String rentDateText = request.getParameter("txtDateFrom");
        String returnDateText = request.getParameter("txtDateTo");
        String category = request.getParameter("cbbCategory");
        String amountOfCar = request.getParameter("txtAmount");
        String curPage = request.getParameter("curPage");
        String searchType = request.getParameter("searchType");
        Integer curPageNum = 1;

        try {
            
            CarsDAO carsDAO = new CarsDAO();
            List<String> categoryList = carsDAO.getCategoryCar();
            request.setAttribute("CATEGORY", categoryList);
            
            if (curPage != null && curPage.length() > 0) {
                curPageNum = Integer.parseInt(curPage);
            }
            
            if ((name == null || name.length() == 0)
                    && (category == null || category.length() == 0)
                    && (amountOfCar == null || amountOfCar.length() == 0)
                    && (rentDateText == null || rentDateText.length() == 0)
                    && (returnDateText == null || returnDateText.length() == 0)) {
                url = ONLOAD_HOME_PAGE;
            } 
            else {
                if (rentDateText != null && rentDateText.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")
                        && returnDateText != null && returnDateText.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
                    //Convert date from text to Date util
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date rentDate = sdf.parse(rentDateText);
                    Date returnDate = sdf.parse(returnDateText);
                    String dateNowText = sdf.format(Calendar.getInstance().getTime());
                    Date dateNow = sdf.parse(dateNowText);
                    
                    
                    //Check valid information for searching
                    boolean checkValid = true;
                    if(amountOfCar != null && !amountOfCar.matches("[0-9]{1,}")) {
                        request.setAttribute("INVALID_QUANTITY", "Quantity of car must be a number ") ;
                        checkValid = false;
                    }
                    if(rentDate.getTime() > returnDate.getTime()) {
                        request.setAttribute("INVALID_RENT_RETURN", "Time for renting must be equal or later than return date") ; 
                        checkValid = false;
                    }
                    if(rentDate.getTime() < dateNow.getTime()) {
                        request.setAttribute("INVALID_RENT", "Time for renting must be equal or later time now") ;
                        checkValid = false;
                    }
                    
                    if (checkValid) {

                        //Get detail list of history belong to type of car
                        Integer amountOfCarNum = Integer.parseInt(amountOfCar);
                        List<CarsDTO> carListByNameOrCategory = carsDAO.getCarLikeNameOrCategory(name, category);
                        OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAO();
                        OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO();
                        for (CarsDTO carsDTO : carListByNameOrCategory) {
                            int tmpAmountOfCar = carsDTO.getQuantity();
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
                                    if (orderDetailsDTO.getDateFrom().getTime() > returnDate.getTime()
                                            || orderDetailsDTO.getDateTo().getTime() < rentDate.getTime()) {

                                    } 
                                    else {
                                        tmpAmountOfCar -= orderDetailsDTO.getQuantity();
                                    }
                                }
                                carsDTO.setQuantity(tmpAmountOfCar);
                            }
                        }

                        //Remove car not enough quantity
                        int count = 0;
                        while (count < carListByNameOrCategory.size()) {
                            if (carListByNameOrCategory.get(count).getQuantity() < amountOfCarNum) {
                                carListByNameOrCategory.remove(count);
                                count--;
                            }
                            count++;
                        }

                        //get items from cart
                        HttpSession session = request.getSession(false);
                        if (session != null) {
                            if (session.getAttribute("CUST_CART") != null) {
                                CartObject cart = (CartObject) session.getAttribute("CUST_CART");
                                List<ItemObject> items = cart.getCars();
                                if (items != null) {
                                    //caculate remain quantity
                                    carListByNameOrCategory = caculateRemainQuantity(carListByNameOrCategory, items, rentDate, returnDate);
                                }
                            }
                        }

                        //set status out of stock and rating
                        for (CarsDTO carsDTO : carListByNameOrCategory) {
                            if(carsDTO.getQuantity() <= 0) {
                                carsDTO.setStatusRemain("This car is out of stock");
                            }
                            double averageRating = orderDetailsDAO.getAverageRatingByCarId(carsDTO.getName());
                            if(averageRating >= 0) {
                                carsDTO.setAverageRating(averageRating + "");
                            }
                            else {
                                carsDTO.setAverageRating("Not found any rating");
                            }
                        }
                        
                        //Paging
                        CarPagingModel carPagingModel = new CarPagingModel();
                        int totalPage = carPagingModel.getTotalPage(carListByNameOrCategory);
                        request.setAttribute("TOTAL_PAGE", totalPage);

                        List<CarsDTO> pagingList = carPagingModel.loadPaging(carListByNameOrCategory, curPageNum);
                        request.setAttribute("PAGING_LIST", pagingList);
                        
                    }
                }
            }
            url += "?curPage=" + curPageNum
                    + "&searchType=" + searchType;
        } 
        catch (SQLException ex) {
            Logger.getLogger(SearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NamingException ex) {
            Logger.getLogger(SearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ParseException ex) {
            Logger.getLogger(SearchServlet.class.getName()).log(Level.SEVERE, null, ex);
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
