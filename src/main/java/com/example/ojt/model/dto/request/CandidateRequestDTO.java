package com.example.ojt.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CandidateRequestDTO {
    private Integer id;

    @NotEmpty(message = "Please fill Name!")
    private String name;

    @NotNull(message = "Please fill Birthday!")
    private String birthday;

    @NotEmpty(message = "Please fill Address!")
    private String address;

    @NotEmpty(message = "Please fill phone!")
    @Pattern(regexp = "(0[3|5|7|8|9])+([0-9]{8})\\b", message = "Enter the Vietnamese phone")
    private String phone;

    @NotEmpty(message = "Please fill Gender!")
    private boolean gender;

    @NotEmpty(message = "Please fill LinkLinkedin!")
    private String linkLinkedin;

    @NotEmpty(message = "Please fill LinkGit!")
    private String linkGit;

    @NotEmpty(message = "Please fill Position!")
    private String position;
    private MultipartFile avatar;

    @NotEmpty(message = "Please fill AboutMe!")
    private String aboutme;

    @NotNull(message = "Account is mandatory!")
    private Integer status ;

    private Date createAt ;
}
