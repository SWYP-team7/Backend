package com.swyp.project.conversation.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConversationRequest {

	@Schema(name = "ConversationCreateRequest", description = "대화 생성 요청")
	public record CreateRequest(
		@Schema(description = "대화 참여자 userId 목록", example = "[1, 2]")
		@NotEmpty List<Long> participantIds
	) {
	}


}
