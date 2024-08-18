package com.cotato.squadus.domain.club.match.entity;

import com.cotato.squadus.common.entity.BaseTimeEntity;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.match.enums.MatchingStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "match_request")
public class MatchRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchRequestIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_post_id")
    private MatchPost matchPost;  // 신청 대상이 되는 매치 글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;  // 신청한 동아리

    @Enumerated(EnumType.STRING)
    private MatchingStatus status;  // 대기, 승낙, 거절 상태 관리

    private Boolean isConfirmedByHomeTeam; // home 팀에서 결과 확정했는지 여부
    private Boolean isConfirmedByAwayTeam; // away 팀에서 결과 확정했는지 여부

    private Boolean isLocked = false;  // 상태 변경 후 잠금 여부

    public void accept() {
        if (isLocked) throw new IllegalStateException("이미 처리된 요청은 수정할 수 없습니다.");
        this.status = MatchingStatus.ACCEPTED;
        this.isLocked = true;
    }

    public void reject() {
        if (isLocked) throw new IllegalStateException("이미 처리된 요청은 수정할 수 없습니다.");
        this.status = MatchingStatus.REJECTED;
        this.isLocked = true;
    }

    @Builder
    public MatchRequest(Club club, MatchPost matchPost, MatchingStatus status, Boolean isConfirmedByAwayTeam, Boolean isConfirmedByHomeTeam){
        this.club = club;
        this.matchPost = matchPost;
        this.status = status;
        this.isConfirmedByHomeTeam = isConfirmedByHomeTeam;
        this.isConfirmedByAwayTeam = isConfirmedByAwayTeam;
    }

    public void setMatchPost(MatchPost matchPost) {
        this.matchPost = matchPost;
    }


}