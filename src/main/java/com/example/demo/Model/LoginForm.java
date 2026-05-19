package com.example.demo.Model;

public class LoginForm {
    private String emailAddress;
    private String password;
    
    public LoginForm() {}

    public LoginForm(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }
}
