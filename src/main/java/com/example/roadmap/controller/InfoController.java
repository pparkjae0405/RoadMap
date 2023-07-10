package com.example.roadmap.controller;

import com.example.roadmap.dto.InfoDTO;
import com.example.roadmap.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Restuful 웹서비스의 컨트롤러, Json 형태로 객체 데이터를 반환
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
@RequestMapping(value = "/tour") // 공통 주소 묶기
public class InfoController {
    private final InfoService infoService;

    /**
     * 로드맵 쓰기 ( POST /{roadmapId}/info )
     */
    @PostMapping("/{roadmapId}/info")
    public ResponseEntity save(@PathVariable Long roadmapId, @RequestBody List<InfoDTO.Request> dto) {
        return ResponseEntity.ok(infoService.save(roadmapId, dto));
    }

    /**
     * 로드맵 삭제 ( DELETE /{roadmapId}/info )
     */
    @DeleteMapping("/{roadmapId}/info")
    public ResponseEntity delete(@PathVariable Long roadmapId) {
        return ResponseEntity.ok(infoService.delete(roadmapId));
    }
}
