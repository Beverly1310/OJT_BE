package com.example.ojt.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CertificateCVResponse {
    String name;
    String organization;
    Date startedAt;
    Date endAt;
    String info;

}
