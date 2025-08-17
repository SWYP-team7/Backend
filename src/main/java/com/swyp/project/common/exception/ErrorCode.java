package com.swyp.project.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

	// JWT 토큰 관련
	INVALID_JWT_SIGNATURE(401, "잘못된 JWT 서명입니다."),
	EXPIRED_JWT_TOKEN(401, "만료된 JWT 토큰입니다."),
	UNSUPPORTED_JWT_TOKEN(401, "지원하지 않는 JWT 토큰입니다."),
	INVALID_JWT_TOKEN(401, "JWT 토큰이 잘못되었습니다."),
	EMPTY_JWT_TOKEN(401, "헤더에 JWT 토큰이 없습니다."),
	;

	private final int status;
	private final String message;

	ErrorCode(int status, String message) {
		this.status = status;
		this.message = message;
	}
}

