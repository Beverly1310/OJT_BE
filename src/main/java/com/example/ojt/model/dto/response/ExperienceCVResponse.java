package com.example.ojt.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperienceCVResponse {
    String position;
    String company;
    Date startedAt;
    Date endAt;
    String info;
}
