package com.example.ojt.controller;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.LocationRequest;
import com.example.ojt.model.entity.Location;
import com.example.ojt.service.localtion.ILocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/locations")
@RequiredArgsConstructor
public class LocationController {

    private final ILocationService locationService;

    @GetMapping
    public ResponseEntity<Page<Location>> findAll(
            @PageableDefault(size = 1000)
            Pageable pageable,
            @RequestParam(defaultValue = "") String search
    ) {
        Page<Location> locations = locationService.findAll(pageable, search);
        return ResponseEntity.ok(locations);
    }

    @PostMapping
    public ResponseEntity<String> addLocation(@RequestBody LocationRequest locationRequest) {
        try {
            locationService.addLocation(locationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Location added successfully");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateLocation(
            @PathVariable Integer id,
            @RequestBody LocationRequest locationRequest
    ) {
        try {
            locationService.updateLocation(locationRequest, id);
            return ResponseEntity.ok("Location updated successfully");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable Integer id) {
        try {
            locationService.deleteByIdLocation(id);
            return ResponseEntity.ok("Location deleted successfully");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
