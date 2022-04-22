package br.com.caelum.apigateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RestauranteComDistanciaController {

    private final RestauranteRestClient restauranteRestClient;
    private final DistanciaRestClient distanciaRestClient;

    public RestauranteComDistanciaController(RestauranteRestClient restauranteRestClient, DistanciaRestClient distanciaRestClient) {
        this.restauranteRestClient = restauranteRestClient;
        this.distanciaRestClient = distanciaRestClient;
    }

    @GetMapping("/restaurantes-com-distancia/{cep}/restaurante/{restauranteId}")
    public Map<String, Object> porCepEIdComDistancia(@PathVariable("cep") String cep,
                                                     @PathVariable("restauranteId") Long restauranteId) {

        Map<String, Object> dadosRestaurante = restauranteRestClient.porId(restauranteId);
        Map<String, Object> dadosDistancia = distanciaRestClient.porCepId(cep, restauranteId);
        dadosRestaurante.putAll(dadosDistancia);
        return dadosRestaurante;
    }
}
