package com.example.ojt.model.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {
    private Integer id;
    private String name;
    private String logo;
    private String website;
    private String linkFacebook;
    private String linkLinkedin;
    private Integer followers;
    private Integer size;
    private Integer outstanding;
    private String description;
    private String phone;
    private String emailCompany;
    private String policy;
}
