package com.example.roadmap.controller;

import com.example.roadmap.dto.RoadmapDTO;
import com.example.roadmap.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Restuful 웹서비스의 컨트롤러, Json 형태로 객체 데이터를 반환
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
@RequestMapping(value = "/tour") // 공통 주소 묶기
public class RoadmapController {
    private final RoadmapService roadmapService;

    /**
     * 글 쓰기 ( POST /write )
     */
    @PostMapping("/write")
    public ResponseEntity save(@RequestBody RoadmapDTO.Request dto) {
        return ResponseEntity.ok(roadmapService.save(dto));
    }

    /**
     * 글 조회 ( GET /{roadmapId} )
     */
    @GetMapping("/{roadmapId}")
    public ResponseEntity read(@PathVariable Long roadmapId) {
        return ResponseEntity.ok(roadmapService.findById(roadmapId));
    }

    /**
     * 글 수정 ( PUT /{roadmapId} )
     */
    @PutMapping("/{roadmapId}")
    public ResponseEntity update(@PathVariable Long roadmapId, @RequestBody RoadmapDTO.Request dto) {
        roadmapService.update(roadmapId, dto);
        return ResponseEntity.ok(roadmapId);
    }

    /**
     * 글 삭제 ( DELETE /{roadmapId} )
     */
    @DeleteMapping("/{roadmapId}")
    public ResponseEntity delete(@PathVariable Long roadmapId) {
        roadmapService.delete(roadmapId);
        return ResponseEntity.ok(roadmapId);
    }

    /**
     * Tour 페이지 조회 ( GET /list?cursor={cursor} )
     */
    @GetMapping("/list")
    public ResponseEntity readTour(Long cursor){
        return ResponseEntity.ok(roadmapService.readTour(cursor));
    }
}
