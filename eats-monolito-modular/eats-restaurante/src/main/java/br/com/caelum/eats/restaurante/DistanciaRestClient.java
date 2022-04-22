package br.com.caelum.eats.restaurante;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class DistanciaRestClient {

    private final RestTemplate restTemplate;
    private final String distanciaServiceUrl;

    public DistanciaRestClient(RestTemplate restTemplate, @Value("${configuracao.distancia.service.url}") String distanciaServiceUrl) {
        this.restTemplate = restTemplate;
        this.distanciaServiceUrl = distanciaServiceUrl;
    }

    void novoRestauranteAprovado(Restaurante restaurante) {
        RestauranteParaServicoDeDistancia restauranteParaDistancia = new RestauranteParaServicoDeDistancia(restaurante);

        String url = distanciaServiceUrl + "/restaurantes";
        ResponseEntity<RestauranteParaServicoDeDistancia> responseEntity = restTemplate.postForEntity(url, restauranteParaDistancia, RestauranteParaServicoDeDistancia.class);
        HttpStatus statusCode = responseEntity.getStatusCode();

        if (!HttpStatus.CREATED.equals(statusCode)) {
            throw new RuntimeException("Status diferente do esperado: " + statusCode);
        }
    }

    // tenta no máximo 5 vezes
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 2000, multiplier = 2, random = true))
    void restauranteAtualizado(Restaurante restaurante) {
        log.info("Monólito tentando chamar distancia-service");
        RestauranteParaServicoDeDistancia restauranteParaDistancia = new RestauranteParaServicoDeDistancia(restaurante);
        String url = distanciaServiceUrl + "/restaurantes/" + restaurante.getId();
        restTemplate.put(url, restauranteParaDistancia, RestauranteParaServicoDeDistancia.class);
    }

}
