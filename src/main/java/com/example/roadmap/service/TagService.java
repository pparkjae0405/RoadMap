package com.example.roadmap.service;

import com.example.roadmap.domain.Roadmap;
import com.example.roadmap.domain.Tag;
import com.example.roadmap.dto.TagDTO;
import com.example.roadmap.repository.RoadmapRepository;
import com.example.roadmap.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class TagService {
    private final TagRepository tagRepository;
    private final RoadmapRepository roadmapRepository;

    /**
     * 태그 작성
     */
    @Transactional
    public TagDTO.ResultResponse save(Long roadmapId, List<TagDTO.Request> dto) {
        // 수행 결과를 리턴할 TagDTO.ResultResponse 선언
        TagDTO.ResultResponse resultResponse = new TagDTO.ResultResponse();

        // 넘어온 roadmapId를 통해 roadmap을 불러오고
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow(() ->
                new IllegalArgumentException("태그 쓰기 실패: 해당 게시글이 존재하지 않습니다. " + roadmapId));

        // 작성할 태그 개수만큼 반복하여
        for(int i = 0 ; i < dto.size() ; i++){
            // dto의 값을 하나씩 TagDTO.Request 형태로 불러온 뒤
            TagDTO.Request dtoValue = dto.get(i);

            // dtoValue의 roadmap을 설정한 다음
            dtoValue.setRoadmap(roadmap);

            // dtoValue를 엔티티로 바꿔 tag에 저장시킨다.
            Tag tag = dtoValue.toEntity();
            tagRepository.save(tag);
        }

        // 태그를 전부 저장했다면 success를 true로 설정하여 리턴
        resultResponse.setSuccess(true);

        return resultResponse;
    }

    /**
     * 태그 삭제
     */
    @Transactional
    public void delete(Long roadmapId) {
        // findByRoadmap_RoadmapId를 호출하여 넘어온 roadmapId에 해당하는 tag 리스트를 찾아
        List<Tag> tags = tagRepository.findByRoadmap_RoadmapId(roadmapId);

        // 모두 삭제한다.
        for(int i = 0 ; i < tags.size() ; i++){
            tagRepository.delete(tags.get(i));
        }
    }
}
