package com.swyp.project.conversation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.project.common.dto.ApiResponse;
import com.swyp.project.conversation.dto.ConversationRequest;
import com.swyp.project.conversation.dto.ConversationResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

	@Operation(summary = "대화 생성", description = "참여자 목록을 입력하면 새 대화 세션을 생성합니다.")
	@PostMapping
	public ResponseEntity<ApiResponse<ConversationResponse.CreateResponse>> createConversation(
		@Valid  @RequestBody ConversationRequest.CreateRequest request) {
		return ResponseEntity.ok(ApiResponse.success());
	}

}
