package com.swyp.project.common.auth.service;

import org.springframework.stereotype.Service;

import com.swyp.project.common.auth.dto.AuthRequest;
import com.swyp.project.common.auth.dto.AuthResponse;
import com.swyp.project.common.auth.jwt.JwtUtil;
import com.swyp.project.user.domain.User;
import com.swyp.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	// public AuthResponse tempLogin(){
	// 	User user = User.builder().id(1L).build();
	// 	return new AuthResponse(jwtUtil.generateAccessToken(user), true);
	// }
	//
	public void login(AuthRequest authRequest){
		User user = userRepository.findBySocialId(authRequest.socialId())
			.orElseGet(() -> userRepository.save(
				User.builder()
				.socialId(authRequest.socialId())
				.build()));
	}
}
