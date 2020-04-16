package com.ftgo.orderservice.api.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.ftgo.common.model.Money;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class OrderLineItem {
	private int quantity;
	private String menuItemId;
	private String name;

	@Embedded
	@AttributeOverrides(@AttributeOverride(name = "amount", column = @Column(name = "price")))
	private Money price;

	public OrderLineItem() {
	}
	
	public OrderLineItem(String menuItemId, String name, Money price,
			int quantity) {
		this.menuItemId = menuItemId;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public Money deltaForChangedQuantity(int newQuantity) {
		return price.multiply(newQuantity - quantity);
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setMenuItemId(String menuItemId) {
		this.menuItemId = menuItemId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(Money price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getMenuItemId() {
		return menuItemId;
	}

	public String getName() {
		return name;
	}

	public Money getPrice() {
		return price;
	}

	public Money getTotal() {
		return price.multiply(quantity);
	}

}