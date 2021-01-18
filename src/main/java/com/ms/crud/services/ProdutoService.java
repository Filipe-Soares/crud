package com.ms.crud.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ms.crud.dto.ProdutoDto;
import com.ms.crud.entity.Produto;
import com.ms.crud.exception.ResourceNotFoundException;
import com.ms.crud.repository.ProdutoRepository;

@Service
public class ProdutoService {

	
		private final ProdutoRepository produtoRepository;
		
		@Autowired
		public ProdutoService(ProdutoRepository produtoRepository) {
			this.produtoRepository = produtoRepository;
		}
		
		public ProdutoDto create(ProdutoDto produtoDto) {
			ProdutoDto produtoDtoReturn = ProdutoDto.create(produtoRepository.save(Produto.create(produtoDto)));
			return produtoDtoReturn;
		}
		
		public Page<ProdutoDto> findAll(Pageable pageable){
			var page = produtoRepository.findAll(pageable);
			return page.map(this::convertToProdutoDto);
		}
		
		private ProdutoDto convertToProdutoDto(Produto produto) {
			return ProdutoDto.create(produto);
		}
		
		public ProdutoDto findById(Long id) {
			var entity = produtoRepository.findById(id)
					.orElseThrow(()-> new ResourceNotFoundException("No records found for this ID"));
			return ProdutoDto.create(entity);
		}
		
		public ProdutoDto update(ProdutoDto produtoDto) {
			final Optional<Produto> optionalProduto = produtoRepository.findById(produtoDto.getId());
			
			if(!optionalProduto.isPresent()) {
				new ResourceNotFoundException("No records found for this ID");
			}
			return ProdutoDto.create(produtoRepository.save(Produto.create(produtoDto)));
		}
		
		public void delete(Long id) {
			var entity = produtoRepository.findById(id)
					.orElseThrow(()-> new ResourceNotFoundException("No records found for this ID"));
			produtoRepository.delete(entity);
		}
		
}
