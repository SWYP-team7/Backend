package com.swyp.project.ai;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swyp.project.ai.dto.AiRequest;
import com.swyp.project.ai.dto.AiResponse;
import com.swyp.project.common.exception.ApiResponseException;
import com.swyp.project.common.exception.ErrorCode;
import com.swyp.project.common.exception.InvalidFormatException;
import com.swyp.project.conversation.dto.ConversationRequest;
import com.swyp.project.user.dto.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatGptClient implements AiClient {

	@Qualifier("chatGptWebClient")
	private final WebClient chatGptWebClient;

	private final ObjectMapper objectMapper;

	private static final String MODEL = "gpt-4.1";

	private static final String PROMPT_TEMPLATE = """
	당신은 '깊은 대화를 위한 질문 생성기'입니다. 
	   아래 입력값을 기반으로 총 5개의 질문 세트를 만드세요 (각 세트 4문항, 총 20문항).
	   각 세트는 1개의 주제와 1~2개의 키워드를 사용하여, 4단계(표면 → 배경 → 비교 → 확장) 질문으로 구성합니다.

	   [생성 규칙]
	   1. 톤과 말투는 따뜻하고 진지하며, 상대방의 경험·감정·관계를 탐구하는 방식으로 작성합니다.
		  예시:
		  - “처음 만났을 때의 인상과 지금의 인상이 달라진 부분이 있나요?”
		  - “서로에게 고마웠지만 말하지 못했던 순간이 있나요?”
		  - “상대방과 있으면 더 ‘나답다’ 느끼는 순간이 있나요?”
		  - “시간이 더 지난 후에도 상대방과의 관계에서 잊지 않았으면 하는 건 무엇인가요?”

	   2. 각 질문은 한 문장으로, 30자 이상 55자 이하로 작성합니다.

	   3. 질문 구조
	   각 세트는 4단계로 구성된 질문 흐름을 따릅니다.
		단계별 질문은 서로 다른 유형의 사고와 감정을 탐구하도록 만드세요.
	   1단계: 구체적 경험·상황 묻기
	   언제, 어디서, 누구와 같은 구체적 맥락을 중심으로 질문합니다.
	   예: “최근에 가장 즐거웠던 순간은 언제였나요?”
	   2단계: 이유·배경 탐색하기
	   해당 경험이 특별하거나 의미 있게 다가온 감정적 이유, 배경, 계기를 묻습니다.
	   예: “그 순간이 특별하게 느껴진 이유는 무엇이었나요?”
	   3. 질문 구조
	   각 세트는 4단계 질문 흐름으로 구성됩니다.
		단계별 질문은 서로 다른 사고와 감정을 탐구하도록 작성하세요.
	   1단계: 구체적 경험·상황 묻기
	   언제, 어디서, 누구와 같은 구체적 맥락 중심 질문
	   예: “최근에 가장 즐거웠던 순간은 언제였나요?”
	   2단계: 이유·배경 탐색하기
	   해당 경험이 특별하거나 의미 있게 다가온 감정적 이유, 배경, 계기 탐구
	   예: “그 순간이 특별하게 느껴진 이유는 무엇이었나요?”
	   3단계: 확장 탐색 (아래 유형 중 하나 선택)
	   아래 5가지 패턴 중 하나를 랜덤으로 사용합니다.
	   사용 가능한 유형 예시:
	   맥락 연결: 그 경험이 다른 상황·관계에 어떤 영향을 주었나요?
	   패턴 탐색: 비슷한 경험에서 반복적으로 느낀 점이 있나요?
	   시각 전환: 그 순간을 상대방은 어떻게 느꼈을까요?
	   자아 연결: 그 경험이 지금의 당신을 만드는 데 어떤 역할을 했나요?
	   가치 드러내기: 그 경험은 당신이 중요하게 여기는 가치와 어떤 관련이 있었나요?
	   4단계: 확장·성찰 질문 (랜덤 패턴 적용)
	   아래 5가지 패턴 중 하나를 랜덤으로 사용합니다.
	   바람/희망: 앞으로 더 바라는 점이나 기대하는 변화
	   조언/역할: 내가 어떤 행동을 하면 도움이 될지
	   변화 상상: 상황이나 관계가 달라진다면 어떤 모습일지
	   가정(IF): 과거로 돌아간다면 무엇을 바꾸고 싶은지
	   성찰/의미: 그 경험이 나에게 어떤 의미로 남는지

	   4. 키워드 조건
		  - 각 세트는 1개 또는 2개의 키워드를 사용합니다.
		  - 예: `신뢰` / `관계+기억`

	   5. 응답 형식 
		  - JSON 데이터 형식
		  - questions 배열 안에 text가 들어간다.
		  - 응답은 한국어로 하고, ?를 제외한 기호는 사용하지 않는다.
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
						"keywords": {
						"type": "array",
						"items": {
							  "type": "string",
							  "minLength": 1,
							  "maxLength": 10
							},
						"minItems": 1,
						"maxItems": 2
						 },
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
					  "required": ["keywords", "questions"],
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

	private static final Map<String, String> PROMPT_INPUT = Map.of("role", "system", "content", PROMPT_TEMPLATE);

	public AiResponse.GeneratedQuestions generateQuestions(ConversationRequest.Create request, UserDto.Info userInfo) {
		String requestBody = createRequestBody(request, userInfo);

		String rawResponse = sendRequest(requestBody);

		if (rawResponse == null || rawResponse.isBlank()) {
			throw new ApiResponseException(ErrorCode.EMPTY_API_RESPONSE);
		}

		return parseResponse(rawResponse);
	}

	private String createRequestBody(ConversationRequest.Create request, UserDto.Info userInfo){
		List<Map<String, String>> input = new ArrayList<>();
		input.add(PROMPT_INPUT);
		input.add(createConversationInfoInput(request));

		try {
			String requestBody = objectMapper.writeValueAsString(Map.of(
				"model", MODEL,
				"input", input,
				"text", objectMapper.readValue(SCHEMA_JSON, new TypeReference<>() {
				})
			));

			log.info("requestBody: {}", requestBody);

			return requestBody;
		} catch (JsonProcessingException e) {
			throw new InvalidFormatException(ErrorCode.INVALID_FORMAT);
		}
	}

	private String sendRequest(String requestBody){
		try{
			String rawResponse = chatGptWebClient.post()
				.uri("/responses")
				.bodyValue(requestBody)
				.retrieve()
				.bodyToMono(String.class)
				.block(Duration.ofSeconds(60));

			log.info("rawResponse: {}", rawResponse);

			return rawResponse;
		} catch (WebClientResponseException e) {
			log.error("OpenAI 4xx/5xx status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
			throw new ApiResponseException(ErrorCode.API_REQUEST_FAILED);
		}
	}

	private AiResponse.GeneratedQuestions parseResponse(String rawResponse){
		try {
			JsonNode root = objectMapper.readTree(rawResponse);
			String text = root.at("/output/0/content/0/text").asText();
			AiResponse.GeneratedQuestions aiResponse = objectMapper.readValue(text, AiResponse.GeneratedQuestions.class);

			log.debug("contentJson: {}", aiResponse);

			return aiResponse;
		} catch (JsonProcessingException e) {
			throw new InvalidFormatException(ErrorCode.INVALID_FORMAT);
		}
	}

	private Map<String,String> createConversationInfoInput(ConversationRequest.Create request){
		AiRequest.ConversationInfo conversationInfo = new AiRequest.ConversationInfo(
			request.participantNames().size(),
			request.relationship(),
			request.category(),
			request.keywords()
		);

		String parsedConversationInfo;
		try{
			parsedConversationInfo = objectMapper.writeValueAsString(conversationInfo);
		} catch (JsonProcessingException e) {
			throw new InvalidFormatException(ErrorCode.INVALID_FORMAT);
		}

		return Map.of("role", "user", "content", parsedConversationInfo);
	}
}

