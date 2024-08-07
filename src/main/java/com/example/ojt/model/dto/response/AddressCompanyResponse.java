package com.example.ojt.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
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