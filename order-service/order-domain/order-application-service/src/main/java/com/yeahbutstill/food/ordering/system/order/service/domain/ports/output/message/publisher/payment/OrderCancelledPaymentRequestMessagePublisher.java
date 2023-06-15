package com.yeahbutstill.food.ordering.system.order.service.domain.ports.output.message.publisher.payment;

import com.yeahbutstill.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.yeahbutstill.food.ordering.system.order.service.domain.event.OrderCancelledEvent;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {
}
