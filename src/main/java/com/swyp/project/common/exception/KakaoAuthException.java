package com.swyp.project.common.exception;

public class KakaoAuthException extends BaseException {
	public KakaoAuthException() {
		super(ErrorCode.KAKAO_AUTH_ERROR);
	}
}
