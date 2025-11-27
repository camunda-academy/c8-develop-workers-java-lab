package com.camunda.academy.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camunda.academy.services.TrackingOrderService;

import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import io.camunda.client.api.worker.JobHandler;

public class PackItemsHandler implements JobHandler {

    private static final Logger logger = LoggerFactory.getLogger(PackItemsHandler.class);

    private final TrackingOrderService trackingOrderService = new TrackingOrderService();

    @Override
    public void handle(JobClient client, ActivatedJob job) throws Exception {
        final Map<String, Object> inputVariables = job.getVariablesAsMap();
        final String orderId = (String) inputVariables.get("orderId");

        logger.info("Order: {} Packing items", orderId);
        final Boolean packedItems = trackingOrderService.packItems(job);
        logger.info("Order: {} Items packed successfully", orderId);

        client.newCompleteCommand(job.getKey())
            .variables(Map.of("packaged", packedItems))
            .send()
            .join();
    }
}