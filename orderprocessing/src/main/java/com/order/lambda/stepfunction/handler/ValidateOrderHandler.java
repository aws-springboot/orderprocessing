package com.order.lambda.stepfunction.handler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Map;

public class ValidateOrderHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
    	System.out.println("input________ValidateOrderHandler______________ : "+input);
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Map<String, Object> order = (Map<String, Object>) input.get("order");
        int quantity = (int) order.get("product_quantity");
        
        if (quantity <= 10) {  // Assume stock is available if quantity <= 10
            return Map.of("status", "success", "order_id", order.get("order_id"));
        } else {
            return Map.of("status", "failed", "reason", "Insufficient stock");
        }
    }
}
