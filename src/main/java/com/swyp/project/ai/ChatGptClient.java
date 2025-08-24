package com.swyp.project.ai;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swyp.project.ai.dto.AiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatGptClient implements AiClient {

	@Qualifier("chatGptWebClient")
	private final WebClient chatGptWebClient;

	private final ObjectMapper objectMapper;

	private static final String MODEL = "gpt-4o-mini";

	private static final String PROMPT_TEMPLATE = """
			당신은 '깊은 대화를 위한 질문 생성기'입니다. 아래 입력값을 기반으로 총 5개의 질문 세트를 만드세요(각 세트 4문항, 총 20문항).
			각 세트는 1개의 주제와 1~2개의 키워드를 사용하여, 4단계(표면 → 배경 → 비교 → 확장) 질문으로 구성합니다.
			1. 말투와 톤
			질문은 따뜻하고 진지한 대화 분위기를 가져야 합니다.
			상대방의 경험·감정·관계를 자연스럽게 탐구하는 느낌으로 작성합니다.
			반드시 아래 예시와 같은 말투를 유지하세요:
			'처음 만났을 때의 인상과 지금의 인상이 달라진 부분이 있나요?'
			'서로에게 고마웠지만 말하지 못했던 순간이 있나요?'
			'상대방과 있으면 더 ‘나답다’ 느끼는 순간이 있나요?'
			'시간이 더 지난 후에도 상대방과의 관계에서 잊지 않았으면 하는 건 무엇인가요?'
			2. 문장 길이
			각 질문은 30~70자 이내로 작성합니다.
			3. 질문 구조 (심화 단계)
			1단계: 가볍게 경험이나 상황을 묻는다.
			2단계: 그 경험의 이유나 배경을 탐색한다.
			3단계: 다른 사례나 비교 경험을 묻는다.
			4단계: 앞으로의 바람이나 확장을 묻는다.
			4. 키워드 조건
			한 세트의 질문은 1개 또는 2개의 키워드를 사용합니다.
			예시: 신뢰 / 관계+기억
			5. 응답 생성
			questions의 text는 줄바꿈이 있다면 줄바꿈 기호를 넣고, 없다면 넣지 않습니다.
			해당 문장은 정상적인 문장이 되어야하고, 이상한 문장기호는 포함하면 안됩니다.
		""";

	private static final String SCHEMA_JSON = """
		{
			"format": {
			  "type": "json_schema",
			  "name": "questions",
			  "schema": {
				"type": "object",
				"properties": {
				  "questionLists": {
					"type": "array",
					"items": {
					  "type": "object",
					  "properties": {
						"keyword": { "type": "string" },
						"questions": {
						  "type": "array",
						  "items": {
							"type": "object",
							"properties": {
							  "depth": {
								"type": "integer",
								"minimum": 1,
								"maximum": 4
							  },
							  "text": {
								"type": "string",
								"minLength": 30,
								"maxLength": 70
							  }
							},
							"required": ["depth", "text"],
							"additionalProperties": false
						  },
						  "minItems": 4,
						  "maxItems": 4
						}
					  },
					  "required": ["keyword", "questions"],
					  "additionalProperties": false
					},
					"minItems": 5,
					"maxItems": 5
				  }
				},
				"required": ["questionLists"],
				"additionalProperties": false
			  },
			"strict": true
			}
		  }
		""";

	private static final List<Map<String, String>> INPUT = List.of(Map.of("role", "system", "content", PROMPT_TEMPLATE));

	public AiResponse.GeneratedQuestions generateQuestions(String prompt) {
		String requestBody;
		try {
			requestBody = objectMapper.writeValueAsString(Map.of(
				"model", MODEL,
				"input", INPUT,
				"text", objectMapper.readValue(SCHEMA_JSON, new TypeReference<>() {
				})
			));
			log.info("requestBody: {}", requestBody);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("JSON 직렬화 실패", e); //todo: 예외처리하기
		}

		String rawResponse = chatGptWebClient.post()
			.uri("/responses")
			.bodyValue(requestBody)
			.retrieve()
			.bodyToMono(String.class)
			.block(Duration.ofSeconds(60));

		log.info("rawResponse: {}", rawResponse);

		if (rawResponse == null || rawResponse.isBlank()) {
			throw new RuntimeException("API 응답이 비어있습니다."); //todo: 예외처리하기
		}

		try {
			JsonNode root = objectMapper.readTree(rawResponse);
			String text = root.at("/output/0/content/0/text").asText();
			AiResponse.GeneratedQuestions aiResponse = objectMapper.readValue(text, AiResponse.GeneratedQuestions.class);

			log.debug("contentJson: {}", aiResponse);

			return aiResponse;
		} catch (JsonProcessingException e) {
			throw new RuntimeException("API 응답 JSON 파싱 실패", e); //todo: 예외처리하기
		}
	}
}

