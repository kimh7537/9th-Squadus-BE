package com.cotato.squadus.domain.club.recruit.entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.cotato.squadus.common.entity.BaseTimeEntity;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubAdminMember;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "recruiting_post")
@NoArgsConstructor
public class RecruitingPost extends BaseTimeEntity {

	@Id
	@GeneratedValue
	private Long postId;

	private String title;

	private Boolean isActive;

	private LocalDate startDate;
	private LocalDate endDate;

	//    private String content;

	//    private String image;

	@ManyToOne
	@JoinColumn(name = "club_member_idx")
	private ClubAdminMember author;

	@ManyToOne
	@JoinColumn(name = "club_id")
	private Club club;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "recruiting_post_questions", joinColumns = @JoinColumn(name = "post_id"))
	@MapKeyColumn(name = "question_index")
	@Column(name = "question")
	private Map<Integer, String> questions = new HashMap<>();

	@Builder
	public RecruitingPost(String title, Boolean isActive, LocalDate startDate, LocalDate endDate,
		ClubAdminMember author, Club club, Map<Integer, String> questions) {
		this.title = title;
		this.isActive = isActive;
		this.startDate = startDate;
		this.endDate = endDate;
		this.author = author;
		this.club = club;
		this.questions = questions;
	}

	//    private Long views;
	//
	//    private Long likes;

}
