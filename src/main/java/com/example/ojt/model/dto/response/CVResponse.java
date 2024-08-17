package com.example.ojt.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CVResponse {
    private Integer id;
    private String fileName;
    private String url;
    private boolean status;
    private Date createdAt;
}
