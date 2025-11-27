package com.camunda.academy.handler;

import java.util.Map;

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

        final Map<String, Object> inputVariables = job.getVariablesAsMap();
        final String orderId = (String) inputVariables.get("orderId");

        logger.info("Order: {} Tracking status", orderId);
        trackingOrderService.trackOrderStatus(job);
        logger.info("Order: {} Status tracked successfully", orderId);

        logger.info("List of variables from Zeebe: {}",job.getVariables());

        client.newCompleteCommand(job.getKey()).send().join();
    }
}