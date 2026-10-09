package com.springboot.OnlineShopping.trip.controller;

import com.springboot.OnlineShopping.aws.model.TripCreatedEvent;
import com.springboot.OnlineShopping.aws.service.EventBridgePublisher;
import com.springboot.OnlineShopping.trip.dto.CreateTripRequest;
import com.springboot.OnlineShopping.trip.model.TripActivity;
import com.springboot.OnlineShopping.trip.model.TripModel;
import com.springboot.OnlineShopping.trip.repository.TripRepository;
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
    private final EventBridgePublisher eventPublisher;

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

        // 1. Save core relational state inside MongoDB
        TripModel savedTrip = tripRepository.save(tripEntity);

        // 2. Map and dispatch the Event Notification asynchronously
        TripCreatedEvent event = new TripCreatedEvent(
                savedTrip.getId(),
                savedTrip.getTitle(),
                savedTrip.getDestination(),
                savedTrip.getStartDate(),
                savedTrip.getOwnerId()
        );
        eventPublisher.publishTripCreatedEvent(event);

        return ResponseEntity.ok(savedTrip);
    }

    // Endpoint to get all trips
    @GetMapping
    public ResponseEntity<List<TripModel>> getAllTrips() {
        return ResponseEntity.ok(tripRepository.findAll());
    }
}
