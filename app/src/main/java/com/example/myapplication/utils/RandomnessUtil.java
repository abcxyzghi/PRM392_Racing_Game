package com.example.myapplication.utils;

import java.util.Random;

public class RandomnessUtil {
    public static int maxSpeed = 10;
    public static int minSpeed = 1;
    public static int getRandomHorse() {
        // Simulate a random horse race outcome between horse 1, 2, or 3
        return (int) (Math.random() * 2) + 1;
    }

    public static int getRandomSpeed() {
        Random random = new Random();
        return random.nextInt(maxSpeed - minSpeed + 1) + minSpeed;
    }
}

