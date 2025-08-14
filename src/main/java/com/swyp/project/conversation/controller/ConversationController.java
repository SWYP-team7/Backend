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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.swyp.project.common.dto.ApiResponse;
import com.swyp.project.conversation.dto.ConversationRequest;
import com.swyp.project.conversation.dto.ConversationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/conversations")
@Tag(name = "Conversation")
public class ConversationController {

	@Operation(summary = "대화 생성", description = "새 대화 세션을 생성합니다.")
	@PostMapping
	public ResponseEntity<ApiResponse<ConversationResponse.Create>> createConversation(
		@Valid  @RequestBody ConversationRequest.Create request) {
		ConversationResponse.Create response = new ConversationResponse.Create(1L);
		return ResponseEntity.accepted().body(ApiResponse.success(response));
	}

	@Operation(summary = "대화 카드 생성", description = "새 질문 카드를 생성합니다.")
	@GetMapping("/{conversationId}/subscribe")
	public SseEmitter subscribeToConversation(@PathVariable Long conversationId){
		SseEmitter emitter = new SseEmitter(60_000L);// 60초 임시
		return emitter;
	}

	@Operation(summary = "카드 저장", description = "특정 질문 카드를 저장합니다. 로그인된 사용자를 기준으로 저장됩니다.")
	@PostMapping("/cards/{cardId}/saves")
	public ResponseEntity<ApiResponse<Void>> saveCard(@PathVariable Long cardId) {
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "카드 저장 취소", description = "저장했던 질문 카드를 '저장함'에서 삭제합니다.")
	@DeleteMapping("/cards/{cardId}/saves")
	public ResponseEntity<ApiResponse<Void>> unsaveCard(@PathVariable Long cardId) {
		return ResponseEntity.ok(ApiResponse.success());
	}

	//todo: url에 end 붙일지 말지?
	@Operation(summary = "대화 종료", description = "대화를 종료합니다.")
	@PatchMapping("/{conversationId}/end")
	public ResponseEntity<ApiResponse<ConversationResponse.End>> endConversation(@PathVariable Long conversationId){
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "음성 파일 업로드 URL 요청", description = "대화 음성 파일을 업로드할 수 있는 임시 URL을 발급받습니다.")
	@PostMapping("/{conversationId}/audio-upload-url")
	public ResponseEntity<ApiResponse<ConversationResponse.AudioUploadUrlResponse>> getAudioUploadUrl(@PathVariable Long conversationId) {
		// Naver Cloud SDK를 사용하여 Pre-signed URL 생성 로직
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "대화 참여자 목록 조회", description = "특정 대화의 참여자 목록을 조회합니다.")
	@GetMapping("/{conversationId}/participants")
	public ResponseEntity<ApiResponse<ConversationResponse.ParticipantInfos>> getConversationParticipants(@PathVariable Long conversationId){
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "분석 리포트 조회", description = "특정 대화의 기본 정보와 통계/분석 데이터를 조회합니다.")
	@GetMapping("/{conversationId}/report/analysis")
	public ResponseEntity<ApiResponse<ConversationResponse.ReportAnalysis>> getConversationReportAnalysis(@PathVariable Long conversationId){
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "다음 대화 추천 주제 조회", description = "AI가 생성한 다음 대화 추천 주제를 조회합니다.")
	@GetMapping("/{conversationId}/report/recommendation")
	public ResponseEntity<ApiResponse<ConversationResponse.ReportRecommendation>> getConversationRecommendation(@PathVariable Long conversationId) {
		return ResponseEntity.ok(ApiResponse.success());
	}

	//todo: 응답 형식 결정 필요
	@Operation(summary = "대화 내용 텍스트 조회", description = "특정 대화의 전체 대화록을 조회합니다.")
	@GetMapping("/{conversationId}/report/transcript")
	public ResponseEntity<ApiResponse<ConversationResponse.ReportTranscript>> getConversationTranscript(@PathVariable Long conversationId){
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "STT 변환 요청", description = "업로드된 음성 파일의 텍스트 변환(STT) 작업을 시작시킵니다.")
	@PostMapping("/{conversationId}/transcripts")
	public ResponseEntity<ApiResponse<Void>> requestTranscription(@PathVariable Long conversationId) {
		return ResponseEntity.accepted().body(ApiResponse.success());
	}

	@Operation(summary = "내 대화 기록 목록 조회", description = "커서 기반 페이지네이션을 사용하여 자신이 참여했던 대화 목록을 조회합니다.")
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<ConversationResponse.History>> getMyConversations(
		@Parameter(description = "페이지 당 항목 수") @RequestParam(defaultValue = "10") int size,
		@Parameter(description = "다음 페이지를 위한 커서 ID") @RequestParam(required = false) Long cursor // 추후 argumentresolver 적용
	) {
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "대화 기록 삭제", description = "특정 대화 기록을 삭제합니다.")
	@DeleteMapping("/{conversationId}")
	public ResponseEntity<ApiResponse<Void>> deleteConversation(@PathVariable Long conversationId){
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "저장한 카드 목록 조회", description = "자신이 저장한 카드 목록을 커서 기반 페이지네이션으로 조회합니다.")
	@GetMapping("/me/cards/saves")
	public ResponseEntity<ApiResponse<ConversationResponse.SavedCards>> getMySavedCards(
		@Parameter(description = "페이지 당 항목 수") @RequestParam(defaultValue = "10") int size,
		@Parameter(description = "다음 페이지를 위한 커서 ID") @RequestParam(required = false) Long cursor) {
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "대화 카테고리 목록 조회", description = "대화 시작 전 선택할 카테고리를 조회합니다.")
	@GetMapping("/categories")
	public ResponseEntity<ApiResponse<ConversationResponse.Category>> getConversationCategories(){
		return ResponseEntity.ok(ApiResponse.success());
	}
	@Operation(summary = "대화 키워드 목록 조회", description = "대화 시작 전 선택할 키워드를 조회합니다.")
	@GetMapping("/keywords")
	public ResponseEntity<ApiResponse<ConversationResponse.Keyword>> getConversationKeywords(){
		return ResponseEntity.ok(ApiResponse.success());
	}
}
