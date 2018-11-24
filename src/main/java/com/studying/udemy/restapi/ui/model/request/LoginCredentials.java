package com.studying.udemy.restapi.ui.model.request;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginCredentials {
    private String userName;
    private String userPassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
