package com.example.ojt.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateBasicInfoResponse {
    String name;
    String about;
    Integer age;
    String address;
    String phone;
    Boolean gender;
    List<ExperienceCVResponse> experience;
    List<String> skills;
    String linkLinkedin;
    String linkGit;
    String position;
    String avatar;

}
