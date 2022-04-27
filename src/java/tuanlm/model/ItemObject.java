/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author MINH TUAN
 */
public class ItemObject implements Serializable, Comparable<ItemObject>{
    private String name;
    private Date dateFrom;
    private Date dateTo;
    private Integer quantity;
    private String remainString;

    public ItemObject() {
    }

    public ItemObject(String name, Date dateFrom, Date dateTo) {
        this.name = name;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    
    
    public ItemObject(String name, Date dateFrom, Date dateTo, Integer quantity) {
        this.name = name;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.quantity = quantity;
    }

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRemainString() {
        return remainString;
    }

    public void setRemainString(String remainString) {
        this.remainString = remainString;
    }
    
    
    
    @Override
    public int compareTo(ItemObject itemObject) {
        if (dateFrom.compareTo(itemObject.getDateFrom()) > 0) {
            return 1;
        } else if (dateFrom.compareTo(itemObject.getDateFrom()) < 0) {
            return -1;
        }
        return 0;
    }
}
