package com.example.ojt.service.localtion;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.LocationRequest;
import com.example.ojt.model.entity.Location;
import com.example.ojt.repository.ILocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService implements ILocationService {
    private final ILocationRepository locationRepository;
    @Override
    public Page<Location> findAll(Pageable pageable, String search) {
        Page<Location> locations;
        if (search.isEmpty()) {
            locations =locationRepository.findAll(pageable);
        }else {
            locations = locationRepository.findAllByNameCityContains(search,pageable);
        }
        return locations;
    }

    @Override
    public void addLocation(LocationRequest locationRequest) throws CustomException {
        Location location = Location.builder()
                .nameCity(locationRequest.getNameCity())
                .build();
        locationRepository.save(location);
    }

    @Override
    public void updateLocation(LocationRequest locationRequest, Integer updateId) throws CustomException {
        Location newLocation = findById(updateId);
        newLocation.setNameCity(locationRequest.getNameCity());
        locationRepository.save(newLocation);
    }

    @Override
    public void deleteByIdLocation(Integer deleteId) throws CustomException {
        if (locationRepository.existsById(deleteId)){
            locationRepository.deleteById(deleteId);
        }else {
            throw new CustomException("Local not found" , HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Location findById(Integer findId) throws CustomException {
        return locationRepository.findById(findId).orElseThrow(() -> new CustomException("Localtion not found", HttpStatus.NOT_FOUND));
    }
}
