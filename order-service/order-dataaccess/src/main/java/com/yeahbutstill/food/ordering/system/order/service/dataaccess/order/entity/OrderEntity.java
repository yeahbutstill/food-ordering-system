package com.yeahbutstill.food.ordering.system.order.service.dataaccess.order.entity;

import com.yeahbutstill.food.ordering.system.domain.valueobject.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
// ini diharuskan menggunakan pola builder
@AllArgsConstructor
// perhatikan bahwa ini diperlukan, karena spring akan membutuhkan konstructor tanpa argumen untuk
// membuat objek proxy dari kelas ini
@NoArgsConstructor
@Table(name = "orders")
@Entity
public class OrderEntity {

    @Id // primary key
    private UUID id;

    private UUID customerId;
    private UUID restaurantId;
    private UUID trackingId;
    private BigDecimal price;

    // nah ini value object
    @Enumerated(EnumType.STRING) // ini akan memungkinkan saya untuk menggunakan tipe enum java di field ini
    private OrderStatus orderStatus;

    private String failureMessages;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL) // why ALL? ketika saya menghapus order_address, maka order juga akan dihapus
    private OrderAddressEntity address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderEntity that)) return false;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
