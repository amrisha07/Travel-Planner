package com.springboot.OnlineShopping.aws.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripCreatedEvent {
    private String tripId;
    private String title;
    private String destination;
    private LocalDate startDate;
    private String ownerId;
}
