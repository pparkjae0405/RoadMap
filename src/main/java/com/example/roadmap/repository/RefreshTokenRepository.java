package com.example.roadmap.repository;

import com.example.roadmap.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    /* 토큰 존재 유무 확인 */
    boolean existsByToken(String token);

    /* token에 해당하는 userId 반환 */
    RefreshToken findByToken(String token);
}
