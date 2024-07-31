package com.cotato.squadus.domain.club.post.entity;

import com.cotato.squadus.common.entity.BaseTimeEntity;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "club_post_comment")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ClubPostComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;

    private Long likes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_member_idx")
    private ClubMember clubMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private ClubPost clubPost;

    @Builder
    public ClubPostComment(String content, ClubMember clubMember, ClubPost clubPost, Long likes) {
        this.content = content;
        this.clubMember = clubMember;
        this.clubPost = clubPost;
        this.likes = likes;
    }

    public void increaseLikes() {
        this.likes += 1L;
    }
}
