package com.yeahbutstill.food.ordering.system.order.service.dataaccess.order.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
// ini diharuskan menggunakan pola builder
@AllArgsConstructor
// perhatikan bahwa ini diperlukan, karena spring akan membutuhkan konstructor tanpa argumen untuk
// membuat objek proxy dari kelas ini
@NoArgsConstructor
// ini diperlukan untuk menggunakan class id dalam entity dengan primary key multi column
@IdClass(OrderItemEntityId.class)
@Table(name = "order_items")
@Entity
public class OrderItemEntity {

    @Id
    private Long id;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    private UUID productId;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subTotal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItemEntity that)) return false;

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
