package com.example.ojt.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class EducationCVResponse {
    private String nameEducation;
    private String major;
    private Date startAt;
    private Date endAt;
    private String info;
}
