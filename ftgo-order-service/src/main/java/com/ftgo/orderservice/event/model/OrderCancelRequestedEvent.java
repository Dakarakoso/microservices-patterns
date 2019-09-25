package com.ftgo.orderservice.event.model;

import io.eventuate.tram.events.common.DomainEvent;
import net.chrisrichardson.ftgo.orderservice.api.model.OrderState;

public class OrderCancelRequestedEvent implements DomainEvent {
	private OrderState state;

	public OrderCancelRequestedEvent(OrderState state) {
		this.state = state;
	}

	public OrderState getState() {
		return state;
	}
}
