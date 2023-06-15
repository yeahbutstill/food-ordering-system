package com.yeahbutstill.food.ordering.system.order.service.domain.entity;

import com.yeahbutstill.food.ordering.system.domain.entity.AggregateRoot;
import com.yeahbutstill.food.ordering.system.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {
    public Customer() {
    }

    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }
}
