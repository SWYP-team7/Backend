package com.swyp.project.conversation.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConversationRequest {

	public record Create(
		List<ParticipantInfo> participantsInfo,
		Long category,
		List<KeywordInfo> keywords,
		String relationship,
		int intimacyLevel
	) {}

	public record ParticipantInfo(
		Long userId, // 비회원이면 null
		String userName,
		boolean isUser
	){}

	// 직접 입력: {id: null, content: "영화"}
	// 기존 키워드 선택: {id: 101, content: null}
	public record KeywordInfo(
		Long id,
		String content
	){}
}
