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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tuanlm.dao.AccountsDAO;
import tuanlm.dto.AccountsDTO;
import tuanlm.utils.VerificationRecapcha;

/**
 *
 * @author MINH TUAN
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private final String LOGIN_PAGE = "loginPage";
    private final String ONLOAD_HOME = "onloadHome";
    private final String VERIFICATION_PAGE = "verificationPage";
    private static final long serialVersionUID = -6506682026701304964L;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = LOGIN_PAGE;
        String email = request.getParameter("txtEmail");
        String password = request.getParameter("txtPassword");
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        boolean verify = VerificationRecapcha.verify(gRecaptchaResponse);
        try {
            if(verify) {
                AccountsDAO dao = new AccountsDAO();
                AccountsDTO dto = dao.checkLogin(email, password);
                if(dto != null) {

                    if(dto.getStatus().equals("NEW")) {
                        url = VERIFICATION_PAGE;
                        String name = dto.getName();
                        url += "?name=" + name;
                    }
                    if(dto.getStatus().equals("ACTIVE")) {
                        url = ONLOAD_HOME;
                        HttpSession session = request.getSession(true);
                        session.setAttribute("WELLCOME_NAME", dto.getName());
                        session.setAttribute("EMAIL", dto.getEmail());
                        session.setAttribute("STATUS", dto.getStatus());
                    }
                }
                else {
                    url += "?Status=Invalid email or password";
                } 
            }
            else {
                url += "?Status=You miss the capcha";
            }
            
            
        }
        catch (SQLException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NamingException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }       
        finally {
            response.sendRedirect(url);
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
