package com.cotato.squadus.domain.club.schedule.service;

import com.cotato.squadus.api.schedule.dto.LikeResponse;
import com.cotato.squadus.api.schedule.dto.ScheduleCommentRequest;
import com.cotato.squadus.api.schedule.dto.ScheduleCommentResponse;
import com.cotato.squadus.common.error.ErrorCode;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.common.repository.ClubMemberRepository;
import com.cotato.squadus.domain.club.schedule.entity.ClubSchedule;
import com.cotato.squadus.domain.club.schedule.entity.ScheduleComment;
import com.cotato.squadus.domain.club.schedule.repository.ClubScheduleRepository;
import com.cotato.squadus.domain.club.schedule.repository.ScheduleCommentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleCommentService {

    private final ClubScheduleRepository clubScheduleRepository;
    private final ScheduleCommentRepository scheduleCommentRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Transactional
    public ScheduleCommentResponse addComment(Long scheduleId, ScheduleCommentRequest scheduleCommentRequest) {
        ClubSchedule schedule = clubScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + scheduleId));

        ClubMember member = clubMemberRepository.findById(scheduleCommentRequest.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + scheduleCommentRequest.getMemberId()));

        ScheduleComment comment = ScheduleComment.builder()
                .content(scheduleCommentRequest.getContent())
                .likes(0L)
                .clubMember(member)
                .clubSchedule(schedule)
                .build();

        scheduleCommentRepository.save(comment);

        return ScheduleCommentResponse.from(comment);
    }

    @Transactional
    public ScheduleCommentResponse updateComment(Long scheduleId, Long commentId, ScheduleCommentRequest scheduleCommentRequest) {
        ScheduleComment comment = scheduleCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));

        if (!comment.getClubMember().getClubMemberIdx().equals(scheduleCommentRequest.getMemberId())) {
            throw new EntityNotFoundException("엔티티를 찾을 수 없습니다.");
        }

        comment.updateContent(scheduleCommentRequest.getContent());
        scheduleCommentRepository.save(comment);

        return ScheduleCommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Long scheduleId, Long commentId, Long memberId) {
        ScheduleComment comment = scheduleCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));

        if (!comment.getClubMember().getClubMemberIdx().equals(memberId)) {
            throw new EntityNotFoundException("엔티티를 찾을 수 없습니다.");
        }

        scheduleCommentRepository.delete(comment);
    }


    public List<ScheduleCommentResponse> getAllComments(Long scheduleId) {
        List<ScheduleComment> comments = scheduleCommentRepository.findByClubScheduleScheduleIdx(scheduleId);
        return comments.stream()
                .map(ScheduleCommentResponse::from)
                .collect(Collectors.toList());
    }

    public LikeResponse likeComment(Long scheduleId, Long commentId) {
        ScheduleComment comment = scheduleCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));

        comment.incrementLikes();
        scheduleCommentRepository.save(comment);

        return LikeResponse.from(comment.getId(), comment.getLikes());
    }
}
