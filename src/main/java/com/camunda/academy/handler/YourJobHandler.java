package com.camunda.academy.handler;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;

public class YourJobHandler implements JobHandler {

	@Override
	public void handle(JobClient client, ActivatedJob job) throws Exception {
		
		System.out.println("Job handled: " + job.getType());

		// Complete the Job
		client.newCompleteCommand(job.getKey()).send().join();
	}
}