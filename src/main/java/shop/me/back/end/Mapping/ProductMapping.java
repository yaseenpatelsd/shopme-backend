package shop.me.back.end.Mapping;


import org.springframework.stereotype.Component;
import shop.me.back.end.Dto.ProductDto;
import shop.me.back.end.Entity.ProductEntity;

@Component
public class ProductMapping {
        public ProductDto toDto(ProductEntity entity) {
            if (entity == null) return null;

            ProductDto dto = new ProductDto();
            dto.setName(entity.getName());
            dto.setDescription(entity.getDescription());
            dto.setPrice(entity.getPrice());
            dto.setQuantity(entity.getQuantity());
            dto.setSellerName(entity.getSellerName());
            dto.setSellerAddress(entity.getSellerAddress());
            dto.setImageUrl(entity.getImageUrl());
            return dto;
        }

        public ProductEntity toEntity(ProductDto dto) {
            if (dto == null) return null;

            return ProductEntity.builder()
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .price(dto.getPrice())
                    .quantity(dto.getQuantity())
                    .sellerName(dto.getSellerName())
                    .sellerAddress(dto.getSellerAddress())
                    .imageUrl(dto.getImageUrl())
                    .build();
        }

}

