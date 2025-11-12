package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<ProductDto> getProducts(@RequestParam(name = "categoryId", required = false) Byte categoryId) {
        List<Product> products;
        if (categoryId != null) {
            products = productRepository.findAllByCategoryId(categoryId);
        } else {
            products = productRepository.findAllWithCategory();
        }
        return products
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Product not found"));
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDto productDto, UriComponentsBuilder uriBuilder) {
        var category = categoryRepository.findById(productDto.categoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Category not found"));
        }

        var product = productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(Collections.singletonMap("message", "Product created"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        var product = productRepository.findById(id).orElse(null);
        System.out.println(productMapper.toDto(product));
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Product not found"));
        }

        productMapper.updateDto(productDto, product);
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "Product updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Product not found"));
        }
        productRepository.delete(product);
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "Product deleted"));
    }

}
