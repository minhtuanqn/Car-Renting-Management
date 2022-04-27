/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.dto;

import java.io.Serializable;

/**
 *
 * @author MINH TUAN
 */
public class CarsDTO implements Serializable{
    private String name;
    private String color;
    private int year;
    private String category;
    private float price;
    private int quantity;
    private String statusRemain;
    private String averageRating;

    public CarsDTO() {
    }

    public CarsDTO(String name, String color, int year, String category, float price, int quantity) {
        this.name = name;
        this.color = color;
        this.year = year;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatusRemain() {
        return statusRemain;
    }

    public void setStatusRemain(String statusRemain) {
        this.statusRemain = statusRemain;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    
}
