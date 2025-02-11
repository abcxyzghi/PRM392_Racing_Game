package com.example.myapplication.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarRowBet {
    private int rowId;
    private boolean isTick;
    private Bet bet;
}
