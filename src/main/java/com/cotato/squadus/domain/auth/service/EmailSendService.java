package com.cotato.squadus.domain.auth.service;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.squadus.common.config.RedisConfig;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.domain.auth.entity.Member;
import com.cotato.squadus.domain.auth.enums.MemberRole;
import com.cotato.squadus.domain.auth.enums.SchoolDomain;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailSendService {

	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private RedisConfig redisConfig;
	private int authNumber;

	/* 이메일 인증에 필요한 정보 */
	@Value("${spring.mail.username}")
	private String serviceName;
	@Autowired
	private MemberService memberService;

	/* 랜덤 인증번호 생성 */
	public void makeRandomNum() {
		Random r = new Random();
		String randomNumber = "";
		for (int i = 0; i < 6; i++) {
			randomNumber += Integer.toString(r.nextInt(10));
		}

		authNumber = Integer.parseInt(randomNumber);
	}

	/* 이메일 전송 */
	public void mailSend(String setFrom, String toMail, String title, String content) {
		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setFrom(setFrom); // service name
			helper.setTo(toMail); // customer email
			helper.setSubject(title); // email title
			helper.setText(content, true); // content, html: true
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace(); // 에러 출력
		}
		// redis에 3분 동안 이메일과 인증 코드 저장
		ValueOperations<String, String> valOperations = redisConfig.redisTemplate().opsForValue();
		valOperations.set(toMail, Integer.toString(authNumber), 180, TimeUnit.SECONDS);
	}

	/* 이메일 작성 */
	public String joinEmail(String email) {
		makeRandomNum();
		String customerMail = email;
		String title = "학교 인증을 위한 이메일입니다";
		String content =
			"<b>안녕하세요!</b> \uD83D\uDC4B" +
				"<br><br>" +
				"이메일 인증을 완료하기 위해 아래 절차를 진행해 주세요:" +
				"<br><br>" +
				"<b>1. 인증 번호 입력</b> \uD83D\uDCDD" +
				"<br>" +
				"인증 번호 : " + "<b>" + authNumber + "</b>" +
				"<br><br>" +
				"2. 학교 인증 칸에 해당 번호를 입력해 주세요. \uD83D\uDD11" +
				"<br><br>" +
				"인증 절차가 완료되면, 추가적인 안내를 드리겠습니다." +
				"<br><br>" +
				"감사합니다! \uD83D\uDE0A";
		//                "이메일을 인증하기 위한 절차입니다." +
		//                        "<br><br>" +
		//                        "인증 번호는 " + authNumber + "입니다." +
		//                        "<br>" +
		//                        "학교 인증 칸에 해당 번호를 입력해주세요.";
		mailSend(serviceName, customerMail, title, content);
		return Integer.toString(authNumber);
	}

	@Transactional
	/* 인증번호 확인 및 이메일 도메인 검증 */
	public Boolean checkAuthNum(String email, String authNum,
		@AuthenticationPrincipal CustomOAuth2Member customOauth2Member) {
		// 이메일 도메인이 유효한지 확인
		if (!isValidSchoolEmail(email)) {
			throw new IllegalArgumentException("유효하지 않은 학교 이메일 도메인입니다.");
		}

		// Redis에서 인증번호 확인
		ValueOperations<String, String> valOperations = redisConfig.redisTemplate().opsForValue();
		String code = valOperations.get(email);

		boolean isAuthSuccessful = Objects.equals(code, authNum);

		if (isAuthSuccessful) {
			String domain = email.substring(email.indexOf("@") + 1);
			String universityName = SchoolDomain.getUniversityByDomain(domain);

			// 회원의 university 필드를 업데이트
			Member memberByUniqueId = memberService.findMemberByUniqueId(customOauth2Member.getUniqueId());
			memberByUniqueId.updateUniversity(universityName);
			memberByUniqueId.updateMemberRole(MemberRole.CERTIFIED_MEMBER);
			memberService.saveMember(memberByUniqueId);
			log.info("사용자 {}의 university 필드가 {}로 업데이트 되었습니다.", memberByUniqueId.getUsername(), universityName);
			log.info("사용자 {}의 memberRole 필드가 {}로 업데이트 되었습니다.", memberByUniqueId.getUsername(),
				MemberRole.CERTIFIED_MEMBER);
		}

		return isAuthSuccessful;
	}

	private boolean isValidSchoolEmail(String email) {
		if (email == null || !email.contains("@")) {
			return false;
		}

		// 이메일 주소에서 도메인 부분 추출
		String domain = email.substring(email.indexOf("@") + 1);

		// 도메인에 해당하는 학교 이름이 있는지 확인
		for (SchoolDomain schoolDomain : SchoolDomain.values()) {
			if (domain.endsWith(schoolDomain.getDomain())) {
				log.info("이메일 인증 학교 : {}", schoolDomain.getUniversityName());
				return true;
			}
		}

		log.info("이메일 인증 실패: 도메인이 일치하지 않습니다.");
		return false;
	}

}


