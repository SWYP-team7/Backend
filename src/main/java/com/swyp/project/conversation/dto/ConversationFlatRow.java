package com.swyp.project.conversation.dto;

import java.time.LocalDateTime;

import com.swyp.project.conversation.domain.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConversationFlatRow {

	private Long conversationId;
	private LocalDateTime createdAt;
	private Category category;
	private String participantName;
	private Integer durationSeconds;
}
