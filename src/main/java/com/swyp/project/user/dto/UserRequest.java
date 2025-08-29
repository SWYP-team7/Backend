package com.swyp.project.user.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequest {

	public record UpsertProfile(
		String name,
		LocalDate birthdate,
		String gender,
		List<Long> keywordIds
	) {
	}


}
