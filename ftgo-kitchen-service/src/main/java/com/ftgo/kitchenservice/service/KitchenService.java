package com.ftgo.kitchenservice.service;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ftgo.kitchenservice.api.model.TicketDetails;
import com.ftgo.kitchenservice.event.TicketDomainEvent;
import com.ftgo.kitchenservice.event.TicketDomainEventPublisher;
import com.ftgo.kitchenservice.exception.TicketNotFoundException;
import com.ftgo.kitchenservice.model.Restaurant;
import com.ftgo.kitchenservice.model.Ticket;
import com.ftgo.kitchenservice.repository.RestaurantRepository;
import com.ftgo.kitchenservice.repository.TicketRepository;
import com.ftgo.restaurantservice.api.model.RestaurantMenu;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * The kitchen service class for creating and managing tickets.
 *
 * @author  Wuyi Chen
 * @date    04/14/2020
 * @version 1.0
 * @since   1.0
 */
@Transactional
public class KitchenService {
	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private TicketDomainEventPublisher domainEventPublisher;

	@Autowired
	private RestaurantRepository restaurantRepository;

	public void createMenu(long id, RestaurantMenu menu) {
		Restaurant restaurant = new Restaurant(id, menu.getMenuItems());
		restaurantRepository.save(restaurant);
	}

	public void reviseMenu(long ticketId, RestaurantMenu revisedMenu) {
		Restaurant restaurant = restaurantRepository.findById(ticketId)
				.orElseThrow(() -> new TicketNotFoundException(ticketId));
		restaurant.reviseMenu(revisedMenu);
	}

	public Ticket createTicket(long restaurantId, Long ticketId, TicketDetails ticketDetails) {
		ResultWithDomainEvents<Ticket, TicketDomainEvent> rwe = Ticket.create(restaurantId, ticketId, ticketDetails);
		ticketRepository.save(rwe.result);
		domainEventPublisher.publish(rwe.result, rwe.events);
		return rwe.result;
	}

	/**
	 * Accept an existing ticket.
	 * 
	 * @param  ticketId
	 *         The ticket ID for looking up.
	 *         
	 * @param  readyBy
	 *         The due time when the ticket should be accepted.
	 */
	public void accept(long ticketId, LocalDateTime readyBy) {
		Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));
		List<TicketDomainEvent> events = ticket.accept(readyBy);
		domainEventPublisher.publish(ticket, events);
	}

	public void confirmCreateTicket(Long ticketId) {
		Ticket ro = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));
		List<TicketDomainEvent> events = ro.confirmCreate();
		domainEventPublisher.publish(ro, events);
	}

	public void cancelCreateTicket(Long ticketId) {
		Ticket ro = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));
		List<TicketDomainEvent> events = ro.cancelCreate();
		domainEventPublisher.publish(ro, events);
	}

	public void cancelTicket(long restaurantId, long ticketId) {
		Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));
		// TODO - verify restaurant id
		List<TicketDomainEvent> events = ticket.cancel();
		domainEventPublisher.publish(ticket, events);
	}

	public void confirmCancelTicket(long restaurantId, long ticketId) {
		Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));
		// TODO - verify restaurant id
		List<TicketDomainEvent> events = ticket.confirmCancel();
		domainEventPublisher.publish(ticket, events);
	}

	public void undoCancel(long restaurantId, long ticketId) {
		Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));
		// TODO - verify restaurant id
		List<TicketDomainEvent> events = ticket.undoCancel();
		domainEventPublisher.publish(ticket, events);
	}

	public void beginReviseOrder(long restaurantId, Long ticketId, Map<String, Integer> revisedLineItemQuantities) {
		Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));
		// TODO - verify restaurant id
		List<TicketDomainEvent> events = ticket.beginReviseOrder(revisedLineItemQuantities);
		domainEventPublisher.publish(ticket, events);
	}

	public void undoBeginReviseOrder(long restaurantId, Long ticketId) {
		Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));
		// TODO - verify restaurant id
		List<TicketDomainEvent> events = ticket.undoBeginReviseOrder();
		domainEventPublisher.publish(ticket, events);
	}

	public void confirmReviseTicket(long restaurantId, long ticketId, Map<String, Integer> revisedLineItemQuantities) {
		Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));
		// TODO - verify restaurant id
		List<TicketDomainEvent> events = ticket.confirmReviseTicket(revisedLineItemQuantities);
		domainEventPublisher.publish(ticket, events);
	}
}
