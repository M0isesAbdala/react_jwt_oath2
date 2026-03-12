package app;

import app.entities.Product;
import app.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTestApi extends AbstractProfileTest {

    @MockitoBean
    private ProductRepository productRepository;

    @Autowired
    ComponentsApiTest componentsApiTest;
    
    private final Product product = new Product(1L, "test", 5.5, "1231231231231");

    @Test
    void createProduct() throws Exception {
        
       componentsApiTest.getMockMvc().perform(post("/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + componentsApiTest.getJwtFactoryTest().getAdminToken())
                        .content(componentsApiTest.getObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk())
                .andReturn();

       componentsApiTest.getMockMvc().perform(post("/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + componentsApiTest.getJwtFactoryTest().getUserToken())
                        .content(componentsApiTest.getObjectMapper().writeValueAsString(product)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void getProduct() throws Exception{

        Mockito.when(productRepository.findAll()).thenReturn(List.of(product));

       componentsApiTest.getMockMvc().perform(get("/products")
                        .with(csrf())
                        .header("Authorization", "Bearer " + componentsApiTest.getJwtFactoryTest().getAdminToken())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0]id").value(product.getId()))
                .andExpect(jsonPath("$[0]name").value(product.getName()))
                .andExpect(jsonPath("$[0]barcode").value(product.getBarcode()))
                .andExpect(jsonPath("$[0]price").value(product.getPrice()))
                .andReturn();

    }

    @Test
    void deleteProduct() throws Exception {

       componentsApiTest.getMockMvc().perform(delete("/products/"+product.getId())
                        .with(csrf())
                        .header("Authorization", "Bearer " + componentsApiTest.getJwtFactoryTest().getAdminToken())
                ).andExpect(status().isOk())
                .andReturn();

       componentsApiTest.getMockMvc().perform(delete("/products/"+product.getId())
                        .with(csrf())
                        .header("Authorization", "Bearer " + componentsApiTest.getJwtFactoryTest().getUserToken())
                ).andExpect(status().isForbidden())
                .andReturn();
    }
}
