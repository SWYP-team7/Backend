package com.swyp.project.common.auth.jwt;

import static com.swyp.project.common.exception.ErrorCode.*;
import static java.nio.charset.StandardCharsets.*;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.swyp.project.common.exception.JwtValidationException;
import com.swyp.project.user.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtil {
	private final String accessSecretKey;
	private static final String CLAIM_NAME = "name";

	public static final long ACCESS_EXPIRATION_TIME = 86400000;

	public JwtUtil(@Value("${spring.jwt.secret-key}") String accessSecretKey) {
		this.accessSecretKey = accessSecretKey;
	}

	// 유저 식별자 id와 이름을 포함한 JWT 토큰 생성
	public String generateAccessToken(User loginUser) {
		return Jwts.builder()
			.setSubject(String.valueOf(loginUser.getId()))
			.claim(CLAIM_NAME, loginUser.getName())
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME))
			.signWith(Keys.hmacShaKeyFor(accessSecretKey.getBytes(UTF_8)), SignatureAlgorithm.HS256)
			.compact();
	}

	public Claims validateToken(String accessToken) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(accessSecretKey.getBytes(UTF_8)))
				.build()
				.parseClaimsJws(accessToken)
				.getBody();
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			throw new JwtValidationException(INVALID_JWT_SIGNATURE);
		} catch (ExpiredJwtException e) {
			throw new JwtValidationException(EXPIRED_JWT_TOKEN);
		} catch (UnsupportedJwtException e) {
			throw new JwtValidationException(UNSUPPORTED_JWT_TOKEN);
		} catch (IllegalArgumentException e) {
			throw new JwtValidationException(INVALID_JWT_TOKEN);
		}
	}
}
