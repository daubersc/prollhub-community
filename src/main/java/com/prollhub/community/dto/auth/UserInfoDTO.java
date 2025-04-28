package com.prollhub.community.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.prollhub.community.persistency.model.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
@Setter
public class UserInfoDTO {

    private UUID id;
    private String username;
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String did;

    public UserInfoDTO(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.email = account.getEmail();
        this.did = account.getDid();
    }

}
