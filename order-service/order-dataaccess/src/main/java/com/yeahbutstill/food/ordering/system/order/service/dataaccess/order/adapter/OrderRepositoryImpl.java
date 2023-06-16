package com.yeahbutstill.food.ordering.system.order.service.dataaccess.order.adapter;

import com.yeahbutstill.food.ordering.system.order.service.dataaccess.order.mapper.OrderDataAccessMapper;
import com.yeahbutstill.food.ordering.system.order.service.dataaccess.order.repository.OrderJpaRepository;
import com.yeahbutstill.food.ordering.system.order.service.domain.entity.Order;
import com.yeahbutstill.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.yeahbutstill.food.ordering.system.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, OrderDataAccessMapper orderDataAccessMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderDataAccessMapper = orderDataAccessMapper;
    }

    /**
     * @param order 
     * @return
     */
    @Override
    public Order save(Order order) {
        return orderDataAccessMapper.orderEntityToOrder(orderJpaRepository
                .save(orderDataAccessMapper.orderToOrderEntity(order)));
    }

    /**
     * @param trackingId 
     * @return
     */
    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJpaRepository.findByTrackingId(trackingId.getValue())
                .map(orderDataAccessMapper::orderEntityToOrder);
    }

}
