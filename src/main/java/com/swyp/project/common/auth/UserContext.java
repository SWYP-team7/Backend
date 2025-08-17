package com.swyp.project.common.auth;

import com.swyp.project.common.auth.dto.UserInfo;

public class UserContext {
	private static final ThreadLocal<UserInfo> userThreadLocal = new ThreadLocal<>();

	public static void set(UserInfo userInfo) {
		userThreadLocal.set(userInfo);
	}

	public static UserInfo get() {
		return userThreadLocal.get();
	}

	public static void clear() {
		userThreadLocal.remove();
	}
}
