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
        // Kiểm tra xem tên đã tồn tại hay chưa
        Optional<TypeCompany> existingCompany = typeCompanyRepository.findByName(typeCompanyRequest.getName());
        if (existingCompany.isPresent()) {
            throw new CustomException("TypeCompany name already exists", HttpStatus.CONFLICT);
        }

        TypeCompany typeCompany = new TypeCompany();
        typeCompany.setName(typeCompanyRequest.getName());
        typeCompanyRepository.save(typeCompany);
        return true;
    }


    @Override
    public boolean updateTypeCompany(TypeCompanyRequest typeCompanyRequest) throws CustomException {
        Optional<TypeCompany> existingCompany = typeCompanyRepository.findById(typeCompanyRequest.getId());
        if (existingCompany.isPresent()) {
            TypeCompany typeCompany = existingCompany.get();

            // Kiểm tra xem tên mới đã tồn tại hay chưa và không phải của TypeCompany hiện tại
            Optional<TypeCompany> duplicateCompany = typeCompanyRepository.findByName(typeCompanyRequest.getName());
            if (duplicateCompany.isPresent() && !duplicateCompany.get().getId().equals(typeCompany.getId())) {
                throw new CustomException("TypeCompany name already exists", HttpStatus.CONFLICT);
            }

            typeCompany.setName(typeCompanyRequest.getName());
            typeCompanyRepository.save(typeCompany);
            return true;
        } else {
            throw new CustomException("TypeCompany not found", HttpStatus.NOT_FOUND);
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
