package com.swyp.project.common.exception;

public class RelationshipNotFound extends BaseException {
	public RelationshipNotFound() {
		super(ErrorCode.RELATIONSHIP_NOT_FOUND);
	}
}
