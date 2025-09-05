package com.swyp.project.conversation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.swyp.project.conversation.dto.ConversationResponse;
import com.swyp.project.conversation.service.ConversationService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ConversationReportController {

	private final ConversationService conversationService;

	@Operation(summary = "분석 리포트 공유 페이지 조회", description = "특정 대화의 공유 페이지를 조회합니다.")
	@GetMapping("/reports/{share-uuid}")
	public String viewReport(@PathVariable(value = "share-uuid") String shareUuid, Model model) {
		ConversationResponse.ReportAnalysisForShare report = conversationService.getReportByShareUuid(shareUuid);
		model.addAttribute("report", report);
		return "report";
	}


}
