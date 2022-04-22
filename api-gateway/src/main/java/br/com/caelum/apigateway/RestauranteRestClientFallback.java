package br.com.caelum.apigateway;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RestauranteRestClientFallback implements RestauranteRestClient {

    @Override
    public Map<String, Object> porId(Long id) {
        return Map.of("id", id);
    }
}
