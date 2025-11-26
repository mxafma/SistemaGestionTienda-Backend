package com.tienda.backend.dto;

public class LoginResponse {
    private String token;
    private String role;
    private UserDTO user;

    public LoginResponse(String token, String role, UserDTO user) {
        this.token = token;
        this.role = role;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public UserDTO getUser() {
        return user;
    }
}
