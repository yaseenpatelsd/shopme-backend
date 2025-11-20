package shop.me.back.end.Dto;


import shop.me.back.end.Enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private Long id;
    private String orderNumber;
    private Double totalAmount;
    private Long quantity;
    private String razorPayId;
    private String paymentStatus;
    private DeliveryStatus status;

}
