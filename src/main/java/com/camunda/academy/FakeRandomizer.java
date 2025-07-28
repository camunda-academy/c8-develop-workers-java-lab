package com.camunda.academy;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javafaker.Faker;

// The FakeRandomizer class which will create fake input variables for our process instances
public class FakeRandomizer {

    private static final Logger logger = LoggerFactory.getLogger(FakeRandomizer.class);

    final Faker faker = new Faker();
    final Map<String, Object> inputVariables = new HashMap<String, Object>();
    final String uniqueId = uuid();
    final boolean packaged = false;

    public FakeRandomizer() {
        inputVariables.put("orderId", uniqueId);
        inputVariables.put("packaged", packaged);
        inputVariables.put("productName", faker.commerce().productName());
        inputVariables.put("price", faker.commerce().price());
        inputVariables.put("promotionCode", faker.commerce().promotionCode());
        inputVariables.put("material", faker.commerce().material());
        inputVariables.put("department", faker.commerce().department());
        inputVariables.put("paymentConfirmation", null);
    }

    public Map<String, Object> getRandom() {
        return inputVariables;
    }

    public static final String uuid() {
        String result = java.util.UUID.randomUUID().toString();
        logger.info("Generated UUID: {}", result);

        result = result.replaceAll("-", "");
        logger.info("UUID without dashes: {}", result);

        result = result.substring(0, 7);
        logger.info("UUID: {}", result);

        return result;
    }
}
