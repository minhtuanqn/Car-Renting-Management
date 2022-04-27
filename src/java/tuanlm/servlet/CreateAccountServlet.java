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
import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tuanlm.dao.AccountsDAO;
import tuanlm.dto.AccountsDTO;
import tuanlm.utils.CreationErrorObject;
import tuanlm.utils.GmailVerification;

/**
 *
 * @author MINH TUAN
 */
@WebServlet(name = "CreateAccountServlet", urlPatterns = {"/CreateAccountServlet"})
public class CreateAccountServlet extends HttpServlet {

    private final String CREATE_PAGE = "createAccount.jsp";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = CREATE_PAGE;
        String email = request.getParameter("txtEmail");
        String password = request.getParameter("txtPassword");
        String passwordAgain = request.getParameter("txtPasswordAgain");
        String name  = request.getParameter("txtName");
        String phone = request.getParameter("txtPhone");
        String address = request.getParameter("txtAddress");
        try  {
            AccountsDAO accountsDAO = new AccountsDAO();
            CreationErrorObject error = new CreationErrorObject();
            boolean checkError = false;
            if(email == null || email.trim().length() == 0) {
                error.setBlankEmail("Email can not be blank");
                checkError = true;
            }
            else {
                if(email != null && !email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
                    error.setInvalidEmail("Email is invalid");
                    checkError = true;
                }
                else {
                    if(accountsDAO.checkDuplicateEmail(email)) {
                        error.setEmailDuplicate("This email has been existed");
                        checkError = true;
                    }
                }
            }
            if(password == null || password.length() < 6 || password.length() > 30) {
                error.setInvalidPassword("Password must be from 6-10 character");
                checkError = true;
            }
            else {
                if(passwordAgain == null || !password.equals(passwordAgain)) {
                    error.setPasswordNotDuplicate("Password must be duplicated");
                    checkError = true;
                }
            }
            if(name == null || name.trim().length() == 0) {
                error.setBlankName("Name can not be blank");
                checkError = true;
            }
            if(phone == null || phone.trim().length() == 0) {
                error.setBlankPhone("Phone can not be blank");
                checkError = true;
            }
            else {
                if(!phone.matches("[0-9]{5,13}")) {
                    error.setInvalidPhone("Phone must be digits and 5 - 13 characters");
                    checkError = true;
                }
            }
            if(address == null || address.trim().length() == 0) {
                error.setBlankAddress("Address can not blank");
                checkError = true;
            }
            if(!checkError) {
                String token = GmailVerification.randomToken();
                AccountsDTO dto = new AccountsDTO(email, password, phone, name, address);
                dto.setToken(token);
                boolean checkCreate = accountsDAO.createAccount(dto);
                if(checkCreate) {
                    request.setAttribute("STATUS_CREATE", "Create successfully");
                    GmailVerification.generateAndSendEmail(email, token);
                }
                else {
                    request.setAttribute("STATUS_CREATE", "Create fails");
                }
            }
            else {
                request.setAttribute("ERROR", error);
            }
        }
        catch (NamingException ex) {
            Logger.getLogger(CreateAccountServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SQLException ex) {
            Logger.getLogger(CreateAccountServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (MessagingException ex) {
            Logger.getLogger(CreateAccountServlet.class.getName()).log(Level.SEVERE, null, ex);
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
