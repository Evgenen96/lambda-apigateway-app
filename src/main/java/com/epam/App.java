package com.epam;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.lambda.runtime.Context;

public class App {

    public Object handleRequest(Request request, Context context) {
        AmazonDynamoDB adb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(adb);
        Product product = null;
        switch (request.getHttpMethod()) {
            case "GET":
                product = mapper.load(Product.class, request.getId());
                if (product == null) {
                    throw new ResourceNotFoundException("Resource Not Found with ID " + request.getId());
                }
                return product;
            case "POST":
                product = request.getProduct();
                Product exisingProduct = mapper.load(Product.class, product.getId());
                if (exisingProduct != null) {
                    throw new RuntimeException("Resource already exist with ID " + product.getId());
                }

                mapper.save(product);
                return product;
            case "default":
                break;

        }

        return null;
    }

}