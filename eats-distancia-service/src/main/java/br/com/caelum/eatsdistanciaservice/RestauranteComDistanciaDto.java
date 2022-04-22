package br.com.caelum.eatsdistanciaservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
class RestauranteComDistanciaDto {

	private Long restauranteId;

	private BigDecimal distancia;

}
