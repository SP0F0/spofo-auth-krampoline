package net.spofo.auth.config;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

    private final String PROXY_SERVER_HOST = "krmp-proxy.9rum.cc";
    private final Integer PROXY_SERVER_PORT = 3128;

    @Bean
    public RestClient restClient() {
        Proxy proxy = new Proxy(
                Type.HTTP, new InetSocketAddress(PROXY_SERVER_HOST, PROXY_SERVER_PORT));
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(proxy);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return RestClient.builder(restTemplate)
                .requestInterceptor(clientHttpRequestInterceptor())
                .build();
    }

    @Bean
    public ClientHttpRequestInterceptor clientHttpRequestInterceptor() {
        return (request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.setContentType(APPLICATION_JSON);
            return execution.execute(request, body);
        };
    }
}