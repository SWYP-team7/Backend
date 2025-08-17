package com.swyp.project.common.auth.service;

import org.springframework.stereotype.Service;

import com.swyp.project.common.auth.dto.AuthResponse;
import com.swyp.project.common.auth.jwt.JwtUtil;
import com.swyp.project.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final JwtUtil jwtUtil;

	public AuthResponse tempLogin(){
		User user = User.builder().id(1L).build();
		return new AuthResponse(jwtUtil.generateAccessToken(user));
	}
}
