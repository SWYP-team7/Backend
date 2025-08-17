package com.swyp.project.common.exception;

public class JwtValidationException extends BaseException {
	public JwtValidationException(ErrorCode errorCode) {
		super(errorCode);
	}
}
