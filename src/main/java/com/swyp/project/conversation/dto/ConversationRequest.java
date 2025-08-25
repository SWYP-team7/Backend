package com.swyp.project.conversation.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConversationRequest {

	public record Create(
		List<String> participantNames,
		String relation,
		String topic,
		List<String> keywords,
		boolean allowRecording
	) {}


	public record End(List<CardDwellTime> dwellTimes) {
	}

	public record CardDwellTime(
		Long cardId,
		double durationSeconds
	){}
}
