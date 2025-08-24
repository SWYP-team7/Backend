package com.swyp.project.common.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

	@Value("${openai.api.key}")
	private String openAiApiKey;

	@Bean
	public WebClient kakaoWebClient(WebClient.Builder builder) {
		// HttpClient 설정을 미리 정의
		HttpClient httpClient = HttpClient.create()
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000) // 연결 타임아웃: 3초
			.responseTimeout(Duration.ofSeconds(5)); // 응답 타임아웃: 5초

		return builder
			.clone() // 1. 독립적인 설정을 위해 builder를 복제
			.baseUrl("https://kauth.kakao.com")
			.clientConnector(new ReactorClientHttpConnector(httpClient)) // 2. 불필요한 핸들러 제거하고 깔끔하게 설정
			.defaultHeaders(h -> h.setAccept(java.util.List.of(MediaType.APPLICATION_JSON)))
			.build();
	}

	@Bean
	public WebClient kakaoApiWebClient(WebClient.Builder builder) {
		// 이 WebClient는 Spring의 기본 HttpClient 설정을 사용하게 됨
		return builder
			.clone() // 1. 독립적인 설정을 위해 builder를 복제
			.baseUrl("https://kapi.kakao.com")
			.build();
	}

	@Bean
	public WebClient chatGptWebClient(WebClient.Builder builder) {
		HttpClient httpClient = HttpClient.create()
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 연결 타임아웃 5초
			.responseTimeout(Duration.ofSeconds(60));

		return builder
			.clone()
			.baseUrl("https://api.openai.com/v1/responses") // ✅ OpenAI API 엔드포인트
			.clientConnector(new ReactorClientHttpConnector(httpClient))
			.defaultHeaders(h -> {
				h.setBearerAuth(openAiApiKey); // ✅ API Key 설정
				h.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
				h.setContentType(MediaType.APPLICATION_JSON);
			})
			.build();
	}
}
