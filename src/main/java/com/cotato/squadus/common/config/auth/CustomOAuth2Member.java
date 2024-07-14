package com.cotato.squadus.common.config.auth;

import com.cotato.squadus.api.auth.dto.LoginRequest;
import com.cotato.squadus.domain.auth.enums.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2Member implements OAuth2User {

    private final LoginRequest loginRequest;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return loginRequest.getMemberRole();
            }
        });
        return collection;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    public String getUniqueId(){
        return loginRequest.getUniqueId();
    }

    @Override
    public String getName() {
        return loginRequest.getUsername();
    }
}
