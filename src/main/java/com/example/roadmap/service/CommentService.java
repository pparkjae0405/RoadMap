package com.example.roadmap.service;

import com.example.roadmap.domain.Comment;
import com.example.roadmap.domain.Roadmap;
import com.example.roadmap.dto.CommentDTO;
import com.example.roadmap.repository.CommentRepository;
import com.example.roadmap.repository.RoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class CommentService {
    private final CommentRepository commentRepository;
    private final RoadmapRepository roadmapRepository;

    /**
     * 댓글 작성
     */
    @Transactional
    public Long save(Long roadmapId, CommentDTO.Request dto) {
        // 넘어온 roadmapId를 통해 roadmap을 불러오고
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다. " + roadmapId));
        // dto의 roadmap을 설정한 다음
        dto.setRoadmap(roadmap);
        // dto를 엔티티로 바꿔 comment에 저장
        Comment comment = dto.toEntity();
        commentRepository.save(comment);

        return comment.getCommentId();
    }

    /**
     * 댓글 조회
     */
    @Transactional
    public List<CommentDTO.Response> findAll(Long roadmapId) {
        // 넘어온 roadmapId를 통해 roadmap을 불러오고
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. roadmapId: " + roadmapId));

        // roadmap.getComment를 통해 comment 리스트를 불러와
        List<Comment> comments = roadmap.getComments();
        // List<CommentDTO.Response>의 형태로 리턴
        return comments.stream().map(CommentDTO.Response::new).collect(Collectors.toList());
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
