package com.cotato.squadus.domain.club.schedule.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.cotato.squadus.common.entity.BaseTimeEntity;
import com.cotato.squadus.domain.club.common.entity.ClubMember;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "schedule_comment")
@EntityListeners(AuditingEntityListener.class)
public class ScheduleComment extends BaseTimeEntity {

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
	@JoinColumn(name = "club_schedule")
	private ClubSchedule clubSchedule;

	@Builder
	public ScheduleComment(String content, Long likes, ClubMember clubMember, ClubSchedule clubSchedule) {
		this.content = content;
		this.likes = likes;
		this.clubMember = clubMember;
		this.clubSchedule = clubSchedule;
	}

	public void updateContent(String content) {
		this.content = content;
	}

	public void incrementLikes() {
		this.likes += 1;
	}
}
