package abdala.client.client.service;

import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
public class AbstractApi {

    private final String resourceHost;
    private final WebClient webClient;

    public AbstractApi(WebClient webClient, String host) {
        this.webClient = webClient;
        this.resourceHost = host;
    }

}
