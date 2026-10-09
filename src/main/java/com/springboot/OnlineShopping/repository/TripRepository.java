package com.springboot.OnlineShopping.repository;

import com.springboot.OnlineShopping.model.TripModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TripRepository extends MongoRepository<TripModel, String> {

    // Custom query method to find trips owned by a specific user
    List<TripModel> findByOwnerId(String ownerId);
}
