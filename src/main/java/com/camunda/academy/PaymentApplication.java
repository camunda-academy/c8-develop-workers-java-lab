package com.camunda.academy;

import java.time.Duration;
import java.util.Scanner;

import com.camunda.academy.handler.PDFCreatorHandler;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.worker.JobWorker;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;

public class PaymentApplication {

	// Zeebe Client Credentials
    private static final String ZEEBE_ADDRESS = "c7e94005-c52d-40f2-b636-2f064700ac54.dsm-1.zeebe.camunda.io:443";
    private static final String ZEEBE_CLIENT_ID = "Et6udCeUudspLu4xpERF1cSr3ObPM5T4";
    private static final String ZEEBE_CLIENT_SECRET = "FJEq.H4BCFU-Tz~7ipoGaVxPIrk9VDQgonjYzLn9MaxbDa1XAWkPMYrbuVY2T~gf";
    private static final String ZEEBE_AUTHORIZATION_SERVER_URL = "https://login.cloud.camunda.io/oauth/token";
    private static final String ZEEBE_TOKEN_AUDIENCE = "zeebe.camunda.io";      

	// Process instance creation
	private static final String PROCESS_ID = "pdf-creator";	
	private static final int NUM_INSTANCES = 20; // TOTAL NUMBER OF NEW PROCESS INSTANCES CREATED

	// Client configuration
	private static final int MAX_JOBS_ACTIVATE = 32; // Default 32
	private static final int NUM_THREADS = 2; // Default 1

	// Worker configuration
	private static final int POLL_INTERVAL = 5; 			// Max interval to check for new jobs
	private static final int WORKER_REQUEST_TIMEOUT = 0; 	// Set the request timeout for activate job request used to poll for new job. Default 0. if the requestTimeout < 0, long polling is disabled and the request is completed immediately, even when no job is activated
	private static final int WORKER_TIMEOUT = 5; 		// Set the time for how long a job is exclusively assigned for this worker.
	private static final int WORKER_MAX_JOBS_ACTIVATE = 2; // IS THIS REDUNDANT? CLIENT SIDE AND WORKER SIDE? This takes priority
	private static final boolean STREAM_ENABLED = false; 	// If set as enabled, the job worker will use a mix of streaming and polling to activate jobs
	private static final int STREAM_TIMEOUT = 10000;		// If streaming is enabled, sets a maximum lifetime for a given stream.


	public static void main(String[] args) {

		final OAuthCredentialsProvider credentialsProvider = new OAuthCredentialsProviderBuilder()
			.authorizationServerUrl(ZEEBE_AUTHORIZATION_SERVER_URL)
			.audience(ZEEBE_TOKEN_AUDIENCE)
			.clientId(ZEEBE_CLIENT_ID)
			.clientSecret(ZEEBE_CLIENT_SECRET)
			.build();

		try (final ZeebeClient client = ZeebeClient.newClientBuilder()
		     	.gatewayAddress(ZEEBE_ADDRESS)
		     	.credentialsProvider(credentialsProvider)
				.defaultJobWorkerMaxJobsActive(MAX_JOBS_ACTIVATE)
				.numJobWorkerExecutionThreads(NUM_THREADS)
		     	.build()) {

			// Request the Cluster Topology
			//System.out.println("Connected to: " + client.newTopologyRequest().send().join());
			
			// Process Instance creator looper
			startProcessInstances(client, NUM_INSTANCES);

			// Start a Job Worker			
			final JobWorker PDFCreatorWorker = client.newWorker()
				.jobType("pdfCreation")
				.handler(new PDFCreatorHandler())
				//.fetchVariables(args)
				.pollInterval(Duration.ofSeconds(POLL_INTERVAL))
				//.maxJobsActive(WORKER_MAX_JOBS_ACTIVATE)
				.requestTimeout(Duration.ofSeconds(WORKER_REQUEST_TIMEOUT))
				.timeout(Duration.ofSeconds(WORKER_TIMEOUT).toMillis())
				//.streamEnabled(STREAM_ENABLED)
				//.streamTimeout(Duration.ofSeconds(STREAM_TIMEOUT))
                .open();

			// Terminate the worker with an Integer input
			Scanner sc = new Scanner(System.in);			
			sc.nextInt();
			sc.close();
			PDFCreatorWorker.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void startProcessInstances(ZeebeClient zeebeClient, int numInstances) {
		System.out.println(PROCESS_ID + ": Starting "+ numInstances + " process instances...");
		for (int i = 0; i < numInstances; i++) {
			zeebeClient.newCreateInstanceCommand()
			.bpmnProcessId(PROCESS_ID)
			.latestVersion()
			.send()				
			.join();
		}
		System.out.println("... " + numInstances + " instances created");
	}
}
