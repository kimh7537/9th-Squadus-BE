package com.cotato.squadus.api.auth.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.squadus.common.config.jwt.JWTUtil;
import com.cotato.squadus.common.config.jwt.RefreshRepository;
import com.cotato.squadus.domain.auth.service.RefreshService;

import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "토큰 재발급", description = "Access Token 재발급 관련 API")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ReissueController {

	private final JWTUtil jwtUtil;
	private final RefreshRepository refreshRepository;
	private final RefreshService refreshService;

	@PostMapping("/reissue")
	@Operation(summary = "Access token 재발급", description = "Refresh token을 바탕으로 Access token을 재발급합니다")
	public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

		// 헤더에서 리프레시 토큰 가져오기
		String refresh = request.getHeader("refresh");
		if (refresh == null) {
			return new ResponseEntity<>("refresh token is missing", HttpStatus.BAD_REQUEST);
		}

		// 만료 여부 확인
		try {
			jwtUtil.isExpired(refresh);
		} catch (ExpiredJwtException e) {
			return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
		}

		// 리프레시 토큰인지 확인
		String category = jwtUtil.getCategory(refresh);
		if (!category.equals("refresh")) {
			return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
		}

		// DB에 저장된 토큰인지 확인
		Boolean isExist = refreshRepository.existsByRefresh(refresh);
		if (!isExist) {
			return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
		}

		Map<String, String> map = refreshService.reissueRefreshToken(refresh);

		// 새로운 Access Token과 Refresh Token 생성
		String newAccess = map.get("access");
		String newRefresh = map.get("refresh");

		// 헤더에 토큰 추가
		response.setHeader("access", newAccess);
		response.setHeader("refresh", newRefresh);

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
