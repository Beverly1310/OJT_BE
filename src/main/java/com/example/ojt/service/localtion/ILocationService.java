package com.example.ojt.service.localtion;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.LocationRequest;
import com.example.ojt.model.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ILocationService {
    Page<Location> findAll(Pageable pageable, String search);
    void addLocation(LocationRequest locationRequest) throws CustomException;
    void updateLocation(LocationRequest locationRequest, Integer updateId) throws CustomException;
    void deleteByIdLocation(Integer deleteId) throws CustomException;
    Location findById(Integer findId) throws CustomException;

}
