package com.yeahbutstill.food.ordering.system.order.service.domain.ports.input.service;

import com.yeahbutstill.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.yeahbutstill.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.yeahbutstill.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.yeahbutstill.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;

import javax.validation.Valid;

public interface OrderApplicationService {

    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
