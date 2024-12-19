package com.camunda.academy.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camunda.academy.services.TrackingOrderService;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;

public class OrderHandler implements JobHandler {

    final Logger logger = LoggerFactory.getLogger(OrderHandler.class);
    private TrackingOrderService trackingOrderService = new TrackingOrderService();

    @Override
    public void handle(JobClient client, ActivatedJob job) throws Exception {

        final Map<String, Object> inputVariables = job.getVariablesAsMap();
        final String orderId = (String) inputVariables.get("orderId");

        logger.info("Order: {} Tracking status", orderId);        
        trackingOrderService.trackOrderStatus(job);        
        logger.info("Order: {} Status tracked successfully", orderId);
        
        client.newCompleteCommand(job.getKey())
            .send()
            .join();
    }
}