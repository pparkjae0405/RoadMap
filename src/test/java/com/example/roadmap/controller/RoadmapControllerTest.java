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
        RoadmapDTO.Request dto = new RoadmapDTO.Request((long) 1, 0, "제목1", "본문1");
        ResponseEntity response = roadmapController.save(dto);

        // 테스트
        assertEquals(200, response.getStatusCode().value());
        assertEquals("1", response.getBody().toString());

    }

    @Test
    @DisplayName("글 조회 테스트")
    public void testRead() throws Exception {

        long roadmapId = 1;
        int view = 0;
        String title = "제목1";
        String content = "본문1";

        // 글쓰기
        RoadmapDTO.Request dto = new RoadmapDTO.Request(roadmapId, view, title, content);
        roadmapController.save(dto);

        // 글 조회
        ResponseEntity response = roadmapController.read(roadmapId);
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
        long roadmapId = 1;
        int view = 0;
        String title = "제목1";
        String content = "본문1";

        // 글쓰기
        RoadmapDTO.Request dto = new RoadmapDTO.Request(roadmapId, view, title, content);
        roadmapController.save(dto);

        String updatedTitle = "수정된 제목1";
        String updatedContent = "수정된 본문1";

        dto = new RoadmapDTO.Request(roadmapId, view, updatedTitle, updatedContent);
        roadmapController.update(roadmapId, dto);

        // 글 조회
        ResponseEntity response = roadmapController.read(roadmapId);
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
        assertEquals(updatedTitle, jsonBody.get("title"));
        assertEquals(updatedContent, jsonBody.get("content"));
        assertNotNull(jsonBody.get("infos"));
        assertNotNull(jsonBody.get("tags"));
        assertNotNull(jsonBody.get("comments"));

    }

    @Test
    @DisplayName("글 삭제 테스트")
    public void delete() throws Exception {
        long roadmapId = 1;
        int view = 0;
        String title = "제목1";
        String content = "본문1";

        // 글쓰기
        RoadmapDTO.Request dto = new RoadmapDTO.Request(roadmapId, view, title, content);
        roadmapController.save(dto);

        ResponseEntity responseEntity = roadmapController.delete(roadmapId);
        System.out.println(responseEntity);

    }
}
