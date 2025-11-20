package shop.me.back.end.Repository;

import shop.me.back.end.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity,Long>, JpaSpecificationExecutor<ProductEntity> {

   List<ProductEntity> findByUser_Username(String username);
   List<ProductEntity> findByName(String name);
}
