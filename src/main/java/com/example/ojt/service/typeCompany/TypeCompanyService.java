package com.example.ojt.service.typeCompany;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.TypeCompanyRequest;
import com.example.ojt.model.dto.response.SuccessResponse;
import com.example.ojt.model.entity.Location;
import com.example.ojt.model.entity.TypeCompany;
import com.example.ojt.repository.ITypeCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TypeCompanyService implements ITypeCompanyService{
    @Autowired
    private ITypeCompanyRepository typeCompanyRepository;
    @Override
    public Page<TypeCompany> findAll(Pageable pageable, String search) {
        Page<TypeCompany> typeCompanies;
        if (search.isEmpty()){
            typeCompanies = typeCompanyRepository.findAll(pageable);
        }else {
            typeCompanies = typeCompanyRepository.findAllByNameContains(search,pageable);
        }
        return typeCompanies;
    }

    @Override
    public boolean addTypeCompany(TypeCompanyRequest typeCompanyRequest) throws CustomException {
        try {
            TypeCompany typeCompany = new TypeCompany();
            typeCompany.setName(typeCompanyRequest.getName());
            typeCompanyRepository.save(typeCompany);
            return true;
        } catch (Exception e) {
            throw new CustomException("Error adding TypeCompany", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean updateTypeCompany(TypeCompanyRequest typeCompanyRequest, Integer updateId) throws CustomException {
        try {
            Optional<TypeCompany> existingCompany = typeCompanyRepository.findById(typeCompanyRequest.getId());
            if (existingCompany.isPresent()) {
                TypeCompany typeCompany = existingCompany.get();
                typeCompany.setName(typeCompanyRequest.getName());
                typeCompanyRepository.save(typeCompany);
                return true;
            } else {
                throw new CustomException("TypeCompany not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new CustomException("Error updating TypeCompany", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public boolean deleteByIdTypeCompany(Integer deleteId) throws CustomException {
        try {
            if (typeCompanyRepository.existsById(deleteId)) {
                typeCompanyRepository.deleteById(deleteId);
                return true;
            } else {
                throw new CustomException("TypeCompany not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new CustomException("Error deleting TypeCompany", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public SuccessResponse findById(Integer findId) throws CustomException {
        try {
            TypeCompany typeCompany = typeCompanyRepository.findById(findId)
                    .orElseThrow(() -> new CustomException("TypeCompany not found", HttpStatus.NOT_FOUND));


            return SuccessResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Get type company success")
                    .data(typeCompany)
                    .build();

        } catch (Exception e) {
            throw new CustomException("Error finding TypeCompany", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
