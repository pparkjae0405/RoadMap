package com.example.roadmap.domain;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private long userId;

    private String email;

    private String major;

    @Column(name="serialNumber")
    private String serialNumber;

    @Column(name = "accessToken")
    private String accessToken;

    @Column(name="accesstokenExpire")
    private String accesstokenExpire;

    @Column(name = "refreshToken")
    private String refreshToken;

    @Column(name = "refreshTokenExpire")
    private String refreshTokenExpire;

}
