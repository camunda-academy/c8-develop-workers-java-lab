package com.camunda.academy.handler;

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
			client.newCompleteCommand(job.getKey()).send().join();

		}catch(Exception e){
			e.printStackTrace();
		}		
	}	
}