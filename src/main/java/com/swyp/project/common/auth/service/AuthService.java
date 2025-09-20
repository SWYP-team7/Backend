package com.swyp.project.common.auth.service;

import org.springframework.stereotype.Service;

import com.swyp.project.common.auth.dto.AuthRequest;
import com.swyp.project.common.auth.dto.AuthResponse;
import com.swyp.project.common.auth.dto.KakaoUserInfo;
import com.swyp.project.common.auth.jwt.JwtUtil;
import com.swyp.project.common.auth.oauthclient.KakaoAuthClient;
import com.swyp.project.user.domain.User;
import com.swyp.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final JwtUtil jwtUtil;
	private final KakaoAuthClient kakaoAuthClient;
	private final UserRepository userRepository;

	public AuthResponse tempLogin(){
		User user = User.builder().id(1L).build();
		return new AuthResponse(jwtUtil.generateAccessToken(user), true);
	}

	public KakaoUserInfo login(AuthRequest authRequest){
		// 로그인 로직 추가 필요
		return kakaoAuthClient.getUserInfo(authRequest.jwt());
	}
}
