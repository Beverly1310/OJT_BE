package com.example.ojt.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StatRes {
    private Integer liveJob;
    private Integer companies;
    private Integer candidates;
    private Integer newJobs;
}
