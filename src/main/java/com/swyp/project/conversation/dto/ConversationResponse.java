package com.swyp.project.conversation.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConversationResponse {

	public record Create(
		long conversationId,
		List<CardInfo> cardPool
	) {}

	public record CardInfo(Long cardId, String content, int level, String swipeType, String source) {}

	public record End(
			long conversationId,
			long reportId
	) {
	}

	public record ReportTranscript(
		String transcript
	) {
	}

	public record  ParticipantInfos(
		List<ParticipantInfo> participantInfos
	) {}

	public record ParticipantInfo(
		String userCode,
		String name,
		boolean isMember
	) {}

	public record ReportAnalysis() {
	}

	public record History(
		List<Summary> conversations,
		CursorInfo cursorInfo
	) {
	}
	public record Summary(
		Long conversationId,
		String title,
		LocalDateTime endedAt,
		int durationSeconds,
		String mainCategory
	) {}


	public record SavedCards(
		List<SavedCardInfo> savedCards,
		CursorInfo cursorInfo
	) {
	}

	public record SavedCardInfo(
		Long saveId,
		Long cardId,
		String content,
		String keyword,
		LocalDateTime savedAt
	) {}

	public record CursorInfo(
		Long nextCursor,
		boolean hasNext
	) {}
}
