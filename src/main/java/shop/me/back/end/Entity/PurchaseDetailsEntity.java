package shop.me.back.end.Entity;

import lombok.Builder;
import shop.me.back.end.Enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "purchase_details")
@Builder
public class PurchaseDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String fullname;
    @Column(nullable = false)
    private String orderNumber;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = true)
    private LocalDateTime orderDate;
    @Column(nullable = true)
    private LocalDateTime statusUpDateDate;
    @Column(nullable = false)
    private Double quantity;
    @Column(nullable = false)
    private Double pricePerProduct;
    @Column(nullable = false)
    private Double totalAmount;
    @Column(nullable = false)
    private String address;
    @Column(nullable = true)
    private DeliveryStatus deliveryStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrdersEntity orders;
}
