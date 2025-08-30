package com.swyp.project.user.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequest {

	public record UpsertProfile(
		@Size(min = 1, max = 30, message = "이름은 1~30여야 합니다.")
		String name,

		@Past(message = "생년월일은 과거 날짜여야 합니다.")
		LocalDate birthdate,

		@Pattern(regexp = "^(MALE|FEMALE)$", message = "성별은 MALE 또는 FEMALE이어야 합니다.")
		String gender,

		@Size(min = 1, message = "성향 & 성격 표현 키워드는 최소 1개입니다.")
		List<String> personalityKeywords,
		@Size(min = 1, message = "대화 스타일 키워드는 최소 1개입니다.")
		List<String> conversationKeywords,
		@Size(min = 1, message = "관심사 키워드는 최소 1개입니다.")
		List<String> interestKeywords
	) {
	}
}
