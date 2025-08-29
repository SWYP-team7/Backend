package com.swyp.project.user.dto;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {
	public record ProfileStatus(
		boolean profileCompleted
	) {
	}

	@Builder
	public record Profile(
		String profileImageUrl,
		String name,
		LocalDate birthdate,
		String gender
	) {
	}

	@Builder
	public record Summary(
		String profileImageUrl,
		String name,
		String email
	){
	}
}
