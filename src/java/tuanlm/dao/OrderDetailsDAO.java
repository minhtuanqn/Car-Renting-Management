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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.naming.NamingException;
import tuanlm.dto.CarsDTO;
import tuanlm.dto.OrderDetailsDTO;
import tuanlm.utils.DBConnector;

/**
 *
 * @author MINH TUAN
 */
public class OrderDetailsDAO implements Serializable{
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
    
    public List<OrderDetailsDTO> getCarListByCarNameOrCategory(CarsDTO carDTO) throws NamingException, SQLException, ParseException {
        List<OrderDetailsDTO> orderDatailsList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowText = sdf.format(Calendar.getInstance().getTime());
        try {
            Date dateNow = sdf.parse(dateNowText);
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "select orderId, carId, quantity, price, dateFrom, dateTo from OrderDetails"
                        + " where carId = ? and dateTo >= ?";
                ps = cnn.prepareStatement(sql);
                ps.setString(1, carDTO.getName());
                ps.setTimestamp(2, new Timestamp(dateNow.getTime()));
                rs = ps.executeQuery();
                while(rs.next()) {
                    Integer orderId = rs.getInt("orderId");
                    String carName = rs.getString("carId");
                    Integer quantity = rs.getInt("quantity");
                    Float price = rs.getFloat("price");
                    Date dateFrom = rs.getDate("dateFrom");
                    Date dateTo = rs.getDate("dateTo");
                    OrderDetailsDTO dto = new OrderDetailsDTO(orderId, carName, quantity, price, dateFrom, dateTo);
                    orderDatailsList.add(dto);
                }
            }
        }
        finally {
            closeConnection();
        }
        return orderDatailsList;
    }
    
    public boolean insertOrderDetail(List<OrderDetailsDTO> list, Integer orderHeaderId) throws NamingException, SQLException {
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "insert into OrderDetails(orderId, carId, quantity, price, dateFrom, dateTo, rating) "
                        + "values(?,?,?,?,?,?,?)";
                for (OrderDetailsDTO orderDetailsDTO : list) {
                    ps = cnn.prepareStatement(sql);
                    ps.setInt(1, orderHeaderId);
                    ps.setString(2, orderDetailsDTO.getCarId());
                    ps.setInt(3, orderDetailsDTO.getQuantity());
                    ps.setFloat(4, orderDetailsDTO.getPrice());
                    ps.setTimestamp(5, new Timestamp(orderDetailsDTO.getDateFrom().getTime()));
                    ps.setTimestamp(6, new Timestamp(orderDetailsDTO.getDateTo().getTime()));
                    ps.setInt(7, -1);
                    ps.executeUpdate();
                }
                return true;
            }
        } 
        finally {
            closeConnection();
        }
        return false;
    }
    
    public List<OrderDetailsDTO> getDetailByHeaderId(Integer headerId) throws SQLException, NamingException {
        List<OrderDetailsDTO> details = new ArrayList<>();
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "select carId, quantity, price, dateFrom, dateTo, rating, feedback from OrderDetails where orderId = ?";
                ps = cnn.prepareStatement(sql); 
                ps.setInt(1, headerId);
                rs = ps.executeQuery();
                while(rs.next()) {
                    String carId = rs.getString("carId");
                    Integer quantity = rs.getInt("quantity");
                    float price = rs.getFloat("price");
                    Timestamp dateFrom = rs.getTimestamp("dateFrom");
                    Timestamp dateTo = rs.getTimestamp("dateTo");
                    Integer rating = rs.getInt("rating");
                    String feedback = rs.getString("feedback");
                    OrderDetailsDTO dto = new OrderDetailsDTO(headerId, carId, quantity, price, dateFrom, dateTo);
                    dto.setRating(rating);
                    dto.setFeedback(feedback);
                    details.add(dto);
                }
                return details;
            }
        } 
        finally {
            closeConnection();
        }
        return null;
    }
    
    public boolean updateRatingAndFeedback(Integer rating, String feedback, Integer orderId, Date dateFrom, Date dateTo, String carId) throws NamingException, SQLException {
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "update OrderDetails set feedback = ? , rating = ? where orderId = ? and carId = ? "
                        + "and dateFrom = ? and dateTo = ?";
                ps = cnn.prepareStatement(sql);
                ps.setString(1, feedback);
                ps.setInt(2, rating);
                ps.setInt(3, orderId);
                ps.setString(4, carId);
                ps.setTimestamp(5, new Timestamp(dateFrom.getTime()));
                ps.setTimestamp(6, new Timestamp(dateTo.getTime()));
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
    
    public double getAverageRatingByCarId(String carId) throws NamingException, SQLException {
        Integer totalRating = 0;
        int count  = 0;
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "select rating  from OrderDetails where carId = ? and rating >= ?";
                ps = cnn.prepareStatement(sql);
                ps.setString(1, carId);
                ps.setInt(2, 0);
                rs = ps.executeQuery();
                while(rs.next()) {
                    Integer rating = rs.getInt("rating");
                    totalRating += rating;
                    count++;
                }
            }
        }
        finally {
            closeConnection();
        }
        return totalRating * 1.0/count;
    }
    
    public List<Integer> searchHistoryByDate(Date rentingDate, Date returnDate) throws NamingException, SQLException {
        List<Integer> orderHeaderList = new ArrayList<>();
        try {
            cnn = DBConnector.makeConnection();
            String sql = "select DISTINCT orderId from OrderDetails where dateFrom >= ? and dateTo <= ?";
            ps = cnn.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(rentingDate.getTime()));
            ps.setTimestamp(2, new Timestamp(returnDate.getTime()));
            rs = ps.executeQuery();
            while(rs.next()) {
                Integer id = rs.getInt("orderId");
                orderHeaderList.add(id);
            }
        }
        finally {
            closeConnection();
        }
        return orderHeaderList;
    }
    
    public List<Integer> searchHistoryLikeId(String searchedorderId, List<Integer> orderIdList) throws NamingException, SQLException {
        try {
            cnn = DBConnector.makeConnection();
            String sql = "select orderId from OrderDetails where orderId like ?";
            ps = cnn.prepareStatement(sql);
            ps.setString(1, "%" + searchedorderId + "%");
            rs = ps.executeQuery();
            while(rs.next()) {
                Integer id = rs.getInt("orderId");
                boolean checkExist = false;
                for (int integer : orderIdList) {
                    if(integer == id) {
                        checkExist = true;
                    }
                }
                if(!checkExist) {
                   orderIdList.add(id); 
                }
            }
        }
        finally {
            closeConnection();
        }
        return orderIdList;
    }
}
