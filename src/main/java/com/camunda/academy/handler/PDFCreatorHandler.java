package com.camunda.academy.handler;

import com.camunda.academy.JobCounter;
import com.camunda.academy.services.PDFService;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;

public class PDFCreatorHandler implements JobHandler {

	private JobCounter counter = new JobCounter();

	@Override
	public void handle(JobClient client, ActivatedJob job) throws Exception {
		
		System.out.println("Handling job: " + job.getType());
		
		//Blocking Call
		//blockingPDFServiceCall(client, job);

		//Non Blocking Call
		nonBlockingPDFServiceCall(client, job);
		
		System.out.println("Job completed: " + job.getType());
	}

	public void blockingPDFServiceCall(final JobClient client, final ActivatedJob job) throws Exception{
		
		PDFService PDFService = new PDFService();
		PDFService.createPDF();
		counter.init();

		// Complete the Job blocking
		client.newCompleteCommand(job.getKey()).send().join();

		counter.inc();
	}

	public void nonBlockingPDFServiceCall(final JobClient client, final ActivatedJob job) throws Exception{
		
		PDFService PDFService = new PDFService();
		PDFService.createPDF();
		counter.init();

		// Complete the Job nonblocking
		client.newCompleteCommand(job.getKey()).send()
			.thenApply(jobResponse -> { counter.inc(); return jobResponse;})
			.exceptionally(t -> {throw new RuntimeException("Could not complete job: " + t.getMessage(), t);});
		
	}
}