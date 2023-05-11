package com.example.roadmap.controller;

import com.example.roadmap.dto.CommentDTO;
import com.example.roadmap.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Restuful 웹서비스의 컨트롤러, Json 형태로 객체 데이터를 반환
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class CommentController {
    private final CommentService commentService;

    /**
     * 댓글 쓰기 ( POST /tour/{roadmapId}/comment )
     *
     */
    @PostMapping("/tour/{roadmapId}/comment")
    public ResponseEntity save(@PathVariable Long roadmapId, @RequestBody CommentDTO.Request dto) {
        return ResponseEntity.ok(commentService.save(roadmapId, dto));
    }

    /**
     * 댓글 조회 ( GET /tour/{roadmapId}/comment )
     *
     */
    @GetMapping("/tour/{roadmapId}/comment")
    public List<CommentDTO.Response> read(@PathVariable Long roadmapId) {
        return commentService.findAll(roadmapId);
    }

    /**
     * 댓글 수정 ( PUT /comment/{commentId} )
     *
     */
    @PutMapping({"/comment/{commentId}"})
    public ResponseEntity update(@PathVariable Long commentId, @RequestBody CommentDTO.Request dto) {
        commentService.update(commentId, dto);
        return ResponseEntity.ok(commentId);
    }

    /**
     * 댓글 삭제 ( DELETE /comment/{commentId} )
     *
     */
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity delete(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok(commentId);
    }
}
