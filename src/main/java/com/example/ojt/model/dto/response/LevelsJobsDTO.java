package com.example.ojt.model.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LevelsJobsDTO {
    private Long id;
    private LevelJobDTO levelJob;
}
