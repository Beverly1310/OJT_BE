package com.example.ojt.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobRequest {
    private Integer id;
    private String title;
    private String description;
    private String requirements;
    private String salary;
    private String expireAt;
    private List<Integer> levelJobIds;
    private List<Integer> typeJobIds;
}
