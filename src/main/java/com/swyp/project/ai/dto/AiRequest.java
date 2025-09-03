package com.swyp.project.ai.dto;

import java.util.List;

public class AiRequest {

	public record ConversationInfo(
		Integer participantCount,
		String relation,
		String category,
		List<String> keywords
	) {
	}

	public record ReportInfo(
		Integer seconds,
		Integer questions,
		Integer hearts,
		List<String> selectedTopics
	){
	}

}
