package shop.me.back.end.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelDto {
    private Long orderId;
    private Long productId;
}
