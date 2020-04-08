package com.ftgo.deliveryservice.model;

import javax.persistence.*;

import com.ftgo.common.model.Address;

import java.time.LocalDateTime;

@Entity
@Access(AccessType.FIELD)
public class Delivery {
	@Id
	private Long id;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "street1", column = @Column(name = "pickup_street1")),
			@AttributeOverride(name = "street2", column = @Column(name = "pickup_street2")),
			@AttributeOverride(name = "city", column = @Column(name = "pickup_city")),
			@AttributeOverride(name = "state", column = @Column(name = "pickup_state")),
			@AttributeOverride(name = "zip", column = @Column(name = "pickup_zip")), })
	private Address pickupAddress;

	@Enumerated(EnumType.STRING)
	private DeliveryState state;

	private long restaurantId;
	private LocalDateTime pickUpTime;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "street1", column = @Column(name = "delivery_street1")),
			@AttributeOverride(name = "street2", column = @Column(name = "delivery_street2")),
			@AttributeOverride(name = "city", column = @Column(name = "delivery_city")),
			@AttributeOverride(name = "state", column = @Column(name = "delivery_state")),
			@AttributeOverride(name = "zip", column = @Column(name = "delivery_zip")), })

	private Address deliveryAddress;
	private LocalDateTime deliveryTime;

	private Long assignedCourier;

	private Delivery() {
	}

	public Delivery(long orderId, long restaurantId, Address pickupAddress, Address deliveryAddress) {
		this.id = orderId;
		this.pickupAddress = pickupAddress;
		this.state = DeliveryState.PENDING;
		this.restaurantId = restaurantId;
		this.deliveryAddress = deliveryAddress;
	}

	public static Delivery create(long orderId, long restaurantId, Address pickupAddress, Address deliveryAddress) {
		return new Delivery(orderId, restaurantId, pickupAddress, deliveryAddress);
	}

	public void schedule(LocalDateTime readyBy) {

	}

	public void cancel() {
		this.state = DeliveryState.CANCELLED;
		this.assignedCourier = null;
	}

	public void assignCourier(long courierId) {
		this.assignedCourier = courierId;
	}

	public long getId() {
		return id;
	}

	public long getRestaurantId() {
		return restaurantId;
	}

	public Address getDeliveryAddress() {
		return deliveryAddress;
	}

	public Address getPickupAddress() {
		return pickupAddress;
	}

	public DeliveryState getState() {
		return state;
	}

	public Long getAssignedCourier() {
		return assignedCourier;
	}
}
