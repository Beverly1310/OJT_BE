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
    private String name;
    private String email;
    private String avatar;
    private String roleName;
    private Integer status;
}

