package com.ftgo.deliveryservice.model;

import javax.persistence.*;
import java.util.List;

/**
 * The entity class for couriers.
 * 
 * @author  Wuyi Chen
 * @date    05/16/2019
 * @version 1.0
 * @since   1.0
 */
@Entity
@Access(AccessType.FIELD)
public class Courier {
	@Id
	private long id;

	@Embedded
	private Plan plan;

	private Boolean available;

	private Courier() {
	}

	public Courier(long courierId) {
		this.id = courierId;
		this.plan = new Plan();
	}

	public static Courier create(long courierId) {
		return new Courier(courierId);
	}

	public void noteAvailable() {
		this.available = true;

	}

	public void addAction(Action action) {
		plan.add(action);
	}

	public void cancelDelivery(long deliveryId) {
		plan.removeDelivery(deliveryId);
	}

	public boolean isAvailable() {
		return available;
	}

	public Plan getPlan() {
		return plan;
	}

	public long getId() {
		return id;
	}

	public void noteUnavailable() {
		this.available = false;
	}

	public List<Action> actionsForDelivery(long deliveryId) {
		return plan.actionsForDelivery(deliveryId);
	}
}
