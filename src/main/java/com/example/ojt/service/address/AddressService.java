package com.example.ojt.service.address;
import com.example.ojt.model.dto.response.AddressCompanyResponse;
import com.example.ojt.model.entity.AddressCompany;
import com.example.ojt.repository.IAddressCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Service
public class AddressService implements IAddressService {

    @Autowired
    private IAddressCompanyRepository addressCompanyRepository;

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
