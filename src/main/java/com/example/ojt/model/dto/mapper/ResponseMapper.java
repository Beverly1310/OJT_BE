package com.example.ojt.model.dto.mapper;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseMapper<T> {
    private HttpResponse httpResponse;
    private int code;
    private String message;
    private T data;
}
