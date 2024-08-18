package com.cotato.squadus.domain.club.match.entity;

import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.match.enums.MatchingStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "mercenary_request")
public class MercenaryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mercenaryRequestIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mercenary_post_id")
    private MercenaryPost mercenaryPost;  // 신청 대상이 되는 매치 글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_member_id")
    private ClubMember clubMember;  // 신청한 동아리

    @Enumerated(EnumType.STRING)
    private MatchingStatus status;  // 대기, 승낙, 거절 상태 관리

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
    public MercenaryRequest(ClubMember clubMember, MercenaryPost mercenaryPost, MatchingStatus status){
        this.clubMember = clubMember;
        this.mercenaryPost = mercenaryPost;
        this.status = status;
    }

    public void setMercenaryPost(MercenaryPost mercenaryPost) {
        this.mercenaryPost = mercenaryPost;
    }


}
