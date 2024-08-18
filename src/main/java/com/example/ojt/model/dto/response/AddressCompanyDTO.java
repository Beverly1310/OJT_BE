package com.example.ojt.model.dto.response;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressCompanyDTO {
    private Integer id;
    private String address;
    private String mapUrl;
    private Date createdAt;
    private int status;
    private CompanyDTO company; // Include CompanyDTO if needed
    private LocationDTO location; // Include LocationDTO if needed
}