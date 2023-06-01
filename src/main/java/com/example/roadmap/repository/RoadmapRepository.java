package com.example.roadmap.repository;

import com.example.roadmap.domain.Roadmap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    /**
     * Tour 페이지 조회
     */
    // 최초로 조회한 경우(cursor == null)
    Page<Roadmap> findAllByOrderByRoadmapIdDesc(PageRequest pageRequest);

    // 최초가 아닌 경우(유효한 cursor)
    Page<Roadmap> findByRoadmapIdLessThanOrderByRoadmapIdDesc(Long cursor, PageRequest pageRequest);

    /**
     * 검색 결과 조회
     */
    // 최초로 조회한경우(cursor == null)
    // findByTitleContaining = title이 keyword을 포함하고 있는 것들을 찾는데
    // OrderByRoadmapIdDesc = 정렬기준을 RoadmapId Desc로 하여라
    //
    Page<Roadmap> findByTitleContainingOrderByRoadmapIdDesc(String keyword, PageRequest pageRequest);

    // 최초가 아닌 경우(유효한 cursor)
    // findByRoadmapIdLessThan = RoadmapId가 cursor보다 작은것들 중
    // TitleContaining = title이 keyword을 포함하고 있는 것들을 찾는데
    // OrderByRoadmapIdDesc = 정렬기준을 RoadmapId Desc로 하여라
    Page<Roadmap> findByRoadmapIdLessThanAndTitleContainingOrderByRoadmapIdDesc(Long cursor, String keyword, PageRequest pageRequest);

    /**
     * 메인 페이지 조회
     */
    // 조회순
    Page<Roadmap> findAllByOrderByViewDesc(PageRequest pageRequest);
    // 최신순(findAllByOrderByRoadmapIdDesc) 재사용
}
