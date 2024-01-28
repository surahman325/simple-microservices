package id.surahman.productservice.controller;

import id.surahman.productservice.dto.GeneralResponse;
import id.surahman.productservice.dto.request.ProductRequest;
import id.surahman.productservice.dto.response.ProductResponse;
import id.surahman.productservice.repository.ProductRepository;
import id.surahman.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    private final ProductRepository repository;

    @PostMapping
    public ResponseEntity<GeneralResponse> create(@RequestBody ProductRequest request){
        ProductResponse product = service.save(request);
        HttpStatus status = HttpStatus.CREATED;
        GeneralResponse res = new GeneralResponse(status.value(), "Product created successfully", product);

        return ResponseEntity.status(status).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> update(@RequestBody ProductRequest request, @PathVariable String id){
        GeneralResponse res = new GeneralResponse();
        try {
            ProductResponse product = service.getById(id);
            HttpStatus status;
            if (product != null) {
                status = HttpStatus.OK;
                res.setMessage("Success");
                ProductResponse saved = service.update(request, id);
                res.setData(saved);
            }else {
                status = HttpStatus.NOT_FOUND;
                res.setMessage("Product not found");
            }

            res.setStatus(status.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }catch (Exception e){
            res.setStatus(HttpStatus.BAD_REQUEST.value());
            res.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(res);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id){
        GeneralResponse res = new GeneralResponse();
        ProductResponse product = service.getById(id);
        HttpStatus status;
        if (product != null) {
            status = HttpStatus.OK;
            res.setMessage("Success");
            res.setData(product);
        }else {
            status = HttpStatus.NOT_FOUND;
            res.setMessage("Product not found");
        }

        res.setStatus(status.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllProduct(){
        List<ProductResponse> data = service.getAll();
        GeneralResponse response = new GeneralResponse(HttpStatus.OK.value(), "Success", data);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> delete(@PathVariable String id){
        GeneralResponse res = new GeneralResponse();
        HttpStatus status;
        try {
            repository.deleteById(id);
            res.setMessage("Success");
            status = HttpStatus.OK;
        }catch (Exception e){
            res.setMessage(e.getMessage());
            status = HttpStatus.BAD_REQUEST;
        }
        res.setStatus(status.value());

        return ResponseEntity.status(status).body(res);
    }
}
