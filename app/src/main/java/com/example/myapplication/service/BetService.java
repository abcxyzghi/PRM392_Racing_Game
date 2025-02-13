package com.example.myapplication.service;

import com.example.myapplication.model.Bet;

import java.util.HashMap;
import java.util.Map;

public class BetService {
    private static BetService instance;
    public Map<Integer, Bet> userBet;
    private BetService () {
        userBet = new HashMap<>();
    }

    public static BetService getInstance() {
        if(instance == null) instance = new BetService();
        return instance;
    }

    public void addBet(Bet bet) {
        Bet temp = getBet(bet.getCarId());
        if (temp == null) {
            userBet.put(bet.getCarId(), bet);
        }
        updateBet(bet);
    }
    public void betReset() {
        userBet.clear();
    }
    public void updateBet(Bet bet) {
        userBet.replace(bet.getCarId(), bet);
    }
    public Bet getBet(Integer carId) {
        return userBet.get(carId);
    }

    public double calculateBet(int winCar) {
        double winnings = 0;
        double totalBet = 0;

        for (Bet bet : userBet.values()) {
            totalBet += bet.getBet(); // Tổng số tiền đã cược
            if (bet.getCarId() == winCar) {
                winnings = bet.getBet() * 2; // Nếu xe thắng, nhân đôi số tiền đặt
            }
        }

        return winnings - totalBet; // Lợi nhuận cuối cùng
    }
    public Double totalBet() {
        double total = 0;
        for (Bet item: userBet.values()) {
            total += item.getBet();
        }
        return total;
    }

    public boolean validateBet() {
        for(Bet item: userBet.values()) {
            if(item.getBet() <= 1) {
                return false;
            }
        }
        return true;
    }
}
