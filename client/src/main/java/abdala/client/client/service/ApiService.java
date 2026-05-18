package abdala.client.client.service;

import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Getter
public class ApiService {

    private final ProductApi productApi;
    private final UserApi userApi;
    private final ResourceApi resourceApi;

    public ApiService(WebClient webClient, Environment env) {
        String host = env.getProperty("GATEWAY_URL") + ":" + env.getProperty("GATEWAY_PORT") + "/" + env.getProperty("RESOURCE_NAME");
        productApi = new ProductApi(webClient, host);
        userApi = new UserApi(webClient, host);
        resourceApi = new ResourceApi(webClient, host);
    }

}