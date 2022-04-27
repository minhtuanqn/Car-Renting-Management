/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.utils;

import java.io.Serializable;

/**
 *
 * @author MINH TUAN
 */
public class CreationErrorObject implements Serializable{
    private String invalidEmail;
    private String blankEmail;
    private String invalidPassword;
    private String passwordNotDuplicate;
    private String emailDuplicate;
    private String invalidPhone;
    private String blankPhone;
    private String blankName;
    private String blankAddress;

    public CreationErrorObject() {
    }

    public String getPasswordNotDuplicate() {
        return passwordNotDuplicate;
    }

    public void setPasswordNotDuplicate(String passwordNotDuplicate) {
        this.passwordNotDuplicate = passwordNotDuplicate;
    }
    
    public String getInvalidPassword() {
        return invalidPassword;
    }

    public void setInvalidPassword(String invalidPassword) {
        this.invalidPassword = invalidPassword;
    }

    
    public String getEmailDuplicate() {
        return emailDuplicate;
    }

    public void setEmailDuplicate(String emailDuplicate) {
        this.emailDuplicate = emailDuplicate;
    }

    public String getInvalidEmail() {
        return invalidEmail;
    }

    public void setInvalidEmail(String invalidEmail) {
        this.invalidEmail = invalidEmail;
    }

    public String getBlankEmail() {
        return blankEmail;
    }

    public void setBlankEmail(String blankEmail) {
        this.blankEmail = blankEmail;
    }

    public String getInvalidPhone() {
        return invalidPhone;
    }

    public void setInvalidPhone(String invalidPhone) {
        this.invalidPhone = invalidPhone;
    }

    public String getBlankPhone() {
        return blankPhone;
    }

    public void setBlankPhone(String blankPhone) {
        this.blankPhone = blankPhone;
    }

    public String getBlankName() {
        return blankName;
    }

    public void setBlankName(String blankName) {
        this.blankName = blankName;
    }

    public String getBlankAddress() {
        return blankAddress;
    }

    public void setBlankAddress(String blankAddress) {
        this.blankAddress = blankAddress;
    }

    
}
