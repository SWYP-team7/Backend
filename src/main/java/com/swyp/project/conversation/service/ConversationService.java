package com.swyp.project.conversation.service;

import org.springframework.stereotype.Service;

import com.swyp.project.ai.AiClient;
import com.swyp.project.ai.dto.AiResponse;
import com.swyp.project.common.auth.UserContext;
import com.swyp.project.common.auth.dto.UserInfo;
import com.swyp.project.conversation.dto.ConversationRequest;
import com.swyp.project.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConversationService {
	private final AiClient aiClient;

	public AiResponse.GeneratedQuestions generateQuestions(ConversationRequest.Create request) {
		UserInfo userInfo = UserContext.get();
		long userId = userInfo.id();
		return aiClient.generateQuestions(request, null);
	}
}
