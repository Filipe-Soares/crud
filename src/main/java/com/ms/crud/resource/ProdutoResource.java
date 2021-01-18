package com.ms.crud.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ms.crud.dto.ProdutoDto;
import com.ms.crud.services.ProdutoService;

@RestController
@RequestMapping("/produto")
public class ProdutoResource {

	private final ProdutoService produtoService;
	private final PagedResourcesAssembler<ProdutoDto> assembler;
	
	@Autowired
	public ProdutoResource(ProdutoService produtoService, PagedResourcesAssembler<ProdutoDto> assembler) {
		this.produtoService = produtoService;
		this.assembler = assembler;
	}
	
	
	@GetMapping(value="/id", produces= {"application/json","application/xml","application/x-yaml"})
	public ProdutoDto findById(@PathVariable("id") Long id) {
		ProdutoDto produtoDto = produtoService.findById(id);
		produtoDto.add(org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo(org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn(ProdutoResource.class).findById(id)).withSelfRel());
		return null;
	}
	
	@GetMapping(produces= {"application/json","application/xml","application/x-yaml"})
	public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0")int page,
			@RequestParam(value = "limit", defaultValue = "12")int limit,
			@RequestParam(value = "direction", defaultValue = "asc")String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));
		
		Page<ProdutoDto> produtos = produtoService.findAll(pageable);
		produtos.stream()
			.forEach(p -> p.add(org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
					.linkTo(org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
					.methodOn(ProdutoResource.class).findById(p.getId())).withSelfRel()));
		PagedModel<EntityModel<ProdutoDto>> pagedModel = assembler.toModel(produtos);
			
		return new ResponseEntity<>(pagedModel, HttpStatus.OK);
	}
	
	
	
	
}
