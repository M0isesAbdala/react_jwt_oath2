package abdala.client.client.service;

import org.springframework.web.reactive.function.client.WebClient;

public class ResourceApi extends AbstractApi {

    public ResourceApi(WebClient webClient, String host) {
        super(webClient, host);
    }

    public String getRoles() {
        return this.getWebClient()
                .get()
                .uri(
                        org.springframework.web.util.UriComponentsBuilder
                                .fromUriString(this.getResourceHost())
                                .path("/roles")
                                .build()
                                .toUri()
                ).retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
