package com.example.ojt.model.dto.response;

import lombok.*;

import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CompanyResponse {
    private Integer id;
    private String name;
    private String logo;
    private String website;
    private String linkFacebook;
    private String linkLinkedin;
    private Integer followers;
    private Integer size;
    private String description;
    private String phone;
    private String emailCompany;
    private String policy;
    private Date createdAt;
    private Date updatedAt;
    private String typeCompanyName;
    private String nameCity; 
    private String address;  
    private String mapUrl;   
}
