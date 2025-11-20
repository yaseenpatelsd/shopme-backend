package shop.me.back.end.Specification;

import shop.me.back.end.Entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class Product {

    public static Specification<ProductEntity> findByName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
    public static Specification<ProductEntity> findByIsActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            if (isActive == null) {
                return criteriaBuilder.conjunction(); // If isActive is null, we don't apply a filter
            }
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }


    public static Specification<ProductEntity> findByBetweenPrice(Double minPrice, Double maxPrice) {
        return (root, query, cb) -> {

            if (minPrice == null && maxPrice == null) return null;

            if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("price"), minPrice, maxPrice);
            }

            if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            }

            return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }
}
