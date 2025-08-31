package com.swyp.project.user.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {

	public record Info(
		String gender,
		LocalDate birthDate,
		List<String> keywords
	) {
	}
}
