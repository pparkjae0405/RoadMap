package com.example.roadmap.controller;

import com.example.roadmap.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.roadmap.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController // Restuful 웹서비스의 컨트롤러, Json 형태로 객체 데이터를 반환
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class UserController {
    private final UserService userService;

    /**
     * 회원정보 조회
     */
    @GetMapping(value = "/user/profile")
    public ResponseEntity readMyInfo() {
        return ResponseEntity.ok(userService.readMyInfo());
    }

    /**
     * 회원정보 수정 ( PUT /user/profile )
     */
    @PutMapping("/user/profile")
    public ResponseEntity update(@RequestBody UserDTO.Request dto) {
        return ResponseEntity.ok(userService.update(dto));
    }

    /**
     * 활동내역 조회
     */
    @GetMapping(value = "/user/{userId}/activity")
    public String viewActivityHistory(@PathVariable long userId) {
        return "";
    }

}
