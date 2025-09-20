package com.swyp.project.common.auth.oauthclient;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.swyp.project.common.auth.dto.KakaoUserInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoAuthClient {
	private final WebClient kakaoApiWebClient;

	public KakaoUserInfo getUserInfo(String jwt) {
		return kakaoApiWebClient.get()
			.uri("/v2/user/me")
			.header("Authorization", "Bearer " + jwt)
			.retrieve()
			.bodyToMono(KakaoUserInfo.class)
			.block();
	}
}
