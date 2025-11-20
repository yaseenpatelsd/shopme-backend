package shop.me.back.end.Mapping;

import org.springframework.stereotype.Component;
import shop.me.back.end.Dto.PurchaseEmailDto;
import shop.me.back.end.Entity.PurchaseDetailsEntity;

@Component
public class PurchaseDetailsMapping {

    // ENTITY → DTO
    public PurchaseEmailDto toDto(PurchaseDetailsEntity entity) {
        if (entity == null) return null;

        PurchaseEmailDto dto = new PurchaseEmailDto();

        dto.setFullname(entity.getFullname());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setProductName(entity.getProductName());
        dto.setOrderDate(entity.getOrderDate());
        dto.setStatusUpDateDate(entity.getStatusUpDateDate());
        dto.setQuantity(entity.getQuantity());
        dto.setPricePerProduct(entity.getPricePerProduct());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setAddress(entity.getAddress());
        dto.setDeliveryStatus(entity.getDeliveryStatus());

        return dto;
    }

    // DTO → ENTITY
    public PurchaseDetailsEntity toEntity(PurchaseEmailDto dto) {
        if (dto == null) return null;

        PurchaseDetailsEntity.PurchaseDetailsEntityBuilder entity =
                PurchaseDetailsEntity.builder();

        entity.fullname(dto.getFullname());
        entity.orderNumber(dto.getOrderNumber());
        entity.productName(dto.getProductName());
        entity.orderDate(dto.getOrderDate());
        entity.statusUpDateDate(dto.getStatusUpDateDate());
        entity.quantity(dto.getQuantity());
        entity.pricePerProduct(dto.getPricePerProduct());
        entity.totalAmount(dto.getTotalAmount());
        entity.address(dto.getAddress());
        entity.deliveryStatus(dto.getDeliveryStatus());

        // ⚠️ NOT SETTING orders because it is a lazy-loaded ManyToOne

        return entity.build();
    }
}
