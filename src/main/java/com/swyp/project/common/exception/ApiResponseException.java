package com.swyp.project.common.exception;

public class ApiResponseException extends BaseException {
	public ApiResponseException(ErrorCode errorCode) {
		super(errorCode);
	}
}
