package com.example.ojt.controller;

import com.example.ojt.model.dto.response.AddressCompanyResponse;
import com.example.ojt.model.entity.AddressCompany;
import com.example.ojt.service.address.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/company/address-company")
public class AddressController {

    @Autowired
    private IAddressService addressService;

    @GetMapping
    public ResponseEntity<Page<AddressCompanyResponse>> findAll(
            @PageableDefault(page = 0, size = 4, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.fromString(direction), sort)
        );
        return ResponseEntity.ok().body(addressService.findAll(sortedPageable, search));
    }
}
