package com.example.ojt.model.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class JWTResponse {
    private final String type = "Bearer";
    private String accessToken ;
    private String email;
    private String roleName;
    private Integer status;
}

