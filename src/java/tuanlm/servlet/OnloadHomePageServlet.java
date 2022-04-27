/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.servlet;

import java.io.IOException;
import java.sql.SQLException;
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
import tuanlm.dao.CarsDAO;
import tuanlm.dao.OrderDetailsDAO;
import tuanlm.dto.CarsDTO;
import tuanlm.utils.CarPagingModel;

/**
 *
 * @author MINH TUAN
 */
@WebServlet(name = "OnloadHomePageServlet", urlPatterns = {"/OnloadHomePageServlet"})
public class OnloadHomePageServlet extends HttpServlet {

    private final String HOME_PAGE = "home.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = HOME_PAGE;
        String curPage = request.getParameter("curPage");
        Integer curPageNum = 1;
        try {
            if (curPage != null && curPage.matches("[0-9]{1,}")) {
                curPageNum = Integer.parseInt(curPage);
            }

            CarsDAO carsDAO = new CarsDAO();

            List<String> categoryList = carsDAO.getCategoryCar();
            request.setAttribute("CATEGORY", categoryList);

            List<CarsDTO> carList = carsDAO.getAllCars();
            request.setAttribute("CARSLIST", carList);

            
            OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAO();
            for (CarsDTO carsDTO : carList) {
                double averageRating = orderDetailsDAO.getAverageRatingByCarId(carsDTO.getName());
                if (averageRating >= 0) {
                    carsDTO.setAverageRating(averageRating + "");
                } else {
                    carsDTO.setAverageRating("Not found any rating");
                }
            }

            CarPagingModel carPagingModel = new CarPagingModel();
            int totalPage = carPagingModel.getTotalPage(carList);
            List<CarsDTO> pagingList = carPagingModel.loadPaging(carList, curPageNum);
            request.setAttribute("PAGING_LIST", pagingList);
            request.setAttribute("TOTAL_PAGE", totalPage);
            url += "?curPage=" + curPageNum;
        } catch (SQLException ex) {
            Logger.getLogger(OnloadHomePageServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(OnloadHomePageServlet.class.getName()).log(Level.SEVERE, null, ex);
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
