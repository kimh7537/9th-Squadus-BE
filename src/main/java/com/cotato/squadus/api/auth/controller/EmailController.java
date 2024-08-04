package com.cotato.squadus.api.auth.controller;

import com.cotato.squadus.api.auth.dto.EmailRequestDto;
import com.cotato.squadus.api.auth.dto.EmailResponseDto;
import com.cotato.squadus.domain.auth.service.EmailSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "학교 인증 이메일", description = "학교 이메일 인증 관련 API")
@RestController
@RequestMapping("/v1/api/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailSendService emailSendService;


    @GetMapping("/test")
    public String helloTest(){
        return "Hello World";
    }


    /* Send Email: 인증번호 전송 버튼 click */
    @PostMapping("/signup")
    @Operation(summary = "이메일 인증 요청", description = "이메일 주소를 통해 학교 이메일 인증을 요청합니다")
    public Map<String, String> mailSend(@RequestBody @Valid EmailRequestDto emailRequestDto) {
        String code = emailSendService.joinEmail(emailRequestDto.getEmail());
        // response를 JSON 문자열으로 반환
        Map<String, String> response = new HashMap<>();
        response.put("code", code);

        return response;
    }

    /* Email Auth: 인증번호 입력 후 인증 버튼 click */
    @PostMapping("/signup/emailAuth")
    @Operation(summary = "이메일 인증 인증번호 확인", description = "이메일 주소로 받은 인증 번호와 이메일 주소를 통해 학교인증을 완료합니다")
    public String authCheck(@RequestBody @Valid EmailResponseDto emailResponseDto) {
        Boolean checked = emailSendService.checkAuthNum(emailResponseDto.getEmail(), emailResponseDto.getAuthNum());
        if (checked) {
            return "이메일 인증 성공!";
        }
        else {
            throw new NullPointerException("이메일 인증 실패!");
        }
    }

}

