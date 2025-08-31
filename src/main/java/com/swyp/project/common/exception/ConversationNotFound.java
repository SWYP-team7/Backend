package com.swyp.project.common.exception;

public class ConversationNotFound extends BaseException{
	public ConversationNotFound() {
		super(ErrorCode.CONVERSATION_NOT_FOUND);
	}
}
