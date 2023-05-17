package com.example.roadmap.controller;

import org.springframework.web.bind.annotation.*;

import com.example.roadmap.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 내 정보 조회 Api
    @GetMapping(value = "/{userId}/info")
    public String viewMyInfo(@PathVariable long userId) {
        return "";
    }
    // 내 정보 수정
    @PutMapping(value = "/{userId}/info")
    public String editMyInfo(@PathVariable long userId) {
        return "";
    }

    // 활동 내역 조회
    @GetMapping(value = "/{userId}/activity")
    public String viewActivityHistory(@PathVariable long userId) {
        return "";
    }

}
