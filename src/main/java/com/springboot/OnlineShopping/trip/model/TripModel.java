package com.springboot.OnlineShopping.trip.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "trips")
public class TripModel {

    @Id
    private String id;
    private String title;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private String ownerId;

    // Nested list of activities inside the single document
    private List<TripActivity> activities = new ArrayList<>();
}
