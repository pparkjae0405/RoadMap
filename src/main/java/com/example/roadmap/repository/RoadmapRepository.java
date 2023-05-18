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
}
