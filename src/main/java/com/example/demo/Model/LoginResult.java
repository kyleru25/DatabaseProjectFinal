package com.example.demo.Model;

public class LoginResult {
    private String role;
    private Object user;

    public LoginResult() {
    }

    public LoginResult(String role, Object user) {
        this.role = role;
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public Object getUser() {
        return user;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUser(Object user) {
        this.user = user;
    }
}