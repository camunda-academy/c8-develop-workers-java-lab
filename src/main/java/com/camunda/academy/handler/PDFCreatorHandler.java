package com.camunda.academy.handler;

import com.camunda.academy.JobCounter;
import com.camunda.academy.services.PDFService;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;

public class PDFCreatorHandler implements JobHandler {

	private JobCounter counter;

	@Override
	public void handle(JobClient client, ActivatedJob job) throws Exception {
		
		System.out.println("Job handled: " + job.getType());
		
		PDFService PDFService = new PDFService();
		PDFService.createPDF();
		//counter.init();

		// Complete the Job
		client.newCompleteCommand(job.getKey()).send().join();
		//counter.inc();
		System.out.println("Job completed: " + job.getType());
	}
}