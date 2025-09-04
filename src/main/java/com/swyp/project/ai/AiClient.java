package com.swyp.project.ai;

import com.swyp.project.ai.dto.AiRequest;
import com.swyp.project.ai.dto.AiResponse;
import com.swyp.project.conversation.dto.ConversationRequest;
import com.swyp.project.user.dto.UserDto;

public interface AiClient {
	AiResponse.GeneratedQuestions generateQuestions(ConversationRequest.Create request, UserDto.Info userInfo);
	AiResponse.GeneratedReport generateReport(AiRequest.ReportInfo request);



}
