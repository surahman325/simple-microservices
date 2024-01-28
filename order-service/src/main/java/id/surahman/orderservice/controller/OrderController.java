package id.surahman.orderservice.controller;

import id.surahman.orderservice.dto.GeneralResponse;
import id.surahman.orderservice.dto.request.OrderRequest;
import id.surahman.orderservice.dto.response.OrderResponse;
import id.surahman.orderservice.repository.OrderRepository;
import id.surahman.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService service;
    private final OrderRepository repository;

    @PostMapping
    @CircuitBreaker(name = "inventoryCheck", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventoryCheck")
    @Retry(name = "inventoryCheck")
    public CompletableFuture<ResponseEntity<GeneralResponse>> createOrder(@RequestBody OrderRequest request){
        return CompletableFuture.supplyAsync(()-> {
            OrderResponse data = service.save(request);
            return ResponseEntity.ok(new GeneralResponse(HttpStatus.OK.value(), "success", data));
        });
    }
    public CompletableFuture<ResponseEntity<GeneralResponse>> fallbackMethod(OrderRequest request, RuntimeException exception){
        log.error(exception.getMessage());
        return CompletableFuture.supplyAsync(()->ResponseEntity.badRequest().body(new GeneralResponse(400, "Something went wrong, please try again later!", null)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getOrderById(@PathVariable Long id){
        OrderResponse order = service.getById(id);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GeneralResponse(HttpStatus.NOT_FOUND.value(), "Success", null));
        }
        return ResponseEntity.ok(new GeneralResponse(HttpStatus.OK.value(), "Success", order));
    }
}
