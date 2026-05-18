package abdala.client.client.service;

import abdala.client.client.dto.DTOCreateProduct;
import abdala.client.client.dto.DTOProduct;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

public class ProductApi extends AbstractApi {

    private final String path = "/products";

    public ProductApi(WebClient webClient, String host) {
        super(webClient, host);
    }

    public String getProducts(int page, int size) {
        return this.getWebClient()
                .get()
                .uri(
                        org.springframework.web.util.UriComponentsBuilder
                                .fromUriString(this.getResourceHost())
                                .path(this.path)
                                .queryParam("page", page)
                                .queryParam("size", size)
                                .build()
                                .toUri()
                ).retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getProduct(Long id) {
        return this.getWebClient()
                .get()
                .uri(
                        org.springframework.web.util.UriComponentsBuilder
                                .fromUriString(this.getResourceHost())
                                .path(this.path + "/" + id)
                                .buildAndExpand()
                                .toUri()
                ).retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public ResponseEntity<String> createProduct(DTOCreateProduct product) {
        return this.getWebClient()
                .post()
                .uri(
                        org.springframework.web.util.UriComponentsBuilder
                                .fromUriString(this.getResourceHost())
                                .path(this.path)
                                .buildAndExpand()
                                .toUri()
                ).bodyValue(product)
                .exchangeToMono(response -> {
                    if (response.statusCode().isError()) {
                        return response.bodyToMono(String.class)
                                .map(body -> ResponseEntity
                                        .status(response.statusCode())
                                        .body(body));
                    }
                    return response.toEntity(String.class);
                }).block();
    }

    public ResponseEntity<String> editProduct(DTOProduct product) {
        return this.getWebClient()
                .put()
                .uri(
                        org.springframework.web.util.UriComponentsBuilder
                                .fromUriString(this.getResourceHost())
                                .path(this.path)
                                .buildAndExpand()
                                .toUri()
                ).bodyValue(product)
                .exchangeToMono(response -> {
                    if (response.statusCode().isError()) {
                        return response.bodyToMono(String.class)
                                .map(body -> ResponseEntity
                                        .status(response.statusCode())
                                        .body(body));
                    }
                    return response.toEntity(String.class);
                }).block();
    }

    public ResponseEntity<String> deleteProduct(Long id) {
        return this.getWebClient()
                .delete()
                .uri(
                        org.springframework.web.util.UriComponentsBuilder
                                .fromUriString(this.getResourceHost())
                                .path(this.path + "/" + id)
                                .buildAndExpand()
                                .toUri()
                )
                .exchangeToMono(response -> {
                    if (response.statusCode().isError()) {
                        return response.bodyToMono(String.class)
                                .map(body -> ResponseEntity
                                        .status(response.statusCode())
                                        .body(body));
                    }
                    return response.toEntity(String.class);
                }).block();
    }
}
