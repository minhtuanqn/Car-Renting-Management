/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author MINH TUAN
 */
public class DiscountsDTO implements Serializable{
    private String code;
    private Integer percenOfDiscount;
    private Date dateFrom;
    private Date dateTo;

    public DiscountsDTO(String code, Integer percenOfDiscount, Date dateFrom, Date dateTo) {
        this.code = code;
        this.percenOfDiscount = percenOfDiscount;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public DiscountsDTO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPercenOfDiscount() {
        return percenOfDiscount;
    }

    public void setPercenOfDiscount(Integer percenOfDiscount) {
        this.percenOfDiscount = percenOfDiscount;
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
    
    
}
