package br.com.caelum.eatsdistanciaservice;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
class RestaurantesMaisProximosController {

	private DistanciaService distanciaService;

	@GetMapping("/restaurantes/mais-proximos/{cep}")
	List<RestauranteComDistanciaDto> maisProximos(@PathVariable("cep") String cep) {
		return distanciaService.restaurantesMaisProximosAoCep(cep);
	}

	@GetMapping("/restaurantes/mais-proximos/{cep}/tipos-de-cozinha/{tipoDeCozinhaId}")
	List<RestauranteComDistanciaDto> maisProximos(@PathVariable("cep") String cep,
                                                                               @PathVariable("tipoDeCozinhaId") Long tipoDeCozinhaId) {
		List<RestauranteComDistanciaDto> restaurantes = distanciaService.restaurantesDoTipoDeCozinhaMaisProximosAoCep(tipoDeCozinhaId, cep);

		return restaurantes;
	}

	@GetMapping("/restaurantes/{cep}/restaurante/{restauranteId}")
    RestauranteComDistanciaDto comDistanciaPorId(@PathVariable("cep") String cep, @PathVariable("restauranteId") Long restauranteId) {
		RestauranteComDistanciaDto restaurantes = distanciaService.restauranteComDistanciaDoCep(restauranteId, cep);

		return restaurantes;
	}

}
