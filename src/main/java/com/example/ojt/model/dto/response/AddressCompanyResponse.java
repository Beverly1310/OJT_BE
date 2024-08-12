package com.example.ojt.model.dto.response;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressCompanyResponse {
    private Integer id;
    private String address;
    private String mapUrl;
    private String companyName;
    private String cityName;
    private Date createdAt;
    private int status;
}