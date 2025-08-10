package com.swyp.project.conversation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConversationResponse {

	@Schema(name = "ConversationCreateResponse", description = "대화 생성 응답")
	public record CreateResponse(
		long id
	) {
	}


}
