package shop.me.back.end.Dto;


import shop.me.back.end.Enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestPaymentStatusChangeDto {
    private Long id;
    private Long productId;
    private PaymentStatus status;
}
