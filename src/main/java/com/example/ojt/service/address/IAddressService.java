package com.example.ojt.service.address;

import com.example.ojt.model.dto.response.AddressCompanyResponse;
import com.example.ojt.model.entity.AddressCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAddressService {
    Page<AddressCompanyResponse> findAll(Pageable pageable, String search);
}

