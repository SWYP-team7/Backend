package com.swyp.project.conversation.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConversationRequest {

	public record Create(
		List<ParticipantInfo> participantsInfo,
		List<Long> categoryIds,
		List<KeywordInfo> keywords
	) {}

	public record ParticipantInfo(
		String userCode,      // 회원이면 코드, 비회원이면 null
		String nonUserName,   // 비회원이면 이름, 회원이면 null
		String relationship,
		int intimacyLevel
		){}

	// 직접 입력: {id: null, content: "영화"}
	// 기존 키워드 선택: {id: 101, content: null}
	public record KeywordInfo(
		Long id,
		String content
	){}
}
