package shop.me.back.end.Dto;


import shop.me.back.end.Enums.DeliveryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusChangeDto {
    @NotNull
    private Long orderID;
    @NotNull
    private Long productId;
    @NotNull
    private DeliveryStatus deliveryStatus;
}
