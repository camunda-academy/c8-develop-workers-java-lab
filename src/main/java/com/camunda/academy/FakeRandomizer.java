package com.camunda.academy;

import java.util.HashMap;
import java.util.Map;

import com.github.javafaker.Faker;

public class FakeRandomizer {

    final Faker faker = new Faker();
    final Map<String, Object> inputVariables = new HashMap<String, Object>();
    final Map<String, Object> requester = new HashMap<String, Object>();
    final Map<String, Object> order = new HashMap<String, Object>();
    final Map<String, Object> business = new HashMap<String, Object>();
    final Map<String, Object> address = new HashMap<String, Object>();    
    final String uniqueId = uuid();
    final boolean packaged = false;

    public FakeRandomizer() {
        
        order.put("orderId", uniqueId);
        order.put("packaged", packaged);
        order.put("productName", faker.commerce().productName());
        order.put("price", faker.commerce().price());
        order.put("promotionCode", faker.commerce().promotionCode());
        order.put("material", faker.commerce().material());
        order.put("department", faker.commerce().department());

        requester.put("name", faker.name().fullName());
        requester.put("firstName", faker.name().firstName());
        requester.put("lastName", faker.name().lastName());
        requester.put("bloodGroup", faker.name().bloodGroup());

        business.put("creditCardType", faker.business().creditCardType());
        business.put("creditCardNumber", faker.business().creditCardNumber());
        business.put("creditCardType", faker.business().creditCardExpiry());

        address.put("streetAddress", faker.address().streetAddress());
        address.put("cp", faker.address().zipCode());
        address.put("city", faker.address().city());
        address.put("country", faker.address().country());

        
        inputVariables.put("order", order);
        inputVariables.put("requester", requester);
        inputVariables.put("address", address);
        inputVariables.put("business", business);
        
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
