package com.order.lambda.stepfunction.handler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Map;

public class GenerateInvoiceHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
    	System.out.println("input_________GenerateInvoiceHandler_____________ : "+input);
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String orderId = (String) input.get("order_id");
        // Simulate invoice generation
        return Map.of("status", "generated", "order_id", orderId);
    }
}
