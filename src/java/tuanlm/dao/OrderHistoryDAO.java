/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.NamingException;
import tuanlm.dto.OrderHistoryDTO;
import tuanlm.utils.DBConnector;

/**
 *
 * @author MINH TUAN
 */
public class OrderHistoryDAO {
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
    
    public boolean createOrder(OrderHistoryDTO dto) throws SQLException, NamingException {
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "insert into OrderHistory(createDate, totalBeforeDiscount, totalAfterDiscount, "
                        + " userEmail, discountId, orderStatus) values (?,?,?,?,?,?)";
                ps = cnn.prepareStatement(sql);
                ps.setTimestamp(1, new Timestamp(new Date().getTime()));
                ps.setFloat(2, dto.getTotalBeforeDiscount());
                ps.setFloat(3, dto.getTotalAfterDiscount());
                ps.setString(4, dto.getUserEmail());
                ps.setString(5, dto.getDiscountId());
                ps.setBoolean(6, dto.isOrderStatus());
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
    
    public int getCurOrderId() throws NamingException, SQLException {
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "select max(id) as maxId from OrderHistory";
                ps = cnn.prepareStatement(sql);
                rs = ps.executeQuery();
                if(rs.next()) {
                    return rs.getInt("maxId");
                }
            }
        } 
        finally {
            closeConnection();
        }
        return -1;
    }
    
    public List<OrderHistoryDTO> getOrderHeaderList(String email) throws NamingException, SQLException {
        List<OrderHistoryDTO> orderHeaders = new ArrayList<>();
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "select id, createDate, totalBeforeDiscount, totalAfterDiscount, "
                        + " discountId from OrderHistory where orderStatus = ? and userEmail = ? ORDER BY createDate DESC";
                ps = cnn.prepareStatement(sql);
                ps.setBoolean(1, true);
                ps.setString(2, email);
                rs = ps.executeQuery();
                while(rs.next()) {
                    Integer id = rs.getInt("id");
                    Date createDate = rs.getTimestamp("createDate");
                    float totalBeforeDiscount = rs.getFloat("totalBeforeDiscount");
                    float totalAfterDiscount = rs.getFloat("totalAfterDiscount");
                    String discountId = rs.getString("discountId");
                    OrderHistoryDTO dto = new OrderHistoryDTO(id, createDate, totalBeforeDiscount, totalAfterDiscount, email, discountId, true);
                    orderHeaders.add(dto);
                }
                return orderHeaders;
            }
        } 
        finally {
            closeConnection();
        }
        return null;
    }
    
    public boolean deleteHistory(int orderId) throws SQLException, NamingException {
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "update OrderHistory set orderStatus = ? where id = ?";
                ps = cnn.prepareStatement(sql);
                ps.setBoolean(1, false);
                ps.setInt(2, orderId);
                int row = ps.executeUpdate();
                if(row > 0) {
                    return true;
                }
            }
        }
        finally{
            closeConnection();
        }
        return false;
    }
    
    public boolean checkActiveOfOrder(int orderId) throws NamingException, SQLException {
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "select orderStatus from OrderHistory where id = ? ";
                ps = cnn.prepareStatement(sql);
                ps.setInt(1, orderId);
                rs = ps.executeQuery();
                if(rs.next()) {
                    boolean check = rs.getBoolean("orderStatus");
                    return check;
                }
            }
        } 
        finally {
            closeConnection();
        }
        return true;
    }
    
    public boolean checkEmailByOrderId(int id, String email) throws SQLException, NamingException {
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "select email from OrderHistory where id = ? and email = ?";
                ps = cnn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setString(2, email);
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
    
    public OrderHistoryDTO getOrderHeaderByEmailAndId(String email, Integer orderId) throws NamingException, SQLException {
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "select id, createDate, totalBeforeDiscount, totalAfterDiscount, "
                        + " discountId from OrderHistory where orderStatus = ? and id = ? and userEmail = ? ";
                ps = cnn.prepareStatement(sql);
                ps.setBoolean(1, true);
                ps.setInt(2, orderId);
                ps.setString(3, email);
                rs = ps.executeQuery();
                if(rs.next()) {
                    Integer id = rs.getInt("id");
                    Date createDate = rs.getTimestamp("createDate");
                    float totalBeforeDiscount = rs.getFloat("totalBeforeDiscount");
                    float totalAfterDiscount = rs.getFloat("totalAfterDiscount");
                    String discountId = rs.getString("discountId");
                    OrderHistoryDTO dto = new OrderHistoryDTO(id, createDate, totalBeforeDiscount, totalAfterDiscount, email, discountId, true);
                    return dto;
                }
            }
        } 
        finally {
            closeConnection();
        }
        return null;
    }
}
