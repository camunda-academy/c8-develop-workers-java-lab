package com.camunda.academy.services;

import java.time.Duration;

import io.camunda.zeebe.client.api.response.ActivatedJob;

public class TrackingOrderService {

    // Service duration in seconds
    private static final int TRACKING_TIME = 10;

    public void trackOrderStatus(ActivatedJob job) throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(TRACKING_TIME).toMillis());
    }

    public Boolean packItems(ActivatedJob job) throws InterruptedException {
        return true;
    }

    public String processPayment(ActivatedJob job) throws InterruptedException {
        return String.valueOf(System.currentTimeMillis());
    }
}