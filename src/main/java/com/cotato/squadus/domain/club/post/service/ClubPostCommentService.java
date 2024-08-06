package com.cotato.squadus.domain.club.post.service;

import com.cotato.squadus.api.post.dto.ClubPostCommentCreateRequest;
import com.cotato.squadus.api.post.dto.ClubPostCommentCreateResponse;
import com.cotato.squadus.api.post.dto.ClubPostCommentLikeResponse;
import com.cotato.squadus.api.post.dto.ClubPostCommentResponse;
import com.cotato.squadus.common.error.ErrorCode;
import com.cotato.squadus.common.error.exception.AppException;
import com.cotato.squadus.domain.auth.service.ClubMemberService;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.common.repository.ClubMemberRepository;
import com.cotato.squadus.domain.club.post.entity.ClubPost;
import com.cotato.squadus.domain.club.post.entity.ClubPostComment;
import com.cotato.squadus.domain.club.post.repository.ClubPostCommentRepository;
import com.cotato.squadus.domain.club.post.repository.ClubPostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubPostCommentService {

    private final ClubPostRepository clubPostRepository;
    private final ClubPostCommentRepository clubPostCommentRepository;
    private final ClubMemberService clubMemberService;

    public List<ClubPostCommentResponse> findAllClubPostComments(Long postId) {
        List<ClubPostCommentResponse> clubPostCommentResponseList = clubPostCommentRepository.findAllByClubPost_PostId(postId).stream()
                .map(ClubPostCommentResponse::from).toList();
        return clubPostCommentResponseList;

    }

    @Transactional
    public ClubPostCommentLikeResponse increaseClubPostCommentLike(Long commentId) {
        if(isClubPostCommentAuthor(commentId)) {
            throw new AppException(ErrorCode.CLUB_POST_COMMENT_AUTHOR);
        }
        ClubMember clubMember = clubMemberService.findClubMemberBySecurityContextHolder();

        ClubPostComment clubPostComment = clubPostCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디를 가진 댓글이 존재하지 않습니다."));
        clubPostComment.increaseLikes();
        clubPostCommentRepository.save(clubPostComment);
        return new ClubPostCommentLikeResponse(clubPostComment.getLikes());
    }

    @Transactional
    public ClubPostCommentCreateResponse createClubPostComment(Long clubId, Long postId, ClubPostCommentCreateRequest clubPostCommentCreateRequest) {

        ClubPost clubPost = clubPostRepository.findByPostId(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디를 가진 동아리 공지가 존재하지 않습니다"));

        ClubMember clubMember = clubMemberService.findClubMemberBySecurityContextHolder();

        ClubPostComment clubPostComment = ClubPostComment.builder()
                .content(clubPostCommentCreateRequest.content())
                .clubMember(clubMember)
                .clubPost(clubPost)
                .likes(0L)
                .build();

        ClubPostComment save = clubPostCommentRepository.save(clubPostComment);
        return new ClubPostCommentCreateResponse(save.getId());

    }

    private Boolean isClubPostCommentAuthor(Long postId) {
        Long clubMemberId = clubMemberService.findClubMemberBySecurityContextHolder().getClubMemberIdx();
        ClubPostComment clubPostComment = clubPostCommentRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디를 가진 동아리 공지 댓글이 존재하지 않습니다."));
        Long clubMemberIdFromComment = clubPostComment.getClubMember().getClubMemberIdx();

        return clubMemberIdFromComment.equals(clubMemberId);
    }

}
