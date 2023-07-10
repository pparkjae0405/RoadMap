package com.example.roadmap.service;

import com.example.roadmap.config.exception.CEmailLoginFailedException;
import com.example.roadmap.domain.Comment;
import com.example.roadmap.domain.Roadmap;
import com.example.roadmap.dto.CommentDTO;
import com.example.roadmap.repository.CommentRepository;
import com.example.roadmap.repository.RoadmapRepository;
import com.example.roadmap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class CommentService {
    private final CommentRepository commentRepository;
    private final RoadmapRepository roadmapRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 작성
     */
    @Transactional
    public CommentDTO.ResultResponse save(Long roadmapId, CommentDTO.Request dto) {
        // 수행 결과를 리턴할 CommentDTO.ResultResponse 선언
        CommentDTO.ResultResponse resultResponse = new CommentDTO.ResultResponse();

        // 넘어온 roadmapId를 통해 roadmap을 불러오고
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다. " + roadmapId));
        // dto의 roadmap을 설정한 다음
        dto.setRoadmap(roadmap);

        // 현재 사용자의 인증 정보를 가져와
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 사용자의 이메일을 가져온 뒤
        org.springframework.security.core.userdetails.User customUser = (org.springframework.security.core.userdetails.User) principal;
        String email = ((User) principal).getUsername();

        // 해당하는 회원 정보를 받아와
        com.example.roadmap.domain.User user = userRepository.findByEmail(email)
                .orElseThrow(CEmailLoginFailedException::new);

        // dto의 user를 설정한 다음
        dto.setUser(user);

        // dto를 엔티티로 바꿔 comment에 저장
        Comment comment = dto.toEntity();
        commentRepository.save(comment);

        // 성공하면 success를 true로 설정하여 리턴
        resultResponse.setSuccess(true);

        return resultResponse;
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public void update(Long commentId, CommentDTO.Request dto) {
        // 넘어온 commentId와 dto를 통해 comment의 content를 수정
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다. commentId=" + commentId));
        //
        comment.update(dto.getContent());
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void delete(Long commentId) {
        // 넘어온 commentId를 통해 해당 comment를 삭제
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다. commentId=" + commentId));

        commentRepository.delete(comment);
    }
}
