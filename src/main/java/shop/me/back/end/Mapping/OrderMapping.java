package shop.me.back.end.Mapping;

import org.springframework.stereotype.Component;
import shop.me.back.end.Dto.OrderResponseDto;
import shop.me.back.end.Entity.OrdersEntity;

@Component
public class OrderMapping {

     public OrderResponseDto toDto(OrdersEntity entity) {
          if (entity == null) return null;

          OrderResponseDto dto = new OrderResponseDto();

          dto.setId(entity.getId());
          dto.setOrderNumber(entity.getOrderNumber());
          dto.setTotalAmount(entity.getTotalAmount());

          // Convert entity.quantity (Double) → DTO.quantity (Long)
          if (entity.getQuantity() != null) {
               dto.setQuantity(entity.getQuantity().longValue());
          }

          dto.setRazorPayId(entity.getRazorPayId());

          // Convert PaymentStatus enum → string
          if (entity.getPaymentStatus() != null) {
               dto.setPaymentStatus(entity.getPaymentStatus().name());
          }

          // Delivery status
          dto.setStatus(entity.getStatus());

          return dto;
     }


}
