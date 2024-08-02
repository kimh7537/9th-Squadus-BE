package com.cotato.squadus.domain.club.post.entity;

import com.cotato.squadus.common.entity.BaseTimeEntity;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubAdminMember;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@Getter
@NoArgsConstructor
@Table(name = "club_post")
public class ClubPost extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "club_member_idx")
    private ClubAdminMember author;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    private String title;

    private String content;

    private String image;

    private Long views;

    private Long likes;

    @Builder
    public ClubPost(ClubAdminMember clubAdminMember, Club club, String title, String content, String image, Long views, Long likes) {
        this.author = clubAdminMember;
        this.club = club;
        this.title = title;
        this.content = content;
        this.image = image;
        this.views = views;
        this.likes = likes;
    }

    public ClubPost increaseLikes() {
        this.likes += 1L;
        return this;
    }

    public void increaseViews() {
        this.views += 1L;
    }
}
