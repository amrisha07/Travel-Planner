package com.springboot.OnlineShopping.trip.repository;

import com.springboot.OnlineShopping.trip.model.TripModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TripRepository extends MongoRepository<TripModel, String> {

    // Custom query method to find trips owned by a specific user
    List<TripModel> findByOwnerId(String ownerId);
}
