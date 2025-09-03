package com.swyp.project.ai.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AiResponse {

	public record GeneratedQuestions(
		List<QuestionList> questionLists
	) {}

	public record QuestionList(
		List<String> keywords,
		List<Question> questions
	) {}

	public record Question(int depth, String text){}

	public record GeneratedReport(
		String comment,
		String nextTopic
	){
	}

}
