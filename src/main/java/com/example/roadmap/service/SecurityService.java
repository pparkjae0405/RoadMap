package com.example.roadmap.service;

import com.example.roadmap.config.exception.CEmailLoginFailedException;
import com.example.roadmap.config.jwt.JwtTokenProvider;
import com.example.roadmap.domain.RefreshToken;
import com.example.roadmap.domain.User;
import com.example.roadmap.dto.TokenDTO;
import com.example.roadmap.dto.UserDTO;
import com.example.roadmap.repository.RefreshTokenRepository;
import com.example.roadmap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 토큰을 발급하는 모든 상황을 처리하는 Service
 */
@Service
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class SecurityService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository tokenRepository;

    /**
     * 로그인된 사용자에게 토큰 발급하는 메소드
     */
    public TokenDTO.Request login(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(CEmailLoginFailedException::new);
        System.out.println("SecurityService-login: 계정을 찾았습니다.");

        // 토큰 발행
        TokenDTO.Request tokenRequest = jwtTokenProvider.generateToken(email);

        // RefreshToken 만 DB에 저장
        // signup 시에도 저장하고, 로그인시에도 저장하므로 존재하는 토큰을 찾기 위해 key 값이 필요
        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.getUserId())
                .token(tokenRequest.getRefreshToken())
                .build();
        tokenRepository.save(refreshToken);
        System.out.println("토큰 발급과 저장을 완료했습니다.");

        return tokenRequest;
    }

    /**
     * 회원가입 요청에 대해 토큰 발급하는 메소드
     */
    public TokenDTO.Request signup(UserDTO.Request userRequest) {
        // 토큰 발행
        return jwtTokenProvider.generateToken(userRequest.getEmail());
    }

    /**
     * Refresh Token의 유효성을 검증하는 메소드
     */
    public boolean validateRefreshToken(String refreshToken) {
        return jwtTokenProvider.validateRefreshToken(refreshToken);
    }

    /**
     * Refresh Token의 존재유무를 검증하는 메소드
     */
    public boolean existsRefreshToken(String refreshToken) {
        return tokenRepository.existsByToken(refreshToken);
    }

    /**
     * Access Token을 재발급하는 메소드
     */
    public TokenDTO.ReissueTokenRequest reissueToken(String email) {
        return jwtTokenProvider.generateAccessToken(email);
    }
}
