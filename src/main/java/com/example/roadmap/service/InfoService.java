package com.example.roadmap.service;

import com.example.roadmap.domain.Info;
import com.example.roadmap.domain.Roadmap;
import com.example.roadmap.dto.InfoDTO;
import com.example.roadmap.repository.InfoRepository;
import com.example.roadmap.repository.RoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class InfoService {
    private final InfoRepository infoRepository;
    private final RoadmapRepository roadmapRepository;

    /**
     * 로드맵 쓰기
     */
    @Transactional
    public InfoDTO.ResultResponse save(Long roadmapId, List<InfoDTO.Request> dto) {
        // 수행 결과를 리턴할 InfoDTO.ResultResponse 선언
        InfoDTO.ResultResponse resultResponse = new InfoDTO.ResultResponse();

        // 넘어온 roadmapId를 통해 roadmap을 불러오고
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow(() ->
                new IllegalArgumentException("로드맵 쓰기 실패: 해당 게시글이 존재하지 않습니다. " + roadmapId));

        // 작성할 로드맵 개수만큼 반복하여
        for(int i = 0 ; i < dto.size() ; i++){
            // dto의 값을 하나씩 InfoDTO.Request 형태로 불러온 뒤
            InfoDTO.Request dtoValue = dto.get(i);

            // dtoValue의 roadmap을 설정한 다음
            dtoValue.setRoadmap(roadmap);

            // dtoValue를 엔티티로 바꿔 info에 저장시킨다.
            Info info = dtoValue.toEntity();
            infoRepository.save(info);
        }

        // 로드맵을 전부 저장했다면 success를 true로 설정하여 리턴
        resultResponse.setSuccess(true);

        return resultResponse;
    }

    /**
     * 로드맵 삭제
     */
    @Transactional
    public InfoDTO.ResultResponse delete(Long roadmapId) {
        // 수행 결과를 리턴할 InfoDTO.ResultResponse 선언
        InfoDTO.ResultResponse resultResponse = new InfoDTO.ResultResponse();

        // findByRoadmap_RoadmapId를 호출하여 넘어온 roadmapId에 해당하는 info 리스트를 찾아
        List<Info> infos = infoRepository.findByRoadmap_RoadmapId(roadmapId);

        // 모두 삭제한다.
        for(int i = 0 ; i < infos.size() ; i++){
            infoRepository.delete(infos.get(i));
        }

        // 로드맵을 전부 삭제했다면 success를 true로 설정하여 리턴
        resultResponse.setSuccess(true);

        return resultResponse;
    }
}
