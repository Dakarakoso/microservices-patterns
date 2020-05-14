package com.ftgo.orderservice.base;

import io.eventuate.common.json.mapper.JSonMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

import org.junit.Before;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

import com.ftgo.common.domain.CommonJsonMapperInitializer;
import com.ftgo.orderservice.OrderTestData;
import com.ftgo.orderservice.controller.OrderServiceController;
import com.ftgo.orderservice.repository.OrderRepository;
import com.ftgo.orderservice.service.OrderService;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The base class for the setup phase of the HTTP server test.
 * 
 * <p>It creates the controllers injected with mock dependencies and 
 * configures those mocks to return values that cause the controller 
 * to generate the expected response.
 *
 * @author  Wuyi Chen
 * @date    05/11/2020
 * @version 1.0
 * @since   1.0
 */
public abstract class HttpBase {
	private StandaloneMockMvcBuilder controllers(Object... controllers) {
		CommonJsonMapperInitializer.registerMoneyModule();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(JSonMapper.objectMapper);
		return MockMvcBuilders.standaloneSetup(controllers).setMessageConverters(converter);
	}

	@Before
	public void setup() {
		OrderService orderService = mock(OrderService.class);
		OrderRepository orderRepository = mock(OrderRepository.class);
		OrderServiceController orderController = new OrderServiceController(orderService, orderRepository);

		when(orderRepository.findById(OrderTestData.ORDER_ID)).thenReturn(Optional.of(OrderTestData.CHICKEN_VINDALOO_ORDER));
		when(orderRepository.findById(555L)).thenReturn(empty());
		RestAssuredMockMvc.standaloneSetup(controllers(orderController));
	}
}