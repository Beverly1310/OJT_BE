package com.example.ojt.model.dto.responsewapper;

import com.example.ojt.model.dto.response.APIResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataResponse<T> {
    private APIResponse apiResponse;
    private T Data;
}
