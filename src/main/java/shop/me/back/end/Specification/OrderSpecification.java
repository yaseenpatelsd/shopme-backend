package shop.me.back.end.Specification;

import shop.me.back.end.Entity.OrdersEntity;
import shop.me.back.end.Enums.DeliveryStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class OrderSpecification {

    public static Specification<OrdersEntity> findByStatus(DeliveryStatus status){
        return (root, query, criteriaBuilder) -> {
            if (status==null)return null;
           return criteriaBuilder.equal(root.get("status"),status);
        };
    }

    public static Specification<OrdersEntity> findBetweenPrice(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) {
                return null;
            } else if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("totalAmount"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("totalAmount"), minPrice);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("totalAmount"), maxPrice);
            }
        };

    }
}
