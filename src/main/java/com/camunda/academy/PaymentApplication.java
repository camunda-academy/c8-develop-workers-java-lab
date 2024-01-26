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

	private static final int WORKER_TIMEOUT = 10;
	private static final int NUM_INSTANCES = 1;

	

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
		     	.build()) {

			// Request the Cluster Topology
			System.out.println("Connected to: " + client.newTopologyRequest().send().join());
			
			//Instance creator looper
			startProcessInstances(client, NUM_INSTANCES);

			// Start a Job Worker
			
			final JobWorker creditCardWorker = client.newWorker()
				.jobType("pdfCreation")
				.handler(new PDFCreatorHandler())
				.timeout(Duration.ofSeconds(WORKER_TIMEOUT).toMillis())
				.streamEnabled(false)
                .open();

			// Terminate the worker with an Integer input
			Scanner sc = new Scanner(System.in);			
			sc.nextInt();
			sc.close();
			creditCardWorker.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void startProcessInstances(ZeebeClient zeebeClient, int numInstances) {
		System.out.println("Starting "+ numInstances + " process instances...");
		for (int i = 0; i < numInstances; i++) {
			zeebeClient.newCreateInstanceCommand()
			.bpmnProcessId("pdf-creator")
			.latestVersion()
			.send()				
			.join(); // using blocking command
		}
		System.out.println("... " + numInstances + " instances created");
	}
}
