package com.example.roadmap.service;

import com.example.roadmap.config.exception.CEmailLoginFailedException;
import com.example.roadmap.domain.Roadmap;
import com.example.roadmap.dto.RoadmapDTO;
import com.example.roadmap.dto.UserDTO;
import com.example.roadmap.repository.RoadmapRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.example.roadmap.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class UserService {

    private final UserRepository userRepository;
    private final RoadmapRepository roadmapRepository;

    /**
     * 회원정보 조회
     */
    @Transactional
    public UserDTO.Response readMyInfo() {
        // 현재 사용자의 인증 정보를 가져온다.
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 사용자의 이메일을 가져온 뒤
        org.springframework.security.core.userdetails.User customUser = (org.springframework.security.core.userdetails.User) principal;
        String email = ((User) principal).getUsername();

        // 회원정보를 받아와 UserDTO.Response 형태로 매핑하여 설정
        UserDTO.Response userResponse = new UserDTO.Response(userRepository.findByEmail(email)
                .orElseThrow(CEmailLoginFailedException::new));

        return userResponse;
    }

    /**
     * 회원정보 수정
     */
    @Transactional
    public Long update(UserDTO.Request dto) {
        // 현재 사용자의 인증 정보를 가져온다.
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 사용자의 이메일을 가져온 뒤
        org.springframework.security.core.userdetails.User customUser = (org.springframework.security.core.userdetails.User) principal;
        String email = ((User) principal).getUsername();

        // 해당하는 회원 정보를 받아와
        com.example.roadmap.domain.User user = userRepository.findByEmail(email)
                .orElseThrow(CEmailLoginFailedException::new);

        // 넘어온 dto를 통해 해당 User의 nickName과 major를 수정하고 회원id를 리턴한다.
        user.update(dto.getNickName(), dto.getMajor());

        return user.getUserId();
    }

    /**
     * 활동내역 조회
     */
    @Transactional
    public RoadmapDTO.ActivityResultResponse readActivity(int page) {
        // page 전처리
        if(page == 1 || page == 0){
            page = 0;
        } else {
            page -= 1;
        }

        // 현재 사용자의 인증 정보를 가져온다.
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 사용자의 이메일을 가져온 뒤
        org.springframework.security.core.userdetails.User customUser = (org.springframework.security.core.userdetails.User) principal;
        String email = ((User) principal).getUsername();

        // 해당하는 회원 정보를 받아와
        com.example.roadmap.domain.User user = userRepository.findByEmail(email)
                .orElseThrow(CEmailLoginFailedException::new);

        // 한번에 불러올 페이지 수(5개)만큼 PageRequest를 만듬
        PageRequest pageRequest = PageRequest.of(page, 5);
        Page<Roadmap> roadmapPage = roadmapRepository.findAllByUserOrderByRoadmapIdDesc(user, pageRequest);

        // 전체 페이지 수와 내용을 RoadmapDTO.ActivityResponse 리스트로 불러와
        int totalPage = roadmapPage.getTotalPages();
        List<RoadmapDTO.ActivityResponse> roadmaps = roadmapPage.getContent()
                .stream()
                .map(RoadmapDTO.ActivityResponse::new)
                .collect(Collectors.toList());

        // RoadmapDTO.ActivityResultResponse 형태로 가공하여 리턴한다.
        RoadmapDTO.ActivityResultResponse activityResultResponse = RoadmapDTO.ActivityResultResponse.builder()
                .totalPage(totalPage)
                .activityResponse(roadmaps)
                .build();

        return activityResultResponse;
    }
}
