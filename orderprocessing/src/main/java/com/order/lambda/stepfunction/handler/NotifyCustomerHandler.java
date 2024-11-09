package com.order.lambda.stepfunction.handler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Map;

public class NotifyCustomerHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
    	System.out.println("input_________NotifyCustomerHandler_____________ : "+input);
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Map<String, Object> order = (Map<String, Object>) input.get("order");
        String orderId = (String) input.get("order_id");
        // Simulate sending a notification
        return Map.of("status", "sent", "order_id", orderId);
    }
}
