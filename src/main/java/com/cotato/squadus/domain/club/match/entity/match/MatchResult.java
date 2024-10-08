package com.cotato.squadus.domain.club.match.entity.match;

import com.cotato.squadus.domain.club.common.entity.Club;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "match_result")
public class MatchResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long matchResultIdx;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "match_post_id")
	private MatchPost matchPost;  // 매칭 게시글

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "home_club_id")
	private Club homeClub;  // 홈팀

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "away_club_id")
	private Club awayClub;  // 어웨이팀

	private Integer homeScore;  // 홈팀 점수
	private Integer awayScore;  // 어웨이팀 점수

	private Boolean isFinalizedHome = false; // Home팀 결과가 확정되었는지 여부
	private Boolean isfinalizedAway = false; // Away팀 결과가 확정되었는지 여부

	@Builder
	public MatchResult(MatchPost matchPost, Club homeClub, Club awayClub, Integer homeScore, Integer awayScore) {
		this.matchPost = matchPost;
		this.homeClub = homeClub;
		this.awayClub = awayClub;
		this.homeScore = homeScore;
		this.awayScore = awayScore;
	}

	public void updateScores(Integer homeScore, Integer awayScore) {
		if (isFinalizedHome || isfinalizedAway)
			throw new IllegalStateException("결과가 이미 확정되었습니다.");
		this.homeScore = homeScore;
		this.awayScore = awayScore;
	}

	public void finalizeHomeResult() {
		this.isFinalizedHome = true;
	}

	public void finalizeAwayResult() {
		this.isfinalizedAway = true;
	}

	public void setIsFinalizedHome(boolean isFinalizedHome) {
		this.isFinalizedHome = isFinalizedHome;
	}

	public void setIsfinalizedAway(boolean isfinalizedAway) {
		this.isfinalizedAway = isfinalizedAway;
	}
}
