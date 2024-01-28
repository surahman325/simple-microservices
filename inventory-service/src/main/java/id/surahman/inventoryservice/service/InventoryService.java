package id.surahman.inventoryservice.service;

import id.surahman.inventoryservice.dto.StockResponse;
import id.surahman.inventoryservice.entity.Inventory;
import id.surahman.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository repository;

    @Transactional(readOnly = true)
    public List<StockResponse> isReadyStock(List<String> code){

        // for test time limiter and retry
//        log.info("Start check stock");
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        log.info("End check stock");
        // for test time limiter and retry

        List<Inventory> inventories = repository.findByCodeIn(code);
        return inventories.stream().map(inventory -> StockResponse.builder().code(inventory.getCode()).isStockReady(inventory.getStock() > 0).build()).toList();
    }
}
