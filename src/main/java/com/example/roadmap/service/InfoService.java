package com.example.roadmap.service;

import com.example.roadmap.domain.Info;
import com.example.roadmap.domain.Roadmap;
import com.example.roadmap.dto.InfoDTO;
import com.example.roadmap.repository.InfoRepository;
import com.example.roadmap.repository.RoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class InfoService {
    private final InfoRepository infoRepository;
    private final RoadmapRepository roadmapRepository;

    /**
     * 로드맵 작성
     */
    @Transactional
    public Long save(Long roadmapId, InfoDTO.Request dto) {
        // 넘어온 roadmapId를 통해 roadmap을 불러오고
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow(() ->
                new IllegalArgumentException("로드맵 쓰기 실패: 해당 게시글이 존재하지 않습니다. " + roadmapId));
        // dto의 roadmap을 설정한 다음
        dto.setRoadmap(roadmap);
        // dto를 엔티티로 바꿔 info에 저장
        Info info = dto.toEntity();
        infoRepository.save(info);

        return info.getInfoId();
    }

    /**
     * 로드맵 수정
     */
    @Transactional
    public void update(Long infoId, InfoDTO.Request dto) {
        // 넘어온 commentId와 dto를 통해 comment의 content를 수정
        Info info = infoRepository.findById(infoId).orElseThrow(() ->
                new IllegalArgumentException("해당 로드맵이 존재하지 않습니다. infoId=" + infoId));
        //
        info.update(dto.getDate(), dto.getTitle(), dto.getContent());
    }

    /**
     * 로드맵 삭제
     */
    @Transactional
    public void delete(Long infoId) {
        // 넘어온 infoId를 통해 해당 info를 삭제
        Info info = infoRepository.findById(infoId).orElseThrow(() ->
                new IllegalArgumentException("해당 로드맵이 존재하지 않습니다. infoId=" + infoId));

        infoRepository.delete(info);
    }
}
