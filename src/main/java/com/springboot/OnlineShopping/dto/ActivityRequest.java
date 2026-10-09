package com.springboot.OnlineShopping.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityRequest {
    private String title;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;
}
