package com.pm.authservice.dto;

public class LoginResponseDTO {
    private final String token;
    // final 被initialized后 ，就不能被再次initialized
    //所以就可以不会改token？？？

    public LoginResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}