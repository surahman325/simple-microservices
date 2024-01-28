package id.surahman.productservice.repository;

import id.surahman.productservice.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ProductRepository extends MongoRepository<Product, String> {
}
