package id.surahman.inventoryservice.repository;

import id.surahman.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findByCodeIn(Collection<String> codes);

    boolean existsByCode(String code);


}
