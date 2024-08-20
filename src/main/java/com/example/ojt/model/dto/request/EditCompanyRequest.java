package com.example.ojt.model.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditCompanyRequest {

    private String name;

    private MultipartFile logo;
    @URL(regexp = "^(http|ftp).*")
    private String website;

    private String linkFacebook;
    private String linkLinkedin;
    @Min(value = 1 ,message = "lơn hơn 0")
    private Integer size;

    private String description;
    @Pattern(regexp = "(0[3|5|7|8|9])+([0-9]{8})\\b", message = "Enter the Vietnamese phone")
    private String phone;

    private String policy;

    private Integer typeCompany;
    private String address;
    private String mapUrl;
    private Integer locationId;
}