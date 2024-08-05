package com.example.ojt.model.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PageToPaginationDTO<T>{
    private List<T> content;
    private int totalPages;
    private long totalElement;
    private int currentPage;
    private int pageSize;

}
