package com.springboot.OnlineShopping.controller;

import com.springboot.OnlineShopping.dto.CreateTripRequest;
import com.springboot.OnlineShopping.model.TripActivity;
import com.springboot.OnlineShopping.model.TripModel;
import com.springboot.OnlineShopping.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripRepository tripRepository;

    // Endpoint to create a trip

    @PostMapping
    public ResponseEntity<TripModel> createTrip(@RequestBody CreateTripRequest request) {
        // Map DTO values safely onto your persistent Entity model
        TripModel tripEntity = new TripModel();
        tripEntity.setTitle(request.getTitle());
        tripEntity.setDestination(request.getDestination());
        tripEntity.setStartDate(request.getStartDate());
        tripEntity.setEndDate(request.getEndDate());
        tripEntity.setOwnerId(request.getOwnerId());

        // Map list of ActivityRequest DTOs into persistent Activity Entities
        if (request.getActivities() != null) {
            List<TripActivity> activityEntities = request.getActivities().stream()
                    .map(dto -> new TripActivity(
                            UUID.randomUUID().toString(), // Automatically generate a clean unique string ID
                            dto.getTitle(),
                            dto.getLocation(),
                            dto.getStartTime(),
                            dto.getEndTime(),
                            dto.getNotes()
                    ))
                    .toList();

            tripEntity.setActivities(activityEntities);
        }

        // Save entity to MongoDB
        TripModel savedTrip = tripRepository.save(tripEntity);

        return ResponseEntity.ok(savedTrip);
    }

    // Endpoint to get all trips
    @GetMapping
    public ResponseEntity<List<TripModel>> getAllTrips() {
        return ResponseEntity.ok(tripRepository.findAll());
    }
}
