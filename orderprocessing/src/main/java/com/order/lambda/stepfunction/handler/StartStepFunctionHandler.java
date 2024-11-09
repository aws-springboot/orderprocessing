package com.order.lambda.stepfunction.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClientBuilder;
import com.amazonaws.services.stepfunctions.model.StartExecutionRequest;
import com.amazonaws.services.stepfunctions.model.StartExecutionResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Map;

public class StartStepFunctionHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();
	private final AWSStepFunctions stepFunctionsClient = AWSStepFunctionsClientBuilder.defaultClient();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        	System.out.println("StartStepFunctionHandler------------------------------"+input.getBody()); 
            Map<String, Object> body = null;
			try {
				body = objectMapper.readValue(input.getBody(), Map.class);
			} catch (JsonMappingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
            Map<String, Object> order = (Map<String, Object>) body.get("order");
            System.out.println("StartStepFunctionHandler-order-----------------------------"+order);

            if (order == null) {
                return new APIGatewayProxyResponseEvent()
                	    .withStatusCode(500)
                	    .withBody("{\"message\": \"Order object is null: \"}")
                	    .withHeaders(Collections.singletonMap("Content-Type", "application/json"));
            }
            String orderId = (String) order.get("order_id");
            int productQuantity = (int) order.get("product_quantity");
            String customerId = (String) order.get("customer_id");
            
            // Create the input for the Step Function
            Map<String, Object> stepFunctionInput = Map.of(
                "order_id", orderId,
                "product_quantity", productQuantity,
                "customer_id", customerId
            );
            // Convert the input to a JSON string (to pass to Step Functions)
            String inputJson = null;
			try {
				inputJson = objectMapper.writeValueAsString(stepFunctionInput);
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            // Start the Step Function execution
            StartExecutionRequest startExecutionRequest = new StartExecutionRequest()
                    .withStateMachineArn("arn:aws:states:ap-south-1:588738584877:stateMachine:OrderProcessingStateMachine-b8JufPg1iiaE") // Replace with your actual ARN
                    .withInput(inputJson); // Input passed to the state machine
            
            try {
                // Call StartExecution
                StartExecutionResult startExecutionResult = stepFunctionsClient.startExecution(startExecutionRequest);

                // Returning the execution ARN or execution status (or any other information you want)
                return new APIGatewayProxyResponseEvent()
                	    .withStatusCode(200)
                	    .withBody("{\"message\": \"Order processed: " + startExecutionResult.getExecutionArn() + "\"}")
                	    .withHeaders(Collections.singletonMap("Content-Type", "application/json"));
            } catch (Exception e) {
                // Handling any exception that occurs during execution
                context.getLogger().log("Error starting Step Function execution: " + e.getMessage());
                return new APIGatewayProxyResponseEvent()
                		.withStatusCode(502)
                	    .withBody("{\"message\": \"Exception " + e.getMessage() + "\"}")
                	    .withHeaders(Collections.singletonMap("Content-Type", "application/json"));
            }
            
    }
}
