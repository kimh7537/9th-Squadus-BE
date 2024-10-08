package com.cotato.squadus.domain.club.schedule.entity;

import static jakarta.persistence.CascadeType.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.cotato.squadus.common.entity.BaseTimeEntity;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubAdminMember;
import com.cotato.squadus.domain.club.schedule.enums.ScheduleCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "club_schedule")
@EntityListeners(AuditingEntityListener.class)
public class ClubSchedule extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long scheduleIdx;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "club_id")
	private Club club;

	private String title;

	@Enumerated(EnumType.STRING)
	private ScheduleCategory scheduleCategory;

	private String content;

	@OneToMany(mappedBy = "clubSchedule", cascade = ALL)
	private List<ScheduleComment> scheduleComments;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "club_member_idx")
	private ClubAdminMember author;

	private String location; // 추후에 Address 클래스로 바뀔 여지 있음

	private String equipment;

	@Temporal(TemporalType.DATE)
	private LocalDate date;

	@Temporal(TemporalType.TIME)
	private LocalTime startTime;

	@Temporal(TemporalType.TIME)
	private LocalTime endTime;

	//    private List<Vote> votes;
	//    private List<Participant> participants;

	@Builder
	public ClubSchedule(Club club, String title, ScheduleCategory scheduleCategory, String content,
		ClubAdminMember author, String location, String equipment, LocalDate date, LocalTime startTime,
		LocalTime endTime) {
		this.club = club;
		this.title = title;
		this.scheduleCategory = scheduleCategory;
		this.content = content;
		this.author = author;
		this.location = location;
		this.equipment = equipment;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public void update(String title, ScheduleCategory scheduleCategory, String content, String location,
		String equipment, LocalDate date, LocalTime startTime, LocalTime endTime) {
		this.title = title;
		this.scheduleCategory = scheduleCategory;
		this.content = content;
		this.location = location;
		this.equipment = equipment;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
	}
}
