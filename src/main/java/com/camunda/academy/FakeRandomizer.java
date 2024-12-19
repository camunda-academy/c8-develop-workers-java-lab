package com.camunda.academy;

import java.util.HashMap;
import java.util.Map;

import com.github.javafaker.Faker;

public class FakeRandomizer {

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

    public Map<String, Object> getRandom(){
        return inputVariables;
    }

    public static final String uuid(){
        String result = java.util.UUID.randomUUID().toString();
        result = result.replaceAll("-", "");
        result = result.substring(0, 7);
        return result;
    }
}
