package com.swyp.project.common.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.project.common.auth.dto.AuthRequest;
import com.swyp.project.common.auth.dto.AuthResponse;
import com.swyp.project.common.dto.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Operation(
		summary = "카카오 로그인/회원가입",
		description = "카카오 인가 코드를 사용해서 로그인 또는 회원가입 처리"
	)
	@PostMapping("/oauth/kakao")
	public ResponseEntity<ApiResponse<AuthResponse>> signup(AuthRequest authRequest) {
		// Implement signup logic here
		return ResponseEntity.ok(ApiResponse.success(new AuthResponse("accessToken123")));
	}
}
