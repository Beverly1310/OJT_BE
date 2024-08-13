package com.example.ojt.model.dto.request;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateProjectReq {
    private Integer id;
    private String name;
    private String info;
    private String link;
    private Date startAt;
    private Date endAt;
}
