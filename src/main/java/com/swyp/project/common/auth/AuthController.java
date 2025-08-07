package com.swyp.project.common.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.project.common.auth.dto.AuthRequest;
import com.swyp.project.common.auth.dto.AuthResponse;
import com.swyp.project.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@RequestMapping("/oauth/kakao")
	public ApiResponse<AuthResponse> signup(AuthRequest authRequest) {
		// Implement signup logic here
		return ApiResponse.success(new AuthResponse("accessToken123"));
	}
}
