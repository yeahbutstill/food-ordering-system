package com.yeahbutstill.food.ordering.system.order.service.dataaccess.restaurant.mapper;

import com.yeahbutstill.food.ordering.system.domain.valueobject.Money;
import com.yeahbutstill.food.ordering.system.domain.valueobject.ProductId;
import com.yeahbutstill.food.ordering.system.domain.valueobject.RestaurantId;
import com.yeahbutstill.food.ordering.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.yeahbutstill.food.ordering.system.order.service.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.yeahbutstill.food.ordering.system.order.service.domain.entity.Product;
import com.yeahbutstill.food.ordering.system.order.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestauran(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity = restaurantEntities.stream().findFirst().orElseThrow(() ->
                new RestaurantDataAccessException("Restaurant could not be found!"));
        List<Product> restaurantProducts = restaurantEntities.stream().map(entity ->
                new Product(new ProductId(entity.getProductId()), entity.getProductName(),
                        new Money(entity.getProductPrice()))).toList();

        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(restaurantProducts)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }

}

