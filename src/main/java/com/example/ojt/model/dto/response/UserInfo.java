package com.example.ojt.model.dto.response;

import com.example.ojt.model.entity.*;
import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserInfo {
    private Candidate candidate;
    private List<EducationCandidate> education;
    private List<ExperienceCandidate> experience;
    private List<ProjectCandidate> project;
    private List<CertificateCandidate> certificate;
    private List<SkillsCandidate> skillsCandidates;
}
