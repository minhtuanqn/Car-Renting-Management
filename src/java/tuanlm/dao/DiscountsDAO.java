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
import javax.naming.NamingException;
import tuanlm.dto.DiscountsDTO;
import tuanlm.utils.DBConnector;

/**
 *
 * @author MINH TUAN
 */
public class DiscountsDAO {
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
    
    public DiscountsDTO getDiscountInfo(String code) throws NamingException, SQLException {
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "select id, percentOfDiscount, dateFrom, dateto from Discounts where id = ?";
                ps = cnn.prepareStatement(sql);
                ps.setString(1, code);
                rs = ps.executeQuery();
                if(rs.next()) {
                    String id = rs.getString("id");
                    Integer percenOfDiscount = rs.getInt("percentOfDiscount");
                    Timestamp dateFrom = rs.getTimestamp("dateFrom");
                    Timestamp dateTo = rs.getTimestamp("dateTo");
                    DiscountsDTO dto = new DiscountsDTO(id, percenOfDiscount, dateFrom, dateTo);
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
