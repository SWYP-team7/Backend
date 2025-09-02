package com.swyp.project.conversation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.project.ai.dto.AiResponse;
import com.swyp.project.common.dto.ApiResponse;
import com.swyp.project.conversation.dto.ConversationRequest;
import com.swyp.project.conversation.dto.ConversationResponse;
import com.swyp.project.conversation.service.ConversationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/conversations")
@Tag(name = "Conversation")
@RequiredArgsConstructor
public class ConversationController {

	private final ConversationService conversationService;

	// @Operation(summary = "임시 대화 생성 API", description = "ChatGpt 기능 확인용.")
	// @PostMapping("/temp")
	// public ResponseEntity<ApiResponse<AiResponse.GeneratedQuestions>> createConversationTemp(
	// 	@Valid  @RequestBody ConversationRequest.Create request) {
	// 	AiResponse.GeneratedQuestions response = conversationService.generateQuestions(request);
	// 	return ResponseEntity.accepted().body(ApiResponse.success(response));
	// }

	@Operation(summary = "대화 생성", description = "새 대화 세션을 생성합니다.")
	@PostMapping
	public ResponseEntity<ApiResponse<ConversationResponse.Create>> createConversation(
		@Valid  @RequestBody ConversationRequest.Create request) {

		long conversationId = conversationService.createConversation(request);
		AiResponse.GeneratedQuestions questions = conversationService.generateQuestions(request);
		conversationService.saveGeneratedQuestions(questions, conversationId);

		ConversationResponse.Create response = new ConversationResponse.Create(conversationId, questions);
		return ResponseEntity.accepted().body(ApiResponse.success(response));
	}

	// @Operation(summary = "카드 저장", description = "특정 질문 카드를 저장합니다. 로그인된 사용자를 기준으로 저장됩니다.")
	// @PostMapping("/{conversationId}/saved-cards")
	// public ResponseEntity<ApiResponse<Void>> saveCard(
	// 	@PathVariable Long conversationId, @Valid @RequestBody ConversationRequest.ConversationCard request) {
	// 	conversationService.saveConversationCard(conversationId, request);
	// 	return ResponseEntity.ok(ApiResponse.success());
	// }

	// @Operation(summary = "카드 저장 취소", description = "저장했던 질문 카드를 '저장함'에서 삭제합니다.")
	// @DeleteMapping("/{conversationId}/saved-cards")
	// public ResponseEntity<ApiResponse<Void>> unsaveCard(
	// 	@PathVariable Long conversationId, @Valid @RequestBody ConversationRequest.ConversationCard request) {
	// 	return ResponseEntity.ok(ApiResponse.success());
	// }

	@Operation(summary = "대화 종료", description = "대화를 종료합니다.")
	@PatchMapping("/{conversationId}/end")
	public ResponseEntity<ApiResponse<ConversationResponse.End>> endConversation(
		@PathVariable Long conversationId, ConversationRequest.End request) {

		return ResponseEntity.ok(ApiResponse.success());
	}

	// @Operation(summary = "대화 참여자 목록 조회", description = "특정 대화의 참여자 목록을 조회합니다.")
	// @GetMapping("/{conversationId}/participants")
	// public ResponseEntity<ApiResponse<ConversationResponse.ParticipantInfos>> getConversationParticipants(@PathVariable Long conversationId){
	// 	return ResponseEntity.ok(ApiResponse.success());
	// }

	@Operation(summary = "분석 리포트 조회", description = "특정 대화의 기본 정보와 통계/분석 데이터를 조회합니다.")
	@GetMapping("/{conversationId}/report/analysis")
	public ResponseEntity<ApiResponse<ConversationResponse.ReportAnalysis>> getConversationReportAnalysis(@PathVariable Long conversationId){
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "내 대화 기록 목록 조회", description = "오프셋 기반 페이지네이션을 사용하여 자신이 참여했던 대화 목록을 조회합니다.")
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<ConversationResponse.History>> getMyConversations(
		@Parameter(description = "페이지 당 항목 수") @RequestParam(defaultValue = "10") int size,
		@Parameter(description = "오프셋") @RequestParam(required = false, defaultValue = "0") Long offset
	) {
		return ResponseEntity.ok(ApiResponse.success());
	}

	// @Operation(summary = "대화 기록 삭제", description = "특정 대화 기록을 삭제합니다.")
	// @DeleteMapping("/{conversationId}")
	// public ResponseEntity<ApiResponse<Void>> deleteConversation(@PathVariable Long conversationId){
	// 	return ResponseEntity.ok(ApiResponse.success());
	// }

	@Operation(summary = "저장한 카드 목록 조회", description = "자신이 저장한 카드 목록을 오프셋 기반 페이지네이션으로 조회합니다.")
	@GetMapping("/me/cards/saves")
	public ResponseEntity<ApiResponse<ConversationResponse.SavedCards>> getMySavedCards(
		@Parameter(description = "페이지 당 항목 수") @RequestParam(defaultValue = "10") int size,
		@Parameter(description = "오프셋") @RequestParam(required = false, defaultValue = "0") Long offset) {
		return ResponseEntity.ok(ApiResponse.success());
	}
}
