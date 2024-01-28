package id.surahman.inventoryservice;

import id.surahman.inventoryservice.dto.ProductAddedEvent;
import id.surahman.inventoryservice.entity.Inventory;
import id.surahman.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceApplication {
	private final InventoryRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@KafkaListener(topics = "productAdded")
	public void handleProductAdded(ProductAddedEvent data){
		// insert into inventory
		log.info("Receive product added with data = {}", data.toString());
		if (!repository.existsByCode(data.getCode())){
			Inventory inventory = new Inventory();
			inventory.setCode(data.getCode());
			inventory.setStock(data.getQty());
			repository.save(inventory);
		}
	}

}
