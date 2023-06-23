package com.example.roadmap.controller;

import com.example.roadmap.dto.RoadmapDTO;
import com.example.roadmap.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Restuful 웹서비스의 컨트롤러, Json 형태로 객체 데이터를 반환
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class RoadmapController {
    private final RoadmapService roadmapService;

    /**
     * 글 쓰기 ( POST /tour/write )
     */
    @PostMapping("/tour/write")
    public ResponseEntity save(@RequestBody RoadmapDTO.Request dto) {
        return ResponseEntity.ok(roadmapService.save(dto));
    }

    /**
     * 글 조회 ( GET /tour/{roadmapId} )
     */
    @GetMapping("/tour/{roadmapId}")
    public ResponseEntity read(@PathVariable Long roadmapId) {
        return ResponseEntity.ok(roadmapService.findById(roadmapId));
    }

    /**
     * 글 수정 ( PUT /tour/{roadmapId} )
     */
    @PutMapping("/tour/{roadmapId}")
    public ResponseEntity update(@PathVariable Long roadmapId, @RequestBody RoadmapDTO.Request dto) {
        roadmapService.update(roadmapId, dto);
        return ResponseEntity.ok(roadmapId);
    }

    /**
     * 글 삭제 ( DELETE /tour/{roadmapId} )
     */
    @DeleteMapping("/tour/{roadmapId}")
    public ResponseEntity delete(@PathVariable Long roadmapId) {
        roadmapService.delete(roadmapId);
        return ResponseEntity.ok(roadmapId);
    }

    /**
     * Tour 페이지 조회 ( GET /tour/list?cursor={cursor} )
     */
    @GetMapping("/tour/list")
    public ResponseEntity readTour(Long cursor){
        return ResponseEntity.ok(roadmapService.readTour(cursor));
    }

    /**
     * 검색 결과 조회 ( GET /tour/find?keyword={keyword}&cursor={cursor} )
     */
    @GetMapping("/tour/find")
    public ResponseEntity readFind(@RequestParam("keyword") String keyword,
                                   @RequestParam("cursor") Long cursor){
        return ResponseEntity.ok(roadmapService.readFind(keyword, cursor));
    }

    /**
     * 메인 페이지 조회순 조회 ( GET /main/popular )
     */
    @GetMapping("/main/popular")
    public ResponseEntity readMainPopular(){
        return ResponseEntity.ok(roadmapService.readMainPopular());
    }

    /**
     * 메인 페이지 최신순 조회 ( GET /main/recent )
     */
    @GetMapping("/main/recent")
    public ResponseEntity readMainRecent(){
        return ResponseEntity.ok(roadmapService.readMainRecent());
    }

    /**
     * 메인 페이지 탑10북 조회 ( GET /main/topbook )
     */
    @GetMapping("/main/topbook")
    public ResponseEntity readMainTopbook(){
        return ResponseEntity.ok(roadmapService.readMainTopbook());
    }
}
