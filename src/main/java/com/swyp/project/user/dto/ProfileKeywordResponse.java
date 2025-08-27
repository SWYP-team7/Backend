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
		long id,
		String name,
		int displayOrder,
		List<ProfileKeywordInfo> keywords
	){}

	@Builder
	public record ProfileKeywordInfo(
		long id,
		String content
	){}

	public record ProfileKeywordsForEdit(
		List<CategoryInfoForEdit> categories
	) {}

	public record CategoryInfoForEdit(
		Long categoryId,
		String categoryName,
		List<KeywordInfoForEdit> keywords
	) {}

	public record KeywordInfoForEdit(
		Long keywordId,
		String content,
		boolean isSelected // 내가 선택한 키워드인지 여부 (true/false)
	) {}
}
