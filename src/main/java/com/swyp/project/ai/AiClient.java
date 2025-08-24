package com.swyp.project.ai;

import com.swyp.project.ai.dto.AiResponse;

public interface AiClient {
	AiResponse.GeneratedQuestions generateQuestions(String prompt);
}
