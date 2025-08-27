package com.swyp.project.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.project.common.dto.ApiResponse;
import com.swyp.project.user.UserService;
import com.swyp.project.user.dto.ProfileKeywordResponse;
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
	private static final long TEMP_USER_ID = 1L;

	@Operation(summary = "내 프로필 상태 조회", description = "로그인된 사용자의 프로필 작성 완료 여부를 확인합니다.")
	@GetMapping("/me/profile-status")
	public ResponseEntity<ApiResponse<UserResponse.ProfileStatus>> getProfileStatus() {
		UserResponse.ProfileStatus response = userService.getProfileStatus(TEMP_USER_ID);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	@Operation(summary = "프로필 키워드 전체 조회", description = "프로필 키워드를 조회합니다.")
	@GetMapping("/profile-keywords")
	public ResponseEntity<ApiResponse<ProfileKeywordResponse.ProfileKeywordByCategory>> getProfileKeyword() {
		ProfileKeywordResponse.ProfileKeywordByCategory response = userService.findProfileKeyword();
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	@Operation(summary = "최초 프로필 생성", description = "온보딩 과정에서 사용자의 프로필 정보를 생성하고 계정을 활성화합니다.")
	@PostMapping("/me/profile")
	public ResponseEntity<ApiResponse<Void>> createProfile(@Valid @RequestBody UserRequest.CreateProfile request) {
		userService.createProfile(request, TEMP_USER_ID);
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "마이페이지 조회", description = "로그인된 사용자의 마이페이지를 조회합니다.")
	@GetMapping("/me/summary")
	public ResponseEntity<ApiResponse<UserResponse.Summary>> getSummary() {
		UserResponse.Summary response = userService.getSummary(TEMP_USER_ID);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	@Operation(summary = "내 기본 정보 조회", description = "로그인된 사용자의 프로필 정보를 조회합니다.")
	@GetMapping("/me/profile")
	public ResponseEntity<ApiResponse<UserResponse.Profile>> getProfile() {
		UserResponse.Profile response = userService.getProfile(TEMP_USER_ID);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	/*@Operation(summary = "내 기본 정보 수정", description = "로그인된 사용자의 프로필 정보를 수정합니다.")
	@PutMapping("/me/profile")
	public ResponseEntity<ApiResponse<Void>> updateProfile() {
		userService.updateProfile(TEMP_USER_ID);
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "내 프로필 키워드 수정 화면 조회", description = "수정 화면에 필요한 전체 프로필 키워드 목록과 선택 여부를 조회합니다.")
	@GetMapping("/me/profile-keywords")
	public ResponseEntity<ApiResponse<ProfileKeywordResponse.ProfileKeywordsForEdit>> getProfileKeywordsForEdit() {
		ProfileKeywordResponse.ProfileKeywordsForEdit response = userService.getProfileKeywordsForEdit(TEMP_USER_ID);
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "내 키워드 수정", description = "로그인된 사용자의 키워드 정보를 수정합니다.")
	@PutMapping("/me/profile-keywords")
	public ResponseEntity<ApiResponse<UserResponse.Keyword>> getKeyword() {
		return ResponseEntity.ok(ApiResponse.success());
	}*/
}
