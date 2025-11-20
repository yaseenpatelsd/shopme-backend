package shop.me.back.end.Repository;

import shop.me.back.end.Entity.PurchaseDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseDetailsRepository extends JpaRepository<PurchaseDetailsEntity,Long> {

    Optional<PurchaseDetailsEntity> findByOrders_Id(Long id);
}
