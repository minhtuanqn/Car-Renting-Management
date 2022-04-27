/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import javax.naming.NamingException;
import tuanlm.dto.AccountsDTO;
import tuanlm.utils.DBConnector;

/**
 *
 * @author MINH TUAN
 */
public class AccountsDAO implements Serializable{
    private Connection cnn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    
    private void closeConnection() throws SQLException {
        if(rs != null) {
            rs.close();
        }
        if(ps != null) {
            ps.close();
        }
        if(cnn != null) {
            cnn.close();
        }
    }
    
    public boolean checkDuplicateEmail(String email) throws NamingException, SQLException {
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "select email  from Accounts where email = ?";
                ps = cnn.prepareStatement(sql);
                ps.setString(1, email);
                rs = ps.executeQuery();
                if(rs.next()) {
                    return true;
                }
            }
        } 
        finally {
            closeConnection();
        }
        return false;
    }
    
    public boolean createAccount(AccountsDTO dto) throws NamingException, SQLException {
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "insert into Accounts(email, password, phone, name, address, createDate, status, token) "
                        + "values(?,?,?,?,?,?,?,?)";
                ps = cnn.prepareStatement(sql);
                ps.setString(1, dto.getEmail());
                ps.setString(2, dto.getPassword());
                ps.setString(3, dto.getPhone());
                ps.setString(4, dto.getName());
                ps.setString(5, dto.getAddress());
                ps.setTimestamp(6, new Timestamp(new Date().getTime()));
                ps.setString(7, "NEW");
                ps.setString(8, dto.getToken());
                int row = ps.executeUpdate();
                if(row > 0) {
                    return true;
                }
            }
        } 
        finally {
            closeConnection();
        }
        return false;
    }
    
    public AccountsDTO checkLogin(String email, String password) throws SQLException, NamingException {
        try {
            String sql = "select email, password, name, status from Accounts where email = ? and password = ?";
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                ps = cnn.prepareStatement(sql);
                ps.setString(1, email);
                ps.setString(2, password);
                rs = ps.executeQuery();
                if(rs.next()) {
                    String status = rs.getString("status");
                    String name = rs.getString("name");
                    AccountsDTO dto = new AccountsDTO();
                    dto.setName(name);
                    dto.setStatus(status);
                    dto.setEmail(email);
                    return dto;
                }
                
            }
        } 
        finally {
            closeConnection();
        }
        return null;
    }
    
    public boolean updateStatusAccount(String email, String token) throws NamingException, SQLException {
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "update Accounts set status = 'ACTIVE' where email = ? and token = ?";
                ps = cnn.prepareStatement(sql);
                ps.setString(1, email);
                ps.setString(2, token);
                int row = ps.executeUpdate();
                if(row > 0) {
                    return true;
                }
            }
        } 
        finally {
            closeConnection();
        }
        return false;
    }
}
