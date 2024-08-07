package com.example.ojt.model.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse {
    private int statusCode;
    private String message;
    private Object data;

}
