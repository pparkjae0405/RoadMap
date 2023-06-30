package com.example.roadmap.service;

import com.example.roadmap.domain.User;
import com.example.roadmap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * UserDetailsService 인터페이스를 구현한 클래스
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional // transactional 처리로 작업단위 지정
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                //Repo 를 Optional 로 지정해줘야 map 메서드를 사용할 수 있음
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(email + " 이메일을 DB 에서 찾을 수 없습니다."));
    }

    // DB 계정 정보가 존재하면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(User user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getAuthority().toString());

        // 여기서 리턴하는 User 은 스프링 시큐리티 내 UserDetails 의 구현체임...!! 헷갈리지xx
        // UserDetails 는 인터페이스라는거~!
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                "",
                Collections.singleton(grantedAuthority)
        );
    }

}
