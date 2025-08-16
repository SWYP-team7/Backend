package com.swyp.project.conversation.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConversationResponse {

	public record Create(
		long conversationId,
		String jobId,
		String sseUrl
	) {}

	public record End(
			long conversationId,
			long reportId,
			int durationSeconds
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
		Long userId,
		String name,
		boolean isUser
	) {}

	public record ReportAnalysis(
		List<String> participantNames,
		String category,
		List<String> keywords,
		LocalDateTime createdAt,
		int durationSeconds,
		int numQuestions,
		int numHearts

	) {
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

	public record ReportRecommendation(
		String nextTopicRecommendation
	) {
	}

	public record ReportSummary(
		String summary
	) {
	}

	public record AudioUploadUrlResponse(
		String uploadUrl
	) {
	}

	public record Category(
		Long id,
		String content
	) {
	}

	public record Keyword(
		Long id,
		String content
	){}
}
