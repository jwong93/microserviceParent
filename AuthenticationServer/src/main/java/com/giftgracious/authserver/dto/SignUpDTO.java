package com.giftgracious.authserver.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDTO {

    private String userName;

    private String password;

    private String email;

    private String mobileNumber;

    private String address;

    private String country;
}
