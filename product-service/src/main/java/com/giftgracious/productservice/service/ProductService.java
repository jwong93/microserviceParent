package com.giftgracious.productservice.service;


import com.giftgracious.productservice.dto.ProductRequestDTO;
import com.giftgracious.productservice.dto.ProductResponseDTO;
import com.giftgracious.productservice.model.Product;
import com.giftgracious.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService{

    private final ProductRepository productRepository;

    public void create (ProductRequestDTO req){
        Product product = Product.builder().name(req.getName())
                .description(req.getDescription())
                .price(req.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    public void update (ProductResponseDTO res){
        Product prod = Product.builder().id(res.getId())
                .price(res.getPrice()).description(res.getDescription())
                .name(res.getName()).build();
        productRepository.save(prod);
    }

    public void remove (String id){
        productRepository.deleteById(id);
    }

    public Product findById(ProductResponseDTO dto){
        Optional<Product> prod = productRepository.findById(dto.getId());
        if (prod.isPresent()){
            Product prod1 = prod.get();
            return prod1;
        }
        return null;
    }

    public ProductResponseDTO returnDto(String id){
        Optional<Product> p = productRepository.findById(id);
        Product m = p.get();
        return mapToProductRes(m);
    }

    public List<ProductResponseDTO> getAllItems (){
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductRes).toList();
    }

    public ProductResponseDTO mapToProductRes (Product product){
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
