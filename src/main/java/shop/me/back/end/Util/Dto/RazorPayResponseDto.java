package shop.me.back.end.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RazorPayResponseDto {
    private Long id;
    private String amount;
    private String currency;
    private String status;

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
}
