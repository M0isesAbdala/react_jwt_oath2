package abdala.resource.resource.controllers;

import abdala.resource.resource.dto.DTOCreateProduct;
import abdala.resource.resource.entities.Product;
import abdala.resource.resource.erros.interfaces.CustomResourceErrorProductApi;
import abdala.resource.resource.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CustomResourceErrorProductApi
@RestController
@RequestMapping("/products")
@SecurityRequirement(name = "oauth2")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public Page<Product> listProduct(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return productRepository.findAll(pageable);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Require scope admin", description = "🔒 Requer scope: ADMIN")
    @SecurityRequirement(name = "oauth2", scopes = {"ADMIN"})
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Product create(@RequestBody DTOCreateProduct product) {
        Product p = new Product(null, product.name(), product.price(), product.barcode());
        return productRepository.save(p);
    }

    @Operation(summary = "Require scope admin", description = "🔒 Requer scope: ADMIN")
    @SecurityRequirement(name = "oauth2", scopes = {"ADMIN"})
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Product edit(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @Operation(summary = "Require scope admin", description = "🔒 Requer scope: ADMIN")
    @SecurityRequirement(name = "oauth2", scopes = {"ADMIN"})
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}