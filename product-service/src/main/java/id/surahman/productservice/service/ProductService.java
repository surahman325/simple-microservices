package id.surahman.productservice.service;

import id.surahman.productservice.dto.ProductAddedEvent;
import id.surahman.productservice.dto.request.ProductRequest;
import id.surahman.productservice.dto.response.ProductResponse;
import id.surahman.productservice.entity.Product;
import id.surahman.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository repository;
    private final KafkaTemplate<String, ProductAddedEvent> kafkaTemplate;

    public ProductResponse save(ProductRequest request){
        Product product = Product.builder().name(request.getName()).description(request.getDescription()).price(request.getPrice()).build();
        log.info("Save product with data {}", product);
        Product saved = repository.save(product);
        kafkaTemplate.send("productAdded", new ProductAddedEvent(saved.getId(), request.getQty()));
        return mapToProductResponse(saved);
    }

    public ProductResponse update(ProductRequest request, String id){
        Product product = Product.builder().id(id).name(request.getName()).description(request.getDescription()).price(request.getPrice()).build();
        log.info("Update product with data {}", product);
        Product saved = repository.save(product);
        return mapToProductResponse(product);
    }
    public ProductResponse getById(String id){
        Optional<Product> product = repository.findById(id);
        return product.map(this::mapToProductResponse).orElse(null);
    }

    public List<ProductResponse> getAll(){
        return repository.findAll().stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product){
        return ProductResponse.builder().id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
