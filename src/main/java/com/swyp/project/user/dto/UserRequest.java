package com.swyp.project.user.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequest {

	public record CreateProfile(
		String name,
		LocalDateTime birthdate,
		String gender,
		List<Long> keywordIds
	) {
	}
}
