package br.com.caelum.eatsdistanciaservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Restaurante {

	@Id
	private Long id;

	@NotBlank @Size(max=9)
	private String cep;
	
	private Long tipoDeCozinhaId;
}
