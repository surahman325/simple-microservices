package id.surahman.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.surahman.orderservice.dto.StockResponse;
import id.surahman.orderservice.dto.request.OrderRequest;
import id.surahman.orderservice.dto.response.OrderResponse;
import id.surahman.orderservice.entity.Order;
import id.surahman.orderservice.entity.OrderItem;
import id.surahman.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository repository;
    public final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, Order> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderResponse save(OrderRequest request) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderItems(request.getOrderItems());

        List<String> codes = request.getOrderItems().stream().map(OrderItem::getCode).toList();

        // check stock
        StockResponse[] responses = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("codes", codes).build())
                .retrieve()
                .bodyToMono(StockResponse[].class)
                .block();
        if (responses != null && responses.length > 0){
            boolean isAllReady = Arrays.stream(responses).allMatch(StockResponse::getIsStockReady);
            if (isAllReady){
                Order saved = repository.save(order);
                kafkaTemplate.send("orderPlaced", saved);
                return mapToResponse(saved);
            }else{
                throw new RuntimeException("Product is out of stock");
            }
        }
        throw new RuntimeException("Failed get stock or stock data does not exist");
    }

    public OrderResponse getById(Long id) {
        OrderResponse response = null;
        Optional<Order> order = repository.findById(id);
        if (order.isPresent()) {
            response = mapToResponse(order.get());
        }
        return response;
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder().id(order.getId()).orderNumber(order.getOrderNumber()).orderItemList(order.getOrderItems()).build();
    }
}
