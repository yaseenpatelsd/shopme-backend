package shop.me.back.end.Repository;

import shop.me.back.end.Entity.PersonalDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPersonalDetailsRepository extends JpaRepository<PersonalDetailsEntity,Long> {
    Optional<PersonalDetailsEntity> findByUser_Username(String username);
    Boolean existsByUser_Username(String username);
}
