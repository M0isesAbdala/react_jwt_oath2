package abdala.client.client.service;

import abdala.client.client.dto.DTOCreateUser;
import abdala.client.client.dto.DTOEditUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

public class UserApi extends AbstractApi {

    private final String path = "/users";

    public UserApi(WebClient webClient, String host) {
        super(webClient, host);
    }

    public String getUsers(int page, int size) {
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

    public String getUser(Long id) {
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

    public ResponseEntity<String> createUser(DTOCreateUser user) {
        return this.getWebClient()
                .post()
                .uri(
                        org.springframework.web.util.UriComponentsBuilder
                                .fromUriString(this.getResourceHost())
                                .path(this.path)
                                .buildAndExpand()
                                .toUri()
                ).bodyValue(user)
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

    public ResponseEntity<String> editUser(DTOEditUser user) {
        return this.getWebClient()
                .put()
                .uri(
                        org.springframework.web.util.UriComponentsBuilder
                                .fromUriString(this.getResourceHost())
                                .path(this.path)
                                .buildAndExpand()
                                .toUri()
                ).bodyValue(user)
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

    public ResponseEntity<String> deleteUser(Long id) {
        return this.getWebClient()
                .delete()
                .uri(
                        UriComponentsBuilder
                                .fromUriString(this.getResourceHost())
                                .path(this.path + "/" + id)
                                .buildAndExpand()
                                .toUri()
                )
                .exchangeToMono(response -> response.toEntity(String.class))
                .block();
    }

}
