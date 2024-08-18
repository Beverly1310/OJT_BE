package com.example.ojt.service.address;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.AddAddressCompanyRequest;
import com.example.ojt.model.dto.request.AddressCompanyRequest;
import com.example.ojt.model.dto.response.AddressCompanyResponse;
import com.example.ojt.model.entity.AddressCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAddressService {
    Page<AddressCompanyResponse> findAll(Pageable pageable, String search);
    boolean addAddressCompany(AddAddressCompanyRequest addressCompanyRequest) throws CustomException;
    boolean updateAddressCompany(AddressCompanyRequest addressCompanyRequest) throws CustomException;
    boolean deleteByIdAddressCompany(Integer deleteId) throws CustomException;
    AddressCompany findById(Integer findId) throws CustomException;
    List<AddressCompanyResponse> findAllByCurrentCompany() throws CustomException;
}

