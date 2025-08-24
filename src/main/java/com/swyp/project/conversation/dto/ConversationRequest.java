package com.swyp.project.conversation.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConversationRequest {

	public record Create(
		List<String> participantNames,
		Long categoryId,
		List<KeywordInfo> keywords
	) {}



	// 직접 입력: {id: null, content: "영화"}
	// 기존 키워드 선택: {id: 101, content: null}
	public record KeywordInfo(
		Long id,
		String content
	){}

	public record End(List<CardDwellTime> dwellTimes) {
	}

	public record CardDwellTime(
		Long cardId,
		double durationSeconds
	){}
}
