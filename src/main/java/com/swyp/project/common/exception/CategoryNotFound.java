package com.swyp.project.common.exception;

public class CategoryNotFound extends BaseException {
	public CategoryNotFound() {
		super(ErrorCode.CATEGORY_NOT_FOUND);
	}
}
