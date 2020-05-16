package com.ftgo.deliveryservice.repository;

import org.springframework.data.repository.CrudRepository;

import com.ftgo.deliveryservice.model.Delivery;

/**
 * The repository class for {@code Delivery} entity. 
 * 
 * @author  Wuyi Chen
 * @date    05/16/2020
 * @version 1.0
 * @since   1.0
 */
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {
}
