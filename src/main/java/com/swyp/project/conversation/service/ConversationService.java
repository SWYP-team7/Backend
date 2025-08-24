package com.swyp.project.conversation.service;

import org.springframework.stereotype.Service;

import com.swyp.project.ai.AiClient;
import com.swyp.project.ai.dto.AiResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConversationService {
	private final AiClient aiClient;

	public AiResponse.GeneratedQuestions generateQuestions() {
		return aiClient.generateQuestions("");
	}
}
