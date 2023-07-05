package com.example.roadmap.service;

import com.example.roadmap.config.exception.CEmailLoginFailedException;
import com.example.roadmap.dto.UserDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.example.roadmap.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원정보 조회
     */
    @Transactional
    public UserDTO.Response readMyInfo() {
        // 현재 사용자의 인증 정보를 가져온다.
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 사용자의 이메일을 가져온 뒤
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principal;
        String email = ((User) principal).getUsername();

        // 회원정보를 받아와 UserDTO.Response 형태로 매핑하여 설정
        UserDTO.Response userResponse = new UserDTO.Response(userRepository.findByEmail(email)
                .orElseThrow(CEmailLoginFailedException::new));

        return userResponse;
    }
}
