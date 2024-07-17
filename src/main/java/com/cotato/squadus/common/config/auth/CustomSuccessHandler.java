package com.cotato.squadus.common.config.auth;

import com.cotato.squadus.common.config.jwt.JWTUtil;
import com.cotato.squadus.common.config.jwt.RefreshEntity;
import com.cotato.squadus.common.config.jwt.RefreshRepository;
import com.cotato.squadus.domain.auth.enums.MemberRole;
import com.cotato.squadus.domain.auth.service.RefreshService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final RefreshService refreshService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2Member customOAuth2Member = (CustomOAuth2Member) authentication.getPrincipal();

        String uniqueId = customOAuth2Member.getUniqueId();
        String username = customOAuth2Member.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt("access", uniqueId, username, role, 600000L); //10분
        String refresh = jwtUtil.createJwt("refresh", uniqueId, username, role, 86400000L); //24시간

        refreshService.addRefreshEntity(uniqueId, username, refresh, 86400000L);

        log.info("access: {} refresh: {}", access, refresh);

        response.setHeader("access", access);
        response.addCookie(refreshService.createCookie("refresh", refresh));
        response.sendRedirect("http://localhost:3000");
    }

}