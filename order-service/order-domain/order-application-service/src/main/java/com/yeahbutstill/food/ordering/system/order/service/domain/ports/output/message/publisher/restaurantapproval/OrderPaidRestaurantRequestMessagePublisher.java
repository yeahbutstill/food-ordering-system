package com.yeahbutstill.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval;

import com.yeahbutstill.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.yeahbutstill.food.ordering.system.order.service.domain.event.OrderPaidEvent;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
