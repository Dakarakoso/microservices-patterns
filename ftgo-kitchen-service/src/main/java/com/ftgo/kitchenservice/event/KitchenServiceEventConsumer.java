package com.ftgo.kitchenservice.event;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;

import org.springframework.beans.factory.annotation.Autowired;

import com.ftgo.kitchenservice.service.KitchenService;
import com.ftgo.restaurantservice.api.event.RestaurantCreatedEvent;
import com.ftgo.restaurantservice.api.event.RestaurantMenuRevisedEvent;
import com.ftgo.restaurantservice.api.model.RestaurantMenu;

/**
 * The event handlers for incoming events.
 *
 * @author  Wuyi Chen
 * @date    04/14/2020
 * @version 1.0
 * @since   1.0
 */
public class KitchenServiceEventConsumer {
	@Autowired
	private KitchenService kitchenService;

	public DomainEventHandlers domainEventHandlers() {
		return DomainEventHandlersBuilder
				.forAggregateType("com.ftgo.restaurantservice.model.Restaurant")
				.onEvent(RestaurantCreatedEvent.class, this::createMenu)
				.onEvent(RestaurantMenuRevisedEvent.class, this::reviseMenu).build();
	}

	/**
	 * The listener for {@code RestaurantCreatedEvent}.
	 * 
	 * @param  de
	 *         The event envelope of {@code RestaurantCreatedEvent}.
	 */
	private void createMenu(DomainEventEnvelope<RestaurantCreatedEvent> de) {
		String restaurantIds = de.getAggregateId();
		long id = Long.parseLong(restaurantIds);
		RestaurantMenu menu = de.getEvent().getMenu();
		kitchenService.createMenu(id, menu);
	}

	/**
	 * The listener for {@code RestaurantMenuRevisedEvent}.
	 * 
	 * @param  de
	 *         The event envelope of {@code RestaurantMenuRevisedEvent}.
	 */
	public void reviseMenu(DomainEventEnvelope<RestaurantMenuRevisedEvent> de) {
		long id = Long.parseLong(de.getAggregateId());
		RestaurantMenu revisedMenu = de.getEvent().getRevisedMenu();
		kitchenService.reviseMenu(id, revisedMenu);
	}
}