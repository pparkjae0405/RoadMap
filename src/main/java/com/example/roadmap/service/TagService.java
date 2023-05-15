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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class TagService {
    private final TagRepository tagRepository;
    private final RoadmapRepository roadmapRepository;

    /**
     * 태그 작성
     */
    @Transactional
    public Long save(Long roadmapId, TagDTO.Request dto) {
        // 넘어온 roadmapId를 통해 roadmap을 불러오고
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow(() ->
                new IllegalArgumentException("태그 쓰기 실패: 해당 게시글이 존재하지 않습니다. " + roadmapId));
        // dto의 roadmap을 설정한 다음
        dto.setRoadmap(roadmap);
        // dto를 엔티티로 바꿔 info에 저장
        Tag tag = dto.toEntity();
        tagRepository.save(tag);

        return tag.getTagId();
    }

    /**
     * 태그 조회
     */
    @Transactional
    public List<TagDTO.Response> findAll(Long roadmapId) {
        // 넘어온 roadmapId를 통해 roadmap을 불러오고
        Roadmap roadmap = roadmapRepository.findById(roadmapId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. roadmapId: " + roadmapId));

        // roadmap.getTag를 통해 tag 리스트를 불러와
        List<Tag> tags = roadmap.getTags();
        // List<TagDTO.Response>의 형태로 리턴
        return tags.stream().map(TagDTO.Response::new).collect(Collectors.toList());
    }

    /**
     * 태그 수정
     */
    @Transactional
    public void update(Long tagId, TagDTO.Request dto) {
        // 넘어온 commentId와 dto를 통해 comment의 content를 수정
        Tag tag = tagRepository.findById(tagId).orElseThrow(() ->
                new IllegalArgumentException("해당 태그가 존재하지 않습니다. tagId=" + tagId));
        //
        tag.update(dto.getTag());
    }

    /**
     * 태그 삭제
     */
    @Transactional
    public void delete(Long tagId) {
        // 넘어온 infoId를 통해 해당 info를 삭제
        Tag tag = tagRepository.findById(tagId).orElseThrow(() ->
                new IllegalArgumentException("해당 태그가 존재하지 않습니다. tagId=" + tagId));

        tagRepository.delete(tag);
    }
}
