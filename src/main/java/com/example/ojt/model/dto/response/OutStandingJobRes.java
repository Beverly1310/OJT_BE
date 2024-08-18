package com.example.ojt.model.dto.response;

import com.example.ojt.model.entity.Job;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OutStandingJobRes {
    private Job job;
    private String typeJob;
}
