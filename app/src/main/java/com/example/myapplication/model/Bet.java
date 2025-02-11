package com.example.myapplication.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Bet {
    private int carId;
    private double bet;
    private String username;
}
