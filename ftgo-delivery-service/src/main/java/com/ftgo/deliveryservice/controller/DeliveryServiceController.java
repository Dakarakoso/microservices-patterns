package com.ftgo.deliveryservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ftgo.deliveryservice.api.model.CourierAvailability;
import com.ftgo.deliveryservice.api.model.DeliveryStatus;
import com.ftgo.deliveryservice.service.DeliveryService;

/**
 * The controller class for defining the external APIs for the delivery service.
 * 
 * @author  Wuyi Chen
 * @date    05/16/2020
 * @version 1.0
 * @since   1.0
 */
@RestController
public class DeliveryServiceController {
	private DeliveryService deliveryService;

	public DeliveryServiceController(DeliveryService deliveryService) {
		this.deliveryService = deliveryService;
	}

	@RequestMapping(path = "/couriers/{courierId}/availability", method = RequestMethod.POST)
	public ResponseEntity<String> updateCourierLocation(@PathVariable long courierId, @RequestBody CourierAvailability availability) {
		deliveryService.updateAvailability(courierId, availability.isAvailable());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(path = "/deliveries/{deliveryId}", method = RequestMethod.GET)
	public DeliveryStatus getDeliveryStatus(@PathVariable long deliveryId) {
		return deliveryService.getDeliveryInfo(deliveryId);
	}
}
