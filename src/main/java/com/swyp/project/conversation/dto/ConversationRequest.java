package com.swyp.project.conversation.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConversationRequest {

	public record Create(
		List<String> participantNames,
		String relationship,
		String category,
		List<String> keywords
	) {}


	public record End(
		Integer durationSeconds,
		Integer numHearts
	) {
	}

	public record ConversationCard(
		int orderIndex,
		int depth
	) {}
}
