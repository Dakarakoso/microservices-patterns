package com.ftgo.orderservice.persistent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;

import com.ftgo.orderservice.OrderTestData;
import com.ftgo.orderservice.api.model.OrderState;
import com.ftgo.orderservice.model.Order;
import com.ftgo.orderservice.repository.OrderRepository;

import static com.ftgo.orderservice.OrderTestData.CONSUMER_ID;
import static com.ftgo.orderservice.OrderTestData.chickenVindalooLineItems;
import static com.ftgo.orderservice.RestaurantTestData.AJANTA_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * The integration test that verifies that an Order can be persisted.
 *
 * @author  Wuyi Chen
 * @date    05/11/2020
 * @version 1.0
 * @since   1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderJpaTestConfiguration.class)
public class OrderJpaTest {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private TransactionTemplate transactionTemplate;

	@Test
	public void shouldSaveAndLoadOrder() {
		long orderId = transactionTemplate.execute((ts) -> {
			Order order = new Order(CONSUMER_ID, AJANTA_ID, OrderTestData.DELIVERY_INFORMATION, chickenVindalooLineItems());
			orderRepository.save(order);
			return order.getId();
		});

		transactionTemplate.execute((ts) -> {
			Order order = orderRepository.findById(orderId).get();

			assertNotNull(order);
			assertEquals(OrderState.APPROVAL_PENDING, order.getState());
			assertEquals(AJANTA_ID, order.getRestaurantId());
			assertEquals(CONSUMER_ID, order.getConsumerId().longValue());
			assertEquals(chickenVindalooLineItems(), order.getLineItems());
			return null;
		});
	}
}
