package com.ftgo.deliveryservice.event;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;

import java.time.LocalDateTime;

import com.ftgo.common.model.Address;
import com.ftgo.deliveryservice.service.DeliveryService;
import com.ftgo.kitchenservice.api.KitchenServiceChannels;
import com.ftgo.kitchenservice.event.model.TicketAcceptedEvent;
import com.ftgo.kitchenservice.event.model.TicketCancelledEvent;
import com.ftgo.orderservice.api.OrderServiceChannels;
import com.ftgo.orderservice.api.event.OrderCreatedEvent;
import com.ftgo.restaurantservice.api.RestaurantServiceChannels;
import com.ftgo.restaurantservice.api.event.RestaurantCreatedEvent;

/**
 * The event handlers for incoming events.
 *
 * @author  Wuyi Chen
 * @date    05/16/2020
 * @version 1.0
 * @since   1.0
 */
public class DeliveryServiceEventConsumer {
	private DeliveryService deliveryService;

	public DeliveryServiceEventConsumer(DeliveryService deliveryService) {
		this.deliveryService = deliveryService;
	}

	public DomainEventHandlers domainEventHandlers() {
		return DomainEventHandlersBuilder.forAggregateType(KitchenServiceChannels.TICKET_EVENT_CHANNEL)
				.onEvent(TicketAcceptedEvent.class, this::handleTicketAcceptedEvent)
				.onEvent(TicketCancelledEvent.class, this::handleTicketCancelledEvent)
				.andForAggregateType(OrderServiceChannels.ORDER_EVENT_CHANNEL)
				.onEvent(OrderCreatedEvent.class, this::handleOrderCreatedEvent)
				.andForAggregateType(RestaurantServiceChannels.RESTAURANT_EVENT_CHANNEL)
				.onEvent(RestaurantCreatedEvent.class, this::handleRestaurantCreated).build();
	}

	public void handleRestaurantCreated(DomainEventEnvelope<RestaurantCreatedEvent> dee) {
		Address address = dee.getEvent().getAddress();
		deliveryService.createRestaurant(Long.parseLong(dee.getAggregateId()), dee.getEvent().getName(), address);
	}

	public void handleOrderCreatedEvent(DomainEventEnvelope<OrderCreatedEvent> dee) {
		Address address = dee.getEvent().getDeliveryAddress();
		deliveryService.createDelivery(Long.parseLong(dee.getAggregateId()),
				dee.getEvent().getOrderDetails().getRestaurantId(), address);
	}

	public void handleTicketAcceptedEvent(DomainEventEnvelope<TicketAcceptedEvent> dee) {
		LocalDateTime readyBy = dee.getEvent().getReadyBy();
		deliveryService.scheduleDelivery(Long.parseLong(dee.getAggregateId()), readyBy);
	}

	public void handleTicketCancelledEvent(DomainEventEnvelope<TicketCancelledEvent> dee) {
		deliveryService.cancelDelivery(Long.parseLong(dee.getAggregateId()));
	}
}
