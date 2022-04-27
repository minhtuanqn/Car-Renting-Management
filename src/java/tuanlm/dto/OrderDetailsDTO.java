/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author MINH TUAN
 */
public class OrderDetailsDTO implements Serializable{
    private int orderId;
    private String carId;
    private int quantity;
    private float price;
    private float totalPrice;
    private Date dateFrom;
    private Date dateTo;
    private String category;
    private Integer rating;
    private String feedback;
    private String dateFromText;
    private String dateToText;
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
    private String statusRemain;

    public OrderDetailsDTO() {
    }

    public OrderDetailsDTO(int orderId, String carId, int quantity, float price, Date dateFrom, Date dateTo) {
        this.orderId = orderId;
        this.carId = carId;
        this.quantity = quantity;
        this.price = price;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public OrderDetailsDTO(String carId, int quantity, float price, Date dateFrom, Date dateTo, String category) {
        this.carId = carId;
        this.quantity = quantity;
        this.price = price;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.category = category;
    }
    

    
    public int getOrderId() {
        return orderId;
    }

    public String getCarId() {
        return carId;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDateFromText() {
        return sdf.format(dateFrom);
    }

    public String getDateToText() {
        return sdf.format(dateTo);
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getStatusRemain() {
        return statusRemain;
    }

    public void setStatusRemain(String statusRemain) {
        this.statusRemain = statusRemain;
    }
    
    
    
}
