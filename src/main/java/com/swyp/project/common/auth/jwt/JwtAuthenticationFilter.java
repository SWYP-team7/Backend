package com.swyp.project.common.auth.jwt;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swyp.project.common.auth.UserContext;
import com.swyp.project.common.auth.dto.UserInfo;
import com.swyp.project.common.dto.ApiResponse;
import com.swyp.project.common.exception.ErrorCode;
import com.swyp.project.common.exception.JwtValidationException;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String BEARER = "Bearer ";

	private final JwtUtil jwtUtil;
	private final ObjectMapper objectMapper;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		return isPermitAllPath(requestUri);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
		FilterChain filterChain)
		throws IOException, ServletException {

		String requestUri = httpRequest.getRequestURI();
		String method = httpRequest.getMethod();

		// OPTIONS 요청 우회
		if ("OPTIONS".equalsIgnoreCase(method)) {
			httpResponse.setStatus(HttpServletResponse.SC_OK);
			return;
		}

		// Authorization 헤더 검사
		String authHeader = httpRequest.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			writeJsonResponse(httpResponse,ApiResponse.failure(ErrorCode.EMPTY_JWT_TOKEN.getMessage(),
				ErrorCode.EMPTY_JWT_TOKEN.name()));
			return;
		}

		// 토큰 유효성 검사
		String accessToken = authHeader.substring(BEARER.length());

		try {
			Claims claims = jwtUtil.validateToken(accessToken);
			UserContext.set(new UserInfo(Long.valueOf(claims.getSubject())));

			filterChain.doFilter(httpRequest, httpResponse);
		} catch (JwtValidationException e) {
			writeJsonResponse(httpResponse, ApiResponse.failure(e.getMessage(), ErrorCode.INVALID_JWT_TOKEN.name()));
		} catch (Exception e) {
			writeJsonResponse(httpResponse,
				ApiResponse.failure(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),ErrorCode.INTERNAL_SERVER_ERROR.name()));
		} finally {
			UserContext.clear();
		}
	}

	private boolean isPermitAllPath(String uri) {
		return uri.startsWith("/api/auth") || uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs");
	}

	private void writeJsonResponse(HttpServletResponse response, ApiResponse<Void> dto) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json; charset=UTF-8");
		String json = objectMapper.writeValueAsString(dto);
		response.getWriter().write(json);
	}
}
