package com.camunda.academy.handler;

import java.util.Map;

import com.camunda.academy.services.TrackingOrderService;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;

public class PackItemsHandler implements JobHandler {

	@Override
	public void handle(JobClient client, ActivatedJob job) throws Exception {

		System.out.println("(" + job.getKey()+ ") Handling job: " + job.getType());

		TrackingOrderService TrackingOrderService = new TrackingOrderService();		
		final Boolean packedItems = TrackingOrderService.packItems(job);		

		System.out.println("(" + job.getKey() + ") Items packed...");

		final Map<String, Object> inputVariables = job.getVariablesAsMap();
		final Map<String, Object> order = (Map<String, Object>) inputVariables.get("order");	
		
		System.out.println("List of variables from Zeebe: " + inputVariables);
			
    	order.put("packaged", packedItems);
		inputVariables.put("order", order);
    	
    	client.newCompleteCommand(job.getKey()).variables(inputVariables).send().join();
	}

	
}