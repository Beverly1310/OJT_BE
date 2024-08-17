package com.example.ojt.controller;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.AddAddressCompanyRequest;
import com.example.ojt.model.dto.request.AddressCompanyRequest;
import com.example.ojt.model.dto.response.APIResponse;
import com.example.ojt.model.dto.response.AddressCompanyResponse;
import com.example.ojt.model.entity.AddressCompany;
import com.example.ojt.service.address.IAddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/company/address-company")
public class AddressController {

    @Autowired
    private IAddressService addressService;
    @GetMapping("/current-company")
    public ResponseEntity<List<AddressCompanyResponse>> getAddressCompaniesByCurrentCompany() throws CustomException {
        List<AddressCompanyResponse> addressCompanies = addressService.findAllByCurrentCompany();
        return new ResponseEntity<>(addressCompanies, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Page<AddressCompanyResponse>> findAll(
            @PageableDefault(page = 0, size = 3, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
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
    @GetMapping("/{addressId}")
    public ResponseEntity<?> findById (@PathVariable Integer addressId ) throws CustomException {
        return ResponseEntity.ok().body(addressService.findById(addressId));
    }

    @PostMapping
    public ResponseEntity<?> addAddressCompany(@Valid @RequestBody AddAddressCompanyRequest addressCompanyRequest) throws CustomException {
        boolean check = addressService.addAddressCompany(addressCompanyRequest);
        if (check) {
            APIResponse response = new APIResponse(200, "add Address Company successful");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new CustomException("Lack of compulsory registration information or invalid information.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateAddressCompany( @RequestBody AddressCompanyRequest addressCompanyRequest) throws CustomException {
        boolean check = addressService.updateAddressCompany(addressCompanyRequest);
        if (check) {
            APIResponse response = new APIResponse(200, "update  Address  success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new CustomException("Lack of compulsory registration information or invalid information.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    @PutMapping ("{id}")
    public ResponseEntity<?> deleteAddressCompany(@PathVariable Integer id) throws CustomException {
        boolean check = addressService.deleteByIdAddressCompany(id);
        if (check) {
            APIResponse apiResponse = new APIResponse(200, "Delete Address success");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            throw new CustomException("Lack of compulsory registration information or invalid information.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
