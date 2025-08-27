package com.swyp.project.common.exception;

public class ProfileAlreadyExistsException extends BaseException{
	public ProfileAlreadyExistsException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ProfileAlreadyExistsException() {
		super(ErrorCode.PROFILE_ALREADY_EXISTS);
	}
}
