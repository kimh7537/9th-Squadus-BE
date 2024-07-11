//package com.cotato.squadus.controller;
//
//import com.cotato.squadus.api.auth.dto.LoginRequest;
//import com.cotato.squadus.api.auth.dto.LoginResponse;
//import com.cotato.squadus.domain.auth.entity.Member;
//import com.cotato.squadus.common.config.jwt.RefreshEntity;
//import com.cotato.squadus.common.config.jwt.JWTUtil;
//import com.cotato.squadus.domain.club.service.MemberRepository;
//import com.cotato.squadus.domain.club.service.RefreshRepository;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Date;
//
//@RestController
//@RequiredArgsConstructor
//public class MemberController {
//
//    private final JWTUtil jwtUtil;
//    private final MemberRepository memberRepository;
//    private final RefreshRepository refreshRepository;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
//        Member member = memberRepository.findByUsername(loginRequest.getUsername());
//
//        if (member == null || !member.getEmail().equals(loginRequest.getEmail())) {
//            return ResponseEntity.status(401).build();
//        }
//
//        String username = member.getUsername();
//        String role = member.getRole();
//        System.out.println("role = " + role);
//
//        //토큰 생성
//        String access = jwtUtil.createJwt("access", username, role, 600000L);
//        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);
//
//        addRefreshEntity(username, refresh, 86400000L);
//
//        //응답 설정
//        response.setHeader("access", access);
//        response.setHeader("refresh", refresh);
//        response.setStatus(HttpStatus.OK.value());
////        LoginResponse loginResponse = new LoginResponse(access, refresh);
//        return ResponseEntity.ok("Login Success");
//    }
//
//    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
//
//        Date date = new Date(System.currentTimeMillis() + expiredMs);
//
//        RefreshEntity refreshEntity = new RefreshEntity();
//        refreshEntity.setUsername(username);
//        refreshEntity.setRefresh(refresh);
//        refreshEntity.setExpiration(date.toString());
//
//        refreshRepository.save(refreshEntity);
//    }
//}
