package shop.me.back.end.Repository;

import shop.me.back.end.Entity.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository extends JpaRepository<OrdersEntity,Long>, JpaSpecificationExecutor<OrdersEntity> {

}
