package com.swyp.project.conversation.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.swyp.project.ai.dto.AiResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConversationResponse {

	public record Create(
		long conversationId,
		AiResponse.GeneratedQuestions generatedQuestions
	) {}

	public record End(
			long conversationId
	) {
	}

	public record ReportTranscript(
		String transcript
	) {
	}

	public record  ParticipantInfos(
		List<String> participantNames
	) {}

	public record ReportAnalysis(
		List<String> participantNames,
		String topic,
		List<String> keywords,
		LocalDateTime createdAt,
		int durationSeconds,
		int numQuestions,
		int numHearts,
		String comment,
		String nextTopic
	) {
	}

	public record History(
		List<Summary> conversations
	) {
	}
	public record Summary(
		Long conversationId,
		String title,
		LocalDateTime createdAt,
		int durationSeconds,
		String category,
		List<String> keywords
	) {}


	public record SavedCards(
		List<SavedCardInfo> savedCards
	) {
	}

	public record SavedCardInfo(
		Long cardId,
		String content,
		List<String> keywords
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

	public record ShareResponse(
		String shareUrl
	) {
	}

	public record PublicReportResponse() {
	}
}
