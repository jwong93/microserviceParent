package com.giftgracious.authserver.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {

    private String userId;
    private String accessToken;
    private String refreshToken;
    private String address;
}
