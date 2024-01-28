package id.surahman.inventoryservice.controller;

import id.surahman.inventoryservice.dto.StockResponse;
import id.surahman.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService service;

    @GetMapping
    public ResponseEntity<List<StockResponse>> isReadyStock(@RequestParam("codes") List<String> codes){
        return ResponseEntity.ok(service.isReadyStock(codes));
    }
}
