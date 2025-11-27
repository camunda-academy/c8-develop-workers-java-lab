package com.camunda.academy.handler;

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

        logger.info("Handling job: {} Packing items", job.getKey());
        trackingOrderService.packItems(job);
        logger.info("Handling job: {} Items packed successfully", job.getKey());

        client.newCompleteCommand(job.getKey()).send().join();
    }
}