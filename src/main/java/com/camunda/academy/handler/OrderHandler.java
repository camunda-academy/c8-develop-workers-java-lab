package com.camunda.academy.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camunda.academy.services.TrackingOrderService;

import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import io.camunda.client.api.worker.JobHandler;

public class OrderHandler implements JobHandler {

    private static final Logger logger = LoggerFactory.getLogger(OrderHandler.class);

    private final TrackingOrderService trackingOrderService = new TrackingOrderService();

    @Override
    public void handle(JobClient client, ActivatedJob job) throws Exception {

        logger.info("Handling job: {} Tracking status", job.getKey());
        trackingOrderService.trackOrderStatus(job);
        logger.info("Handling job: {} Order status tracked successfully", job.getKey());

        client.newCompleteCommand(job.getKey()).send().join();
    }
}