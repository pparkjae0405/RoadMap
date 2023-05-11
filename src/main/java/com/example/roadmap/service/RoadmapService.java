package com.example.roadmap.service;

import com.example.roadmap.domain.Roadmap;
import com.example.roadmap.dto.RoadmapDTO;
import com.example.roadmap.repository.RoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class RoadmapService {
    private final RoadmapRepository roadmapRepository;

    /**
     * 글 작성
     */
    @Transactional
    public Long save(RoadmapDTO.Request dto) {
        // 넘어온 dto를 엔티티로 바꿔 roadmap에 저장
        Roadmap roadmap = dto.toEntity();
        roadmapRepository.save(roadmap);

        return roadmap.getRoadmapId();
    }

    /**
     * 글 조회
     */
    @Transactional
    public RoadmapDTO.Response findById(Long roadmapId) {
        // 넘어온 roadmapId에 해당하는 roadmap을 찾고
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. roadmapId: " + roadmapId));
        // RoadmapDTO.Response에 매칭시켜 리턴
        return new RoadmapDTO.Response(roadmap);
    }

    /**
     * 글 수정
     */
    @Transactional
    public void update(Long roadmapId, RoadmapDTO.Request dto) {
        // 넘어온 roadmapId와 dto를 통해 해당 roadmap의 title과 content를 수정
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. roadmapId=" + roadmapId));

        roadmap.update(dto.getTitle(), dto.getContent());
    }

    /**
     * 글 삭제
     */
    @Transactional
    public void delete(Long roadmapId) {
        // 넘어온 roadmapId를 통해 해당 roadmap을 삭제
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. roadmapId=" + roadmapId));

        roadmapRepository.delete(roadmap);
    }
}