package br.com.caelum.eats.pagamento;


import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.core.ControllerEntityLinks;
import org.springframework.hateoas.server.mvc.ControllerLinkRelationProvider;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/pagamentos")
@AllArgsConstructor
class PagamentoController {

	private PagamentoRepository pagamentoRepo;
	private PedidoRestClient pedidoRestClient;

	@GetMapping("/{id}")
	public EntityModel<PagamentoDto> detalha(@PathVariable("id") Long id) {
		Pagamento pagamento = pagamentoRepo.findById(id).orElseThrow(ResourceNotFoundException::new);

		List<Link> links = new ArrayList<>();

		Link self = linkTo(methodOn(PagamentoController.class).detalha(id)).withSelfRel();
		links.add(self);

		if (Pagamento.Status.CRIADO.equals(pagamento.getStatus())) {
			Link confirma = linkTo(methodOn(PagamentoController.class).confirma(id)).withRel("confirma");
			links.add(new LinkWithMethod(confirma, "PUT"));

			Link cancela = linkTo(methodOn(PagamentoController.class).cancela(id)).withRel("cancela");
			links.add(new LinkWithMethod(cancela, "DELETE"));
		}

		return EntityModel.of(new PagamentoDto(pagamento), links);
	}

	@PostMapping
	ResponseEntity<EntityModel<PagamentoDto>> cria(@RequestBody Pagamento pagamento, UriComponentsBuilder uriBuilder) {
		pagamento.setStatus(Pagamento.Status.CRIADO);
		Pagamento salvo = pagamentoRepo.save(pagamento);
		URI path = uriBuilder.path("/pagamentos/{id}").buildAndExpand(salvo.getId()).toUri();

		PagamentoDto dto = new PagamentoDto(salvo);

		Long id = salvo.getId();

		List<Link> links = new ArrayList<>();

		Link self = linkTo(methodOn(PagamentoController.class).detalha(id)).withSelfRel();
		links.add(self);

		Link confirma = linkTo(methodOn(PagamentoController.class).confirma(id)).withRel("confirma");
		links.add(new LinkWithMethod(confirma, "PUT"));

		Link cancela = linkTo(methodOn(PagamentoController.class).cancela(id)).withRel("cancela");
		links.add(new LinkWithMethod(cancela, "DELETE"));

		return ResponseEntity.created(path).body(EntityModel.of(dto, links));
	}

	@PutMapping("/{id}")
	public EntityModel<PagamentoDto> confirma(@PathVariable("id") Long id) {
		Pagamento pagamento = pagamentoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException());
		pagamento.setStatus(Pagamento.Status.CONFIRMADO);
		pagamentoRepo.save(pagamento);

		Long pedidoId = pagamento.getPedidoId();
		pedidoRestClient.avisaQueFoiPago(pedidoId);

		List<Link> links = new ArrayList<>();

		Link self = linkTo(methodOn(PagamentoController.class).detalha(id)).withSelfRel();
		links.add(self);

		PagamentoDto dto = new PagamentoDto(pagamento);

		return EntityModel.of(dto, links);
	}

	@DeleteMapping("/{id}")
	EntityModel<PagamentoDto> cancela(@PathVariable("id") Long id) {
		Pagamento pagamento = pagamentoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException());
		pagamento.setStatus(Pagamento.Status.CANCELADO);
		pagamentoRepo.save(pagamento);

		List<Link> links = new ArrayList<>();

		Link self = linkTo(methodOn(PagamentoController.class).detalha(id)).withSelfRel();
		links.add(self);

		return EntityModel.of(new PagamentoDto(pagamento), links);
	}

}