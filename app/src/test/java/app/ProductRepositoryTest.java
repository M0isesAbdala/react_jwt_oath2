package app;

import app.entities.Product;
import app.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductRepositoryTest extends AbstractProfileTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void createProduct(){
        String productName = "test";
        String productBarcode = "1234567891234";
        Double productPrice = 5.5;

        Product product = new Product();
        product.setName(productName);
        product.setBarcode(productBarcode);
        product.setPrice(productPrice);

        productRepository.save(product);

        Optional<Product> result = productRepository.findById(product.getId());

        assertTrue(result.isPresent());
        assertEquals(productName, result.get().getName());
        assertEquals(productBarcode, result.get().getBarcode());
        assertEquals(productPrice, result.get().getPrice());

        long productId = product.getId();

        Product newProduct = new Product();
        newProduct.setName(productName);
        newProduct.setBarcode(productBarcode);
        newProduct.setPrice(productPrice);

        assertThrows(Exception.class, () -> {
            productRepository.save(newProduct);
        });

        newProduct.setBarcode("1");

        assertThrows(Exception.class, () -> {
            productRepository.save(newProduct);
        });

        removeProduct(productId);
    }

    void removeProduct(Long id){
        productRepository.deleteById(id);
    }
}
