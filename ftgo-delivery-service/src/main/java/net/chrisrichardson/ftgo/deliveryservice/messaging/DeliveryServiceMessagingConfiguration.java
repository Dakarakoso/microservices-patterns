package net.chrisrichardson.ftgo.deliveryservice.messaging;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.events.subscriber.TramEventSubscriberConfiguration;
import net.chrisrichardson.ftgo.deliveryservice.domain.DeliveryService;
import net.chrisrichardson.ftgo.deliveryservice.domain.DeliveryServiceDomainConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.ftgo.common.domain.CommonConfiguration;

@Configuration
@Import({DeliveryServiceDomainConfiguration.class, TramEventSubscriberConfiguration.class, CommonConfiguration.class})
public class DeliveryServiceMessagingConfiguration {

  @Bean
  public DeliveryMessageHandlers deliveryMessageHandlers(DeliveryService deliveryService) {
    return new DeliveryMessageHandlers(deliveryService);
  }

  @Bean
  public DomainEventDispatcher domainEventDispatcher(DeliveryMessageHandlers deliveryMessageHandlers, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("deliveryService-deliveryMessageHandlers", deliveryMessageHandlers.domainEventHandlers());
  }
}
