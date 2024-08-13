package com.example.ojt.model.dto.response;

import com.example.ojt.model.entity.Candidate;
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
public class CandidateResponseDTO {
    private Integer id;
    private String name;
    private String birthday;
    private String address;
    private String phone;
    private String gender;
    private String linkLinkedin;
    private String linkGit;
    private String position;
    private String avatar;
    private String aboutme;
    private Integer status;
    private Date createAt = new Date();
    private String account;
    public CandidateResponseDTO (Candidate candidate) {
        this.id = candidate.getId();
        this.name = candidate.getName();
        this.birthday = candidate.getBirthday();
        this.address = candidate.getAddress();
        this.phone = candidate.getPhone();
        this.gender = candidate.getGender();
        this.linkLinkedin = candidate.getLinkLinkedin();
        this.linkGit = candidate.getLinkGit();
        this.position = candidate.getPosition();
        this.avatar = candidate.getAvatar();
        this.aboutme = candidate.getAboutme();
        this.status = candidate.getStatus();
        this.createAt = candidate.getCreatedAt();
        this.account = candidate.getAccount().getEmail();

    }
}
