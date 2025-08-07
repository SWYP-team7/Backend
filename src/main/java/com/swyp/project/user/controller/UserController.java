package com.swyp.project.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.project.common.dto.ApiResponse;
import com.swyp.project.user.dto.UpdateUserKeywordsRequest;
import com.swyp.project.user.dto.UpdateUserRequest;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Operation(summary = "유저 기본 정보 수정", description = "로그인된 사용자의 기본 정보를 수정합니다.")
	@PutMapping("/me/info")
	public ResponseEntity<ApiResponse<Void>> updateUserInfo(@RequestBody UpdateUserRequest request) {
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "유저 키워드 수정", description = "로그인된 사용자의 키워드를 수정합니다.")
	@PutMapping("/me/keywords")
	public ResponseEntity<ApiResponse<Void>>updateUserKeywords(@RequestBody UpdateUserKeywordsRequest request) {
		return ResponseEntity.ok(ApiResponse.success());
	}
}
