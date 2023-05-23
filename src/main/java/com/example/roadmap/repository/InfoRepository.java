package com.example.roadmap.repository;

import com.example.roadmap.domain.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfoRepository extends JpaRepository<Info, Long> {
    /**
     * 로드맵 삭제
     */
    // 넘어온 roadmapId에 해당하는 info 리스트를 찾는다.
    List<Info> findByRoadmap_RoadmapId(Long roadmapId);
}
