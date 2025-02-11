package com.example.myapplication.model;

import java.util.Map;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Account {
    private String username;
    private double cash;
    private Map<Integer, Bet> bet;
}
