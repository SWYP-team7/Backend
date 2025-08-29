package com.swyp.project.user.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileKeywordResponse {

	public record ProfileKeywordByCategory(
		List<CategoryInfo> categories
	){}

	@Builder
	public record CategoryInfo(
		String name,
		List<ProfileKeywordInfo> keywords
	){}

	@Builder
	public record ProfileKeywordInfo(
		long id,
		String content,
		boolean isSelected
	){}
}
