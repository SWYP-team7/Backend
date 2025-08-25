package com.swyp.project.user.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {

	public record Info(
		String gender,
		Integer age,
		List<String> traitKeywords,
		List<String> conversationStyleKeywords,
		List<String> interestKeywords
	) {
	}
}
