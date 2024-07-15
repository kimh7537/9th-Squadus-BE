package com.cotato.squadus.common.config.auth;

import com.cotato.squadus.api.auth.dto.LoginRequest;
import com.cotato.squadus.api.auth.dto.OAuth2Attribute;
import com.cotato.squadus.domain.auth.entity.Member;
import com.cotato.squadus.domain.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //naver, google
        OAuth2Attribute attribute = OAuth2Attribute.of(registrationId, oAuth2User.getAttributes());

        String uniqueId = registrationId+" "+attribute.getProviderId();

        Member member = saveOrUpdate(attribute, uniqueId);
        LoginRequest loginRequest = new LoginRequest(member);

        return new CustomOAuth2Member(loginRequest);
    }

    private Member saveOrUpdate(OAuth2Attribute attribute, String uniqueId){
        Member user = memberRepository.findByUniqueId(uniqueId)
                .map(entity -> entity.update(attribute.getEmail(), attribute.getUsername()))
                .orElse(attribute.toEntity(uniqueId));
        return memberRepository.save(user);
    }
}

