package com.yeahbutstill.food.ordering.system.order.service.application.rest;

import com.yeahbutstill.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.yeahbutstill.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.yeahbutstill.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.yeahbutstill.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.yeahbutstill.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping()
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderCommand createOrderCommand) {
        log.info("Create order for customer: {} at restaurant: {}", createOrderCommand.getCustomerId(),
                createOrderCommand.getRestaurantId());
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        log.info("Order create with tracking id: {}", createOrderResponse.getOrderTackingId());

        return ResponseEntity.ok(createOrderResponse);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponse> getOrderByTrackingId(@PathVariable UUID trackingId) {
        TrackOrderResponse trackOrderResponse = orderApplicationService.trackOrder(TrackOrderQuery.builder()
                .orderTrackingId(trackingId)
                .build());
        log.info("Returning order status with tracking id: {}", trackOrderResponse.getOrderTrackingId());

        return ResponseEntity.ok(trackOrderResponse);
    }

}
