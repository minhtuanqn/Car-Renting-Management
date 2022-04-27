/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author MINH TUAN
 */
public class OrderHistoryDTO {
    private Integer id;
    private Date createDate;
    private float totalBeforeDiscount;
    private float totalAfterDiscount;
    private String userEmail;
    private String discountId;
    private boolean orderStatus;
    private Integer percenOfDiscount;
    private String createDateText;
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

    public OrderHistoryDTO() {
    }

    public OrderHistoryDTO(Integer id, Date createDate, float totalBeforeDiscount, float totalAfterDiscount, String userEmail, String discountId, boolean orderStatus) {
        this.id = id;
        this.createDate = createDate;
        this.totalBeforeDiscount = totalBeforeDiscount;
        this.totalAfterDiscount = totalAfterDiscount;
        this.userEmail = userEmail;
        this.discountId = discountId;
        this.orderStatus = orderStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public float getTotalBeforeDiscount() {
        return totalBeforeDiscount;
    }

    public void setTotalBeforeDiscount(float totalBeforeDiscount) {
        this.totalBeforeDiscount = totalBeforeDiscount;
    }

    public float getTotalAfterDiscount() {
        return totalAfterDiscount;
    }

    public void setTotalAfterDiscount(float totalAfterDiscount) {
        this.totalAfterDiscount = totalAfterDiscount;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public boolean isOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(boolean orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPercenOfDiscount() {
        return percenOfDiscount;
    }

    public void setPercenOfDiscount(Integer percenOfDiscount) {
        this.percenOfDiscount = percenOfDiscount;
    }

    public String getCreateDateText() {
        return sdf.format(createDate);
    }

    public void setCreateDateText(String createDateText) {
        this.createDateText = createDateText;
    }
    
    
}
