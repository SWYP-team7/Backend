package com.swyp.project.common.exception;

public class UserNotFoundException extends BaseException{
	public UserNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public UserNotFoundException() {
		super(ErrorCode.USER_NOT_FOUND);
	}
}
