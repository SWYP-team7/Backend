package com.swyp.project.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.project.common.dto.ApiResponse;
import com.swyp.project.user.UserService;
import com.swyp.project.user.dto.UserRequest;
import com.swyp.project.user.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@Operation(summary = "프로필 생성 및 수정", description = "프로필을 생성 및 수정합니다.")
	@PatchMapping("/me/profile")
	public ResponseEntity<ApiResponse<Void>> upsertProfile(@Valid @RequestBody UserRequest.UpsertProfile request) {
		userService.upsertProfile(request);
		return ResponseEntity.ok(ApiResponse.success());
	}

	/*@Operation(summary = "내 프로필 상태 조회", description = "로그인된 사용자의 프로필 작성 완료 여부를 확인합니다.")
	@GetMapping("/me/profile-status")
	public ResponseEntity<ApiResponse<UserResponse.ProfileStatus>> getProfileStatus() {
		UserResponse.ProfileStatus response = userService.getProfileStatus();
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	@Operation(summary = "프로필 키워드 전체 조회", description = "프로필 키워드를 조회합니다.")
	@GetMapping("/profile-keywords")
	public ResponseEntity<ApiResponse<UserResponse.ProfileKeywordByCategory>> getProfileKeyword() {
		UserResponse.ProfileKeywordByCategory response = userService.getProfileKeyword();
		return ResponseEntity.ok(ApiResponse.success(response));
	}



	@Operation(summary = "마이페이지 조회", description = "로그인된 사용자의 마이페이지를 조회합니다.")
	@GetMapping("/me/summary")
	public ResponseEntity<ApiResponse<UserResponse.Summary>> getSummary() {
		UserResponse.Summary response = userService.getSummary();
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	@Operation(summary = "내 기본 정보 조회", description = "로그인된 사용자의 프로필 정보를 조회합니다.")
	@GetMapping("/me/profile")
	public ResponseEntity<ApiResponse<UserResponse.Profile>> getProfile() {
		UserResponse.Profile response = userService.getProfile();
		return ResponseEntity.ok(ApiResponse.success(response));
	}*/
}
