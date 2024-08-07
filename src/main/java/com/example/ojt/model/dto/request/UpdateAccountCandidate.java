package com.example.ojt.model.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateAccountCandidate {
    private String aboutMe;
    private String address;
    private MultipartFile avatar;
    private Date birthDay;
    private Boolean gender;
    private String linkGit;
    private String linkLinkedin;
    private String name;
    @Pattern(regexp = "^(\\+84|0)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5]|9[0-9])[0-9]{7}$" )
    private String phone;
    private String position;
}
