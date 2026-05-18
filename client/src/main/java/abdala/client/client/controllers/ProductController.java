package abdala.client.client.controllers;

import abdala.client.client.dto.DTOCreateProduct;
import abdala.client.client.dto.DTOProduct;
import abdala.client.client.service.ApiService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ApiService apiService;

    public ProductController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String listProduct(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return apiService.getProductApi().getProducts(page, size);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getProduct(@PathVariable Long id) {
        return apiService.getProductApi().getProduct(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createProducts(@RequestBody DTOCreateProduct product) {
        return apiService.getProductApi().createProduct(product);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editProducts(@RequestBody DTOProduct product) {
        return apiService.getProductApi().editProduct(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return apiService.getProductApi().deleteProduct(id);
    }
}