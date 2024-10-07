package com.cotato.squadus.domain.club.article.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.cotato.squadus.common.entity.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "article")
@EntityListeners(AuditingEntityListener.class)
public class Article extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long articleIdx;

	private String title;

	private String subtitle;

	private String type;

	private String tag;

	private String content;

	private Long views;

	private String imageUrl;

	@Builder
	public Article(String title, String subtitle, String type, String tag, String content, Long views,
		String imageUrl) {
		this.title = title;
		this.subtitle = subtitle;
		this.type = type;
		this.tag = tag;
		this.content = content;
		this.views = views;
		this.imageUrl = imageUrl; // 추가된 필드
	}

	public Article() {
	}

	public void setViews(Long views) {
		this.views = views;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
