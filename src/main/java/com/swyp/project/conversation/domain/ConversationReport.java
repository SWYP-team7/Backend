package com.swyp.project.conversation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "conversation_report")
public class ConversationReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conversation_id", nullable = false)
	private Conversation conversation;

	@Column(name = "duration_seconds", nullable = false)
	private Integer durationSeconds;

	@Column(name = "num_questions", nullable = false)
	private Integer numQuestions;

	@Column(name = "num_hearts", nullable = false)
	private Integer numHearts;

	@Lob
	@Column(name = "comment", nullable = false)
	private String comment;

	@Column(name = "next_recommended_topic", nullable = false)
	private String nextRecommendedTopic;

	@Column(name = "share_uuid", length = 36, nullable = false, unique = true)
	private String shareUuid;

}
