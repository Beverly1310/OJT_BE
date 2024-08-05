package com.example.ojt.model.dto.mapper;

import lombok.*;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PageDataDTO<T> {
    private int totalPage;
    private int currentPage;
    private String searchName;
    private Long totalElement;
    private int limit;
    private String sort;
    private List<T> content;
}
