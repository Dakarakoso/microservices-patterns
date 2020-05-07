package com.ftgo.apiagateway.proxies;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.ftgo.apiagateway.exception.OrderNotFoundException;
import com.ftgo.apiagateway.orders.OrderDestinations;
import com.ftgo.apiagateway.orders.OrderInfo;

import reactor.core.publisher.Mono;

/**
 * The proxy class of the order service.
 * 
 * @author  Wuyi Chen
 * @date    05/05/2020
 * @version 1.0
 * @since   1.0
 */
@Service
public class OrderService {
	private OrderDestinations orderDestinations;
	private WebClient         client;

	/**
	 * Constructs a {@code OrderService} object.
	 * 
	 * @param  orderDestinations
	 *         The destination of the order service.
	 *         
	 * @param  client
	 *         The HTTP client for performing HTTP requests 
	 */
	public OrderService(OrderDestinations orderDestinations, WebClient client) {
		this.orderDestinations = orderDestinations;
		this.client            = client;
	}

	/**
	 * Get the information of an order by order ID.
	 * 
	 * @param  orderId
	 *         The order ID for query.
	 *         
	 * @return  The order info wrapped in a {@code Mono} object.
	 */
	public Mono<OrderInfo> findOrderById(String orderId) {
		Mono<ClientResponse> response = client.get().uri(orderDestinations.getOrderServiceUrl() + "/orders/{orderId}", orderId).exchange();
		return response.flatMap(resp -> {
			switch (resp.statusCode()) {
			case OK:
				return resp.bodyToMono(OrderInfo.class);
			case NOT_FOUND:
				return Mono.error(new OrderNotFoundException());
			default:
				return Mono.error(new RuntimeException("Unknown" + resp.statusCode()));
			}
		});
	}
}