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
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import tuanlm.dto.CarsDTO;
import tuanlm.utils.DBConnector;

/**
 *
 * @author MINH TUAN
 */
public class CarsDAO implements Serializable{
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
    
    public List<CarsDTO> getAllCars() throws SQLException, NamingException {
        List<CarsDTO> carsList = new ArrayList<>();
        try {
            String sql = "select name, color, year, category, price, quantity from Cars";
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                ps = cnn.prepareStatement(sql);
                rs = ps.executeQuery();
                while(rs.next()) {
                    String name = rs.getString("name");
                    String color = rs.getString("color");
                    Integer year = rs.getInt("year");
                    String category = rs.getString("category");
                    Float price = rs.getFloat("price");
                    Integer quantity = rs.getInt("quantity");
                    CarsDTO dto = new CarsDTO(name, color, year, category, price, quantity);
                    carsList.add(dto);
                }
            }
        } 
        finally {
            closeConnection();
        }
        return carsList;
    }
    
    public List<String> getCategoryCar() throws SQLException, NamingException {
        List<String> categoryList = new ArrayList<>();
        try {
            String sql = "select distinct category from Cars";
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                ps = cnn.prepareStatement(sql);
                rs = ps.executeQuery();
                while(rs.next()) {
                    String category = rs.getString("category");
                    categoryList.add(category);
                }
            }
        } 
        finally {
            closeConnection();
        }
        return categoryList;
    }
    
    public List<CarsDTO> getCarLikeNameOrCategory(String searchedName, String category) throws NamingException, SQLException {
        List<CarsDTO> carsList = new ArrayList<>();
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "select name, color, year, category, price, quantity from Cars where name like ? or category like ?";
                ps = cnn.prepareStatement(sql);
                ps.setString(1, "%" + searchedName + "%");
                ps.setString(2, "%" + category + "%");
                rs = ps.executeQuery();
                while(rs.next()) {
                    String name = rs.getString("name");
                    String color = rs.getString("color");
                    Float price = rs.getFloat("price");
                    Integer year = rs.getInt("year");
                    String categoryDTO = rs.getString("category");
                    Integer quantity = rs.getInt("quantity");
                    CarsDTO dto = new CarsDTO(name, color, year, categoryDTO, price, quantity);
                    carsList.add(dto);
                }
            }
        } 
        finally {
            closeConnection();
        }
        return carsList;
    }
    
    public String getCarCategoryByName(String name) throws SQLException, NamingException {
        String category = "";
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "select category from Cars where name = ?";
                ps = cnn.prepareStatement(sql);
                ps.setString(1, name);
                rs = ps.executeQuery();
                if(rs.next()) {
                    category = rs.getString("category");
                }
            }
        } 
        finally {
            closeConnection();
        }
        return category;
    }
    
    public Float getCarPriceByName(String name) throws SQLException, NamingException {
        Float price = 0f;
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "select price from Cars where name = ?";
                ps = cnn.prepareStatement(sql);
                ps.setString(1, name);
                rs = ps.executeQuery();
                if(rs.next()) {
                    price = rs.getFloat("price");
                }
            }
        } 
        finally {
            closeConnection();
        }
        return price;
    }
    
    public Integer getQuantityForCar(String name) throws SQLException, NamingException {
        Integer quantity = 0;
        try {
            cnn = DBConnector.makeConnection();
            if(cnn != null) {
                String sql = "select quantity from Cars where name = ?";
                ps = cnn.prepareStatement(sql);
                ps.setString(1, name);
                rs = ps.executeQuery();
                if(rs.next()) {
                    quantity = rs.getInt("quantity");
                }
            }
        } 
        finally {
            closeConnection();
        }
        return quantity;
    }
}
