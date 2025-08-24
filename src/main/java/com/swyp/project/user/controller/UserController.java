package com.swyp.project.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.project.common.dto.ApiResponse;
import com.swyp.project.user.dto.UserRequest;
import com.swyp.project.user.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Operation(summary = "내 프로필 상태 조회", description = "로그인된 사용자의 프로필 작성 완료 여부를 확인합니다.")
	@GetMapping("/me/profile-status")
	public ResponseEntity<ApiResponse<UserResponse.ProfileStatus>> getProfileStatus() {
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "최초 프로필 생성", description = "온보딩 과정에서 사용자의 프로필 정보를 생성하고 계정을 활성화합니다.")
	@PostMapping("/me/profile")
	public ResponseEntity<ApiResponse<Void>> createProfile(@Valid @RequestBody UserRequest.CreateProfile request) {
		// profile_completed 값 true로 변경
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "마이페이지 조회", description = "로그인된 사용자의 마이페이지를 조회합니다.")
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<UserResponse.Profile>> getMyPage() {
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "내 기본 정보 조회", description = "로그인된 사용자의 프로필 정보를 조회합니다.")
	@GetMapping("/me/profile")
	public ResponseEntity<ApiResponse<UserResponse.Profile>> getProfile() {
		return ResponseEntity.ok(ApiResponse.success());
	}

	/*@Operation(summary = "내 키워드 조회", description = "로그인된 사용자의 키워드 정보를 조회합니다.")
	@GetMapping("/me/profile")
	public ResponseEntity<ApiResponse<UserResponse.Keyword>> getKeyword() {
		return ResponseEntity.ok(ApiResponse.success());
	}*/





	// PATCH /api/users/me/devices/touch
	// 사용자 앱 실행 or 로그인 시 last_login_at을 갱신하라는 api

	// POST /api/users/me/devices
	// 알림 보내기 위한 주소 등록 api
}
