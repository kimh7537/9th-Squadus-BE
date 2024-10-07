package com.cotato.squadus.common.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cotato.squadus.api.auth.dto.LoginRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2Member implements OAuth2User {

	private final LoginRequest loginRequest;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();

		collection.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return loginRequest.getMemberRole().name();
			}
		});
		return collection;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	public String getUniqueId() {
		return loginRequest.getUniqueId();
	}

	@Override
	public String getName() {
		return loginRequest.getUsername();
	}
}
