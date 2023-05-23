package com.example.roadmap.repository;

import com.example.roadmap.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    /**
     * 태그 삭제
     */
    // 넘어온 roadmapId에 해당하는 tag 리스트를 찾는다.
    List<Tag> findByRoadmap_RoadmapId(Long roadmapId);
}
