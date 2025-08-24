package com.swyp.project.user.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {
	public record ProfileStatus(
		boolean profileCompleted
	) {
	}

	public record Profile() {
	}
}
