package com.example.ojt.model.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CandidatePerMonth {
    private String month;
    private Integer number;

}
