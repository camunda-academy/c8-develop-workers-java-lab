package com.camunda.academy.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camunda.academy.services.TrackingOrderService;

import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import io.camunda.client.api.worker.JobHandler;

public class ProcessPaymentHandler implements JobHandler {

    private static final Logger logger = LoggerFactory.getLogger(ProcessPaymentHandler.class);

    private final TrackingOrderService trackingOrderService = new TrackingOrderService();

    @Override
    public void handle(JobClient client, ActivatedJob job) throws Exception {
        logger.info("Handling job: {} Processing payment", job.getKey());
        trackingOrderService.processPayment(job);
        logger.info("Handling job: {} Payment processed successfully", job.getKey());

        client.newCompleteCommand(job.getKey()).send().join();
    }
}
