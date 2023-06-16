package com.yeahbutstill.food.ordering.system.order.service.dataaccess.order.entity;

import com.yeahbutstill.food.ordering.system.order.service.domain.entity.Order;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Builder
// ini diharuskan menggunakan pola builder
@AllArgsConstructor
// perhatikan bahwa ini diperlukan, karena spring akan membutuhkan konstructor tanpa argumen untuk
// membuat objek proxy dari kelas ini
@NoArgsConstructor
public class OrderItemEntityId implements Serializable {

    private Long id;
    private OrderEntity order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItemEntityId that)) return false;

        if (!getId().equals(that.getId())) return false;
        return getOrder().equals(that.getOrder());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getOrder().hashCode();
        return result;
    }
}
