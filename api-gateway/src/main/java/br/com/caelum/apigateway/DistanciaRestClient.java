package br.com.caelum.apigateway;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class DistanciaRestClient {

    private final RestTemplate restTemplate;
    private final String distanciaServiceUrl;

    DistanciaRestClient(RestTemplate restTemplate, @Value("${configuracao.distancia.service.url}") String distanciaServiceUrl) {
        this.restTemplate = restTemplate;
        this.distanciaServiceUrl = distanciaServiceUrl;
    }

    // fallbackMethod será chamado como fallback (quando der erro de timeout)
    @HystrixCommand(fallbackMethod = "restauranteSemDistanciaNemDetalhes")
    Map<String, Object> porCepId(String cep, Long restauranteId) {
       String url = distanciaServiceUrl + "/restaurantes/" + cep + "/restaurante/" + restauranteId;
       return restTemplate.getForObject(url, Map.class);
    }

    Map<String, Object> restauranteSemDistanciaNemDetalhes(String cep, Long restauranteId) {
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("restauranteId", restauranteId);
        resultado.put("cep", cep);
        return resultado;
    }

}
