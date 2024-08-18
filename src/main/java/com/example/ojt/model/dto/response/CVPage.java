package com.example.ojt.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CVPage
{
    String name;
    String about;
    List<ExperienceCVResponse> experience;
    Integer age;
    String address;
    List<SkillCVResponse> skills;
    List<CertificateCVResponse> certificates;
    List<ProjectCVResponse> projects;
    List<EducationCVResponse> educations;
    String phone;
    Boolean gender;
    String linkLinkedin;
    String linkGit;
    String avatar;

}
