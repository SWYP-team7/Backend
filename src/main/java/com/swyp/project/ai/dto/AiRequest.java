package com.swyp.project.ai.dto;

import java.util.List;

public class AiRequest {

	public record ConversationInfo(
		Integer participantCount,
		List<String> relation,
		String category,
		List<String> keywords
	) {
	}

	public record ReportInfo(
		Integer seconds,
		Integer questions,
		Integer hearts,
		String selectedTopic
	){
	}

}
