package com.camunda.academy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import io.camunda.client.impl.CamundaClientCloudBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camunda.academy.handler.OrderHandler;
import com.camunda.academy.handler.PackItemsHandler;
import com.camunda.academy.handler.ProcessPaymentHandler;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.worker.JobWorker;
import io.camunda.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.client.impl.oauth.OAuthCredentialsProviderBuilder;

import static io.camunda.client.ClientProperties.*;

public class OrderApplication {

    // Zeebe Client Credentials
    private static final String CAMUNDA_PROPERTIES_PATH = "src/main/resources/application.properties";

    private static final Logger logger = LoggerFactory.getLogger(OrderApplication.class);

    // Process instance creation
    private static final String PROCESS_ID = "orderProcess";
    private static final int NUM_INSTANCES = 1; // TOTAL NUMBER OF NEW PROCESS INSTANCES CREATED

    // Worker configuration
    private static final int WORKER_TIMEOUT = 1; // Set the time for how long a job is exclusively assigned for this worker.

    public static void main(String[] args) throws IOException {
        var properties = loadProperties();
        try (final CamundaClient client = CamundaClient.newCloudClientBuilder()
                .withClusterId(properties.getProperty(CLOUD_CLUSTER_ID))
                .withClientId(properties.getProperty(CLOUD_CLIENT_ID))
                .withClientSecret(properties.getProperty(CLOUD_CLIENT_SECRET))
                .withRegion(properties.getProperty(CLOUD_REGION))
                .build()) {
            // Process Instance creator looper
            startProcessInstances(client, NUM_INSTANCES);

            final JobWorker OrderWorker = client.newWorker()
                .jobType("trackOrderStatus")
                .handler(new OrderHandler())
                .open();
            
            final JobWorker ProcessPaymentWorker = client.newWorker()
                .jobType("processPayment")
                .handler(new ProcessPaymentHandler())
                .open();
        
            final JobWorker PackItemsWorker = client.newWorker()
                .jobType("packItems")
                .handler(new PackItemsHandler())
                .open();
             
            // Terminate the worker with an Integer input
            Scanner sc = new Scanner(System.in);
            sc.nextInt();
            sc.close();
            OrderWorker.close();
            ProcessPaymentWorker.close();
            PackItemsWorker.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startProcessInstances(CamundaClient camundaClient, int numInstances) {
        logger.info("Starting: " + numInstances + " process instances for process: " + PROCESS_ID);
        for (int i = 0; i < numInstances; i++) {
            FakeRandomizer fakeRandomizer = new FakeRandomizer();
            Map<String, Object> fakeRequest = fakeRandomizer.getRandom();
            logger.info("Generating Order({})",fakeRequest.get("orderId"));
            var event = camundaClient.newCreateInstanceCommand()
                .bpmnProcessId(PROCESS_ID)
                .latestVersion()
                .variables(fakeRequest)
                .send()
                .join();
            logger.info("Process instance: {} started", event.getProcessInstanceKey());
        }
        logger.info("Ending: " + numInstances + " instances created for process: " + PROCESS_ID);
    }

    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(CAMUNDA_PROPERTIES_PATH)) {
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw e;
        }
    }
}
