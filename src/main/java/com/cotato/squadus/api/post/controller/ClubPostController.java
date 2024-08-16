package com.cotato.squadus.api.post.controller;

import com.cotato.squadus.api.post.dto.*;
import com.cotato.squadus.domain.club.post.service.ClubPostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "동아리 공지", description = "동아리 공지 관련 API")
@Slf4j
@RestController
@RequestMapping("/v1/api/clubs/{clubId}/posts")
@RequiredArgsConstructor
public class ClubPostController {

    private final ClubPostService clubPostService;

    @GetMapping("")
    @Operation(summary = "동아리 공지 전체 조회", description = "clubId를 바탕으로 동아리 공지 전체를 조회합니다")
    public ResponseEntity<ClubPostListResponse> getAllClubPostsByClubId(@PathVariable Long clubId) {
//        clubMemberService.validateClubMember(clubId);
        ClubPostListResponse allClubPostsByClubId = clubPostService.findAllClubPostsByClubId(clubId);
        log.info("ClubId로 동아리 공지 전체 조회 : {} ", allClubPostsByClubId);
        return ResponseEntity.ok(allClubPostsByClubId);
    }

    @GetMapping("/summary")
    @Operation(summary = "동아리 공지 요약 전체 조회", description = "clubId를 바탕으로 동아리 공지에 대한 요약 전체를 조회합니다")
    public ResponseEntity<ClubPostSummaryListResponse> getAllClubPostsSummaryByClubId(@PathVariable Long clubId) {
//        clubMemberService.validateClubMember(clubId);
        ClubPostSummaryListResponse allClubPostsSummaryByClubId = clubPostService.findAllClubPostsSummaryByClubId(clubId);
        log.info("ClubId로 동아리 공지 전체 조회(제목, 날짜) : {} ", allClubPostsSummaryByClubId);
        return ResponseEntity.ok(allClubPostsSummaryByClubId);
    }

    @GetMapping("{postId}")
    @Operation(summary = "동아리 공지 단건 조회", description = "postId를 바탕으로 동아리 공지 하나에 대한 정보를 조회합니다")
    public ResponseEntity<ClubPostResponse> getPostByPostId(@PathVariable Long clubId, @PathVariable Long postId) {
//        clubMemberService.validateClubMember(clubId);
        ClubPostResponse clubPostByPostId = clubPostService.findClubPostByPostId(postId);
        log.info("PostId로 동아리 공지 조회 : {} ", clubPostByPostId);
        return ResponseEntity.ok(clubPostByPostId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "동아리 공지 생성", description = "동아리 공지를 하나 생성합니다")
    public ResponseEntity<ClubPostCreateResponse> createClubPost(
            @PathVariable Long clubId,
            @Parameter(description = "공지 사항 생성 요청 정보", schema = @Schema(implementation = ClubPostCreateRequest.class))
            @RequestPart("clubPostCreateRequest") String clubPostCreateRequestString,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        // JSON String을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ClubPostCreateRequest clubPostCreateRequest;
        try {
            clubPostCreateRequest = objectMapper.readValue(clubPostCreateRequestString, ClubPostCreateRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format", e);
        }

        ClubPostCreateResponse clubPostCreateResponse = clubPostService.createClubPost(clubId, clubPostCreateRequest, imageFile);
        log.info("동아리 공지 작성, postId: {} ", clubPostCreateResponse);
        return ResponseEntity.ok(clubPostCreateResponse);
    }


    @PatchMapping("{postId}/like")
    @Operation(summary = "동아리 공지 좋아요 증가", description = "postId를 바탕으로 동아리 공지의 좋아요를 1 증가시킵니다")
    public ResponseEntity<ClubPostLikesResponse> increaseClubPostLikes(@PathVariable Long clubId, @PathVariable Long postId) {
//        clubMemberService.validateClubMember(clubId);
        ClubPostLikesResponse clubPostLikesResponse = clubPostService.increaseClubPostLikes(clubId, postId);
        log.info("동아리 공지 좋아요, likes: {} ", clubPostLikesResponse);
        return ResponseEntity.ok(clubPostLikesResponse);
    }
}
