package com.example.roadmap.controller;

import static org.junit.jupiter.api.Assertions.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import com.example.roadmap.dto.RoadmapDTO;
import com.example.roadmap.repository.CommentRepository;
import com.example.roadmap.repository.InfoRepository;
import com.example.roadmap.repository.RoadmapRepository;
import com.example.roadmap.repository.TagRepository;
import com.example.roadmap.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RoadmapControllerTest {
    @Autowired
    RoadmapController roadmapController;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    InfoRepository infoRepository;

    @Autowired
    RoadmapRepository roadmapRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("----------------------------------");

        // jpa 초기화
        // this.commentRepository.deleteAll();
        // this.infoRepository.deleteAll();
        // this.roadmapRepository.deleteAll();
        // this.tagRepository.deleteAll();
        // this.userRepository.deleteAll();

        // this.entityManager.createNativeQuery("ALTER TABLE roadmap ALTER COLUMN
        // `roadmapId` RESTART WITH 1").executeUpdate();

    }

    @AfterEach
    public void tearDown() throws Exception {
        System.out.println("----------------------------------");
    }

    @Test
    @DisplayName("글 쓰기 테스트")
    public void testWrite() throws Exception {

        // 글 쓰기
        RoadmapDTO.Request dto = RoadmapDTO.Request.builder().title("제목1").content("본문1").build();
        ResponseEntity response = roadmapController
                .save(dto);

        // 테스트
        assertEquals(200, response.getStatusCode().value());
        assertEquals("1", response.getBody().toString());

    }

    @Test
    @DisplayName("글 조회 테스트")
    public void testRead() throws Exception {

        // 글쓰기
        RoadmapDTO.Request dto = RoadmapDTO.Request.builder().title("제목1").content("본문1").build();
        roadmapController.save(dto);

        // 글 조회
        ResponseEntity response = roadmapController.read((long) 1);
        Object body = response.getBody();
        Map<String, String> jsonBody = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(body),
                Map.class);

        // 현재 날짜
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c1 = Calendar.getInstance();
        String strToday = sdf.format(c1.getTime());

        // 테스트
        assertEquals(1, jsonBody.get("roadmapId"));
        assertEquals(strToday, jsonBody.get("date"));
        assertEquals(0, jsonBody.get("view"));
        assertEquals("제목1", jsonBody.get("title"));
        assertEquals("본문1", jsonBody.get("content"));
        assertNotNull(jsonBody.get("infos"));
        assertNotNull(jsonBody.get("tags"));
        assertNotNull(jsonBody.get("comments"));

    }

    @Test
    @DisplayName("글 수정 테스트")
    public void testUpdate() throws Exception {

        // 글쓰기
        RoadmapDTO.Request dto = RoadmapDTO.Request.builder().title("제목1").content("본문1").build();

        roadmapController.save(dto);

        dto = RoadmapDTO.Request.builder().title("수정된 제목1").content("수정된 본문1").build();
        roadmapController.update((long) 1, dto);

        // 글 조회
        ResponseEntity response = roadmapController.read((long) 1);
        Object body = response.getBody();
        Map<String, String> jsonBody = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(body),
                Map.class);

        // 현재 날짜
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c1 = Calendar.getInstance();
        String strToday = sdf.format(c1.getTime());

        // 테스트
        assertEquals(1, jsonBody.get("roadmapId"));
        assertEquals(strToday, jsonBody.get("date"));
        assertEquals(0, jsonBody.get("view"));
        assertEquals("\uC218\uC815\uB41C \uC81C\uBAA91", jsonBody.get("title"));
        assertEquals("\uC218\uC815\uB41C \uBCF8\uBB381", jsonBody.get("content"));
        assertNotNull(jsonBody.get("infos"));
        assertNotNull(jsonBody.get("tags"));
        assertNotNull(jsonBody.get("comments"));

    }

    @Test
    @DisplayName("글 삭제 테스트")
    public void delete() throws Exception {
        // 글쓰기
        RoadmapDTO.Request dto = RoadmapDTO.Request.builder().title("제목1").content("본문1").build();
        roadmapController.save(dto);

        ResponseEntity responseEntity = roadmapController.delete((long) 1);
        System.out.println(responseEntity);

    }
}
