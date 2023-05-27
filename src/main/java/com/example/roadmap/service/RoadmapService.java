package com.example.roadmap.service;

import com.example.roadmap.domain.Roadmap;
import com.example.roadmap.dto.RoadmapDTO;
import com.example.roadmap.repository.RoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Tour 페이지 조회
     */
    @Transactional
    public List<RoadmapDTO.TourResponse> readTour(Long cursor){
        // 한번에 불러올 페이지 수(10개)만큼 PageRequest를 만듬
        PageRequest pageRequest = PageRequest.of(0, 10);

        // 최초 조회인지 아닌지 판별하여 해당하는 roadmapRepository의 기능 호출
        Page<Roadmap> roadmapPage;
        if(cursor == null) {
            // 최초 조회라면 가장 최근에 올라온 글 10개
            roadmapPage = roadmapRepository
                    .findAllByOrderByRoadmapIdDesc(pageRequest);
        }else{
            // 아니라면 cursor 미만 최근에 올라온 글 10개
            roadmapPage = roadmapRepository
                    .findByRoadmapIdLessThanOrderByRoadmapIdDesc(cursor, pageRequest);
        }

        // 마지막에 불러왔던 cursor와 pageRequest를 인자로 전달
        List<Roadmap> roadmaps = roadmapPage.getContent();

        // roadmaps를 RoadmapDTO.TourResponse 형태로 가공하여 리턴
        return roadmaps.stream()
                .map(RoadmapDTO.TourResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 검색 결과 조회
     */
    @Transactional
    public List<RoadmapDTO.FindResponse> readFind(String keyword, Long cursor){
        // 한번에 불러올 페이지 수(10개)만큼 PageRequest를 만듬
        PageRequest pageRequest = PageRequest.of(0, 10);

        // 최초 조회인지 아닌지 판별하여 해당하는 roadmapRepository의 기능 호출
        Page<Roadmap> roadmapPage;
        if(cursor == null) {
            // 최초 조회라면 keyword에 해당하는 글들을 최신 순으로 10개,
            roadmapPage = roadmapRepository
                    .findByTitleContainingOrderByRoadmapIdDesc(keyword, pageRequest);
        }else{
            // 아니라면 cursor 미만 keyword에 해당하는 글들을 최신 순으로 10개
            roadmapPage = roadmapRepository
                    .findByRoadmapIdLessThanAndTitleContainingOrderByRoadmapIdDesc(cursor, keyword, pageRequest);
        }

        // 마지막에 불러왔던 cursor와 pageRequest를 인자로 전달
        List<Roadmap> roadmaps = roadmapPage.getContent();

        // roadmaps를 RoadmapDTO.FindResponse 형태로 가공하여 리턴
        return roadmaps.stream()
                .map(RoadmapDTO.FindResponse::new)
                .collect(Collectors.toList());
    }
}