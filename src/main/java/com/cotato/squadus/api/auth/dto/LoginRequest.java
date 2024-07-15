package com.cotato.squadus.api.auth.dto;

import com.cotato.squadus.domain.auth.entity.Member;
import com.cotato.squadus.domain.auth.enums.MemberRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private String uniqueId;
    private String username;
    private String email;
    private String memberRole;

    public LoginRequest(Member member) {
        this.uniqueId = member.getUniqueId();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.memberRole = member.getMemberRole().name();
    }

    @Builder
    public LoginRequest(String uniqueId, String username, String memberRole) {
        this.uniqueId = uniqueId;
        this.username = username;
        this.memberRole = memberRole;
    }


}
