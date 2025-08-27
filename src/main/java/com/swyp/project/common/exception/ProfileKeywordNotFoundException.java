package com.swyp.project.common.exception;

public class ProfileKeywordNotFoundException extends BaseException{
	public ProfileKeywordNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ProfileKeywordNotFoundException() {
		super(ErrorCode.PROFILE_KEYWORD_NOT_FOUND);
	}
}
