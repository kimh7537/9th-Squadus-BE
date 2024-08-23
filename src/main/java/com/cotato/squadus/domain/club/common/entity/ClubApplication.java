package com.cotato.squadus.domain.club.common.entity;

import com.cotato.squadus.domain.auth.enums.ApplicationStatus;
import com.cotato.squadus.domain.auth.entity.Member;
import com.cotato.squadus.domain.club.recruit.entity.RecruitingPost;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "club_application")
public class ClubApplication {

    @Id @GeneratedValue
    private Long applicationIdx;

    @ManyToOne
    @JoinColumn(name = "member_idx")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "club_idx")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "recruiting_post_id")
    private RecruitingPost recruitingPost;

    private LocalDateTime appliedAt;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "application_questions", joinColumns = @JoinColumn(name = "application_id"))
    @MapKeyColumn(name = "question_index")
    @Column(name = "questions")
    private Map<Integer, String> questions = new HashMap<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "recruiting_post_answers", joinColumns = @JoinColumn(name = "application_id"))
    @MapKeyColumn(name = "answer_index")
    @Column(name = "answers")
    private Map<Integer, String> answers = new HashMap<>();

    @Builder
    public ClubApplication(Member member, Club club, LocalDateTime appliedAt, ApplicationStatus applicationStatus, Map<Integer, String> questions, Map<Integer, String> answers, RecruitingPost recruitingPost) {
        this.member = member;
        this.club = club;
        this.appliedAt = appliedAt;
        this.applicationStatus = applicationStatus;
        this.questions = new HashMap<>(questions);
        this.answers = answers;
        this.recruitingPost = recruitingPost;
    }

    public void updateApplicationState(ApplicationStatus status) {
        this.applicationStatus = status;
    }
}
