package com.example.roadmap.controller;

import com.example.roadmap.dto.TagDTO;
import com.example.roadmap.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Restuful 웹서비스의 컨트롤러, Json 형태로 객체 데이터를 반환
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class TagController {
    private final TagService tagService;

    /**
     * 태그 쓰기 ( POST /tour/{roadmapId}/tag )
     *
     */
    @PostMapping("/tour/{roadmapId}/tag")
    public ResponseEntity save(@PathVariable Long roadmapId, @RequestBody TagDTO.Request dto) {
        return ResponseEntity.ok(tagService.save(roadmapId, dto));
    }

    /**
     * 태그 삭제 ( DELETE /tour/{roadmapId}/tag/{tagId} )
     *
     */
    @DeleteMapping("/tour/{roadmapId}/tag/{tagId}")
    public ResponseEntity delete(@PathVariable Long tagId) {
        tagService.delete(tagId);
        return ResponseEntity.ok(tagId);
    }
}
