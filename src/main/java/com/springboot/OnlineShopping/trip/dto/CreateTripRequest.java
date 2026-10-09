package com.springboot.OnlineShopping.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTripRequest {

    private String title;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private String ownerId;
    private List<ActivityRequest> activities = new ArrayList<>();
}
