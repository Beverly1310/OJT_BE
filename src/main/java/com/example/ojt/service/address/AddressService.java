package com.example.ojt.service.address;
import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.AddAddressCompanyRequest;
import com.example.ojt.model.dto.request.AddressCompanyRequest;
import com.example.ojt.model.dto.response.AddressCompanyResponse;
import com.example.ojt.model.entity.AddressCompany;
import com.example.ojt.model.entity.Company;
import com.example.ojt.model.entity.Location;
import com.example.ojt.repository.IAddressCompanyRepository;
import com.example.ojt.repository.ICompanyRepository;
import com.example.ojt.repository.ILocationRepository;
import com.example.ojt.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressService implements IAddressService {

    @Autowired
    private IAddressCompanyRepository addressCompanyRepository;

    @Autowired
    private ICompanyRepository companyRepository;

    @Autowired
    private ILocationRepository locationRepository;


    private  Company getCurrentCompany() throws CustomException {
        Company company = companyRepository.findByAccountId(AccountService.getCurrentUser().getId()).orElseThrow(() -> new CustomException("Company not found" , HttpStatus.NOT_FOUND));
        return company;
    }

    @Override
    public Page<AddressCompanyResponse> findAll(Pageable pageable, String search) {
        Page<AddressCompany> addressCompanies;
        if (search.isEmpty()) {
            addressCompanies = addressCompanyRepository.findAll(pageable);
        } else {
            addressCompanies = addressCompanyRepository.findAllByAddressContains(search, pageable);
        }
        return addressCompanies.map(this::convertToResponse);
    }

    @Override
    public boolean addAddressCompany(AddAddressCompanyRequest addressCompanyRequest) throws CustomException {
        AddressCompany addressCompany = AddressCompany.builder()
                .address(addressCompanyRequest.getAddress())
                .mapUrl(addressCompanyRequest.getMapUrl())
                .location(locationRepository.findById(addressCompanyRequest.getLocation()).orElseThrow(() -> new CustomException("Local not found" , HttpStatus.NOT_FOUND)))
                .company(getCurrentCompany())
                .createdAt(new Date())
                .status(1)
                .build();
        addressCompanyRepository.save(addressCompany);
        return true;
    }



    @Override
    public boolean updateAddressCompany(AddressCompanyRequest addressCompanyRequest) throws CustomException {
        AddressCompany existingAddressCompany = addressCompanyRepository.findById(addressCompanyRequest.getId())
                .orElseThrow(() -> new CustomException("AddressCompany not found", HttpStatus.NOT_FOUND));

        AddressCompany addressCompanyCheck = addressCompanyRepository.findByAddress(addressCompanyRequest.getAddress()).orElse(null);
        if (addressCompanyCheck != null && !addressCompanyCheck.getId().equals(addressCompanyRequest.getId())) {
            throw new CustomException("Address already exists", HttpStatus.BAD_REQUEST);
        }
        if (addressCompanyRequest.getAddress()!=null){
            existingAddressCompany.setAddress(addressCompanyRequest.getAddress());
        }

        if (addressCompanyRequest.getMapUrl() !=null) {

        existingAddressCompany.setMapUrl(addressCompanyRequest.getMapUrl());
        }
        if (addressCompanyRequest.getLocation() != null) {

        existingAddressCompany.setLocation(getLocation(addressCompanyRequest.getLocation()));
        }
        existingAddressCompany.setCreatedAt(new Date());

        addressCompanyRepository.save(existingAddressCompany);
        return true;
    }



    @Override
    public boolean deleteByIdAddressCompany(Integer id) throws CustomException {
        Company currentCompany = getCurrentCompany();
        AddressCompany addressCompany = addressCompanyRepository.findById(id)
                .orElseThrow(() -> new CustomException("AddressCompany not found", HttpStatus.NOT_FOUND));

        if (!addressCompany.getCompany().getId().equals(currentCompany.getId())) {
            throw new CustomException("Unauthorized action", HttpStatus.UNAUTHORIZED);
        }

        addressCompany.setStatus(2);
        addressCompanyRepository.save(addressCompany);
        return true;
    }
    @Override
    public List<AddressCompanyResponse> findAllByCurrentCompany() throws CustomException {
        Company company = getCurrentCompany();
        List<AddressCompany> addressCompanies = addressCompanyRepository.findAllByCompany(company);

        // Sử dụng Stream API để lọc các địa chỉ trùng lặp theo locationId
        List<AddressCompanyResponse> uniqueLocations = addressCompanies.stream()
                .collect(Collectors.groupingBy(addressCompany -> addressCompany.getLocation().getId()))
                .values().stream()
                .map(locationList -> locationList.get(0)) // Lấy địa chỉ đầu tiên của mỗi nhóm locationId
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return uniqueLocations;
    }



    @Override
    public AddressCompany findById(Integer findId) throws CustomException {
        return addressCompanyRepository.findById(findId)
                .orElseThrow(() -> new CustomException("AddressCompany not found", HttpStatus.NOT_FOUND));
    }



    private Location getLocation(Integer locationId) throws CustomException {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new CustomException("Location not found", HttpStatus.NOT_FOUND));
    }

    private AddressCompanyResponse convertToResponse(AddressCompany addressCompany) {
        return AddressCompanyResponse.builder()
                .id(addressCompany.getId())
                .address(addressCompany.getAddress())
                .mapUrl(addressCompany.getMapUrl())
                .companyName(addressCompany.getCompany() != null ? addressCompany.getCompany().getName() : null)
                .cityName(addressCompany.getLocation() != null ? addressCompany.getLocation().getNameCity() : null)
                .createdAt(addressCompany.getCreatedAt())
                .status(addressCompany.getStatus())
                .build();
    }
}
