/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author MINH TUAN
 */
public class CartObject implements Serializable{
    private List<ItemObject> cars = null;

    public List<ItemObject> getCars() {
        return cars;
    }

    
    public void addToCart(ItemObject item, Integer maxQuantity) {
        if(this.cars == null) {
            this.cars  = new ArrayList<>();
        }
        
        int tmpQuantity = 0;
        for (ItemObject car : this.cars) {
            if(car.getName().equals(item.getName())) {
                if(car.getDateTo().getTime() < item.getDateFrom().getTime()
                    || car.getDateFrom().getTime() > item.getDateTo().getTime()) {
                    
                }
                else {
                    tmpQuantity += car.getQuantity();
                }
            }
        }
        if(tmpQuantity + 1 <= maxQuantity) {
            for (ItemObject car : this.cars) {
                if(car.getName().equals(item.getName())) {
                    if(car.getDateFrom().getTime() == item.getDateFrom().getTime() 
                        && car.getDateTo().getTime() == item.getDateTo().getTime()) {
                        int quantityExist = car.getQuantity();
                        car.setQuantity(quantityExist + 1);
                        return;
                    }
                }
            }
            item.setQuantity(1); 
            this.cars.add(item);
        }
    }
    
    public void deleteFromCart(String name, Date dateFrom, Date dateTo) {
        if(this.cars == null) {
            return;
        }
        int count = 0;
        while(count < this.cars.size()) {
            if(this.cars.get(count).getName().equals(name) 
                    && this.cars.get(count).getDateFrom().getTime() == dateFrom.getTime() 
                    && this.cars.get(count).getDateTo().getTime() == dateTo.getTime()) {
                this.cars.remove(count);
                count --;
            }
            count ++;
        }
        if(this.cars.isEmpty()) {
            this.cars = null;
        }
    }
    
    public String updateCarQuantity(String name, Date dateFrom, Date dateTo, Integer maxQuantity, Integer updatedQuantity) {
        if(this.cars == null) {
            return "null";
        }
        int tmpQuantity = 0;
        for (ItemObject car : this.cars) {
            if(car.getName().equals(name)) {
                if(car.getDateTo().getTime() < dateFrom.getTime()
                    || car.getDateFrom().getTime() > dateTo.getTime()) {
                    
                }
                else if(car.getDateFrom().getTime() == dateFrom.getTime()
                        && car.getDateTo().getTime() == dateTo.getTime()) {
                    
                }
                else {
                    tmpQuantity += car.getQuantity();
                }
            }
        }
        if(tmpQuantity + updatedQuantity <= maxQuantity) {
            for (ItemObject car : this.cars) {
                if(car.getName().equals(name) 
                        && car.getDateFrom().getTime() == dateFrom.getTime()
                        && car.getDateTo().getTime() == dateTo.getTime()) {
                    car.setQuantity(updatedQuantity);
                    car.setRemainString("");
                    return "success";
                }
            }
            return "notFound";
        }
        else {
            return "notEnough";
        }
    }
}
