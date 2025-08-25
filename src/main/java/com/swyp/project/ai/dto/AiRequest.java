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

}
