package com.yeahbutstill.food.ordering.system.order.service.dataaccess.restaurant.entity;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantEntityId implements Serializable {

    private UUID restaurantId;
    private UUID productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantEntityId that)) return false;

        if (!getRestaurantId().equals(that.getRestaurantId())) return false;
        return getProductId().equals(that.getProductId());
    }

    @Override
    public int hashCode() {
        int result = getRestaurantId().hashCode();
        result = 31 * result + getProductId().hashCode();
        return result;
    }

}
