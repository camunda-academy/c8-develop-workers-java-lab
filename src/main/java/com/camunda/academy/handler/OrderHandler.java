package com.camunda.academy.handler;

import java.util.Map;

import com.camunda.academy.services.TrackingOrderService;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;

public class OrderHandler implements JobHandler {

	@Override
	public void handle(JobClient client, ActivatedJob job) throws Exception {
		
		System.out.println("(" + job.getKey()+ ") Handling job: " + job.getType());
		
		TrackingOrderService TrackingOrderService = new TrackingOrderService();		
		
		try{
			TrackingOrderService.trackOrderStatus(job);		
			
			final Map<String, Object> inputVariables = job.getVariablesAsMap();
			System.out.println("List of variables from Zeebe: " + job.getVariables());
			
			final Map<String, Object> order = (Map<String, Object>) inputVariables.get("order");
			System.out.println("(" + job.getKey() + ") Getting order (" + order.get("orderId") + ")");
			
			client.newCompleteCommand(job.getKey()).send().join();

		}catch(Exception e){
			e.printStackTrace();
		}		
	}	
}