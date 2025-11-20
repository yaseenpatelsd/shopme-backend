package shop.me.back.end.Dto;


import shop.me.back.end.Enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseEmailDto {
    private String fullname;
    private String orderNumber;
    private String productName;
    private LocalDateTime orderDate;
    private LocalDateTime statusUpDateDate;
    private Double quantity;
    private Double pricePerProduct;
    private Double totalAmount;
    private String address;
    private DeliveryStatus deliveryStatus;
}
