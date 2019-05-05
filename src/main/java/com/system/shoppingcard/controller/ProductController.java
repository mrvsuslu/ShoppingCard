package com.system.shoppingcard.controller;

import com.system.shoppingcard.authentication.ShoppingCardAuthService;
import com.system.shoppingcard.domain.Product;
import com.system.shoppingcard.dto.ProductDTO;
import com.system.shoppingcard.messageProvider.LocaleMessageProvider;
import com.system.shoppingcard.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ShoppingCardAuthService shoppingCardAuthService;

    private final LocaleMessageProvider localeMessageProvider;

    @PostMapping(value = "/add")
    @ResponseBody
    public ResponseEntity addProduct(@RequestHeader HttpHeaders httpHeaders, @RequestBody ProductDTO product) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        productService.add(product);
        return new ResponseEntity<>(localeMessageProvider.getMessage("success.product.add"), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}")
    @ResponseBody
    public ResponseEntity updateProduct(@RequestHeader HttpHeaders httpHeaders, @RequestBody ProductDTO product, @PathVariable("id") final long id) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        productService.update(product, id);
        return new ResponseEntity<>(localeMessageProvider.getMessage("success.product.update"), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @ResponseBody
    public ResponseEntity deleteProduct(@RequestHeader HttpHeaders httpHeaders, @PathVariable("id") final long id) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        productService.delete(id);
        return new ResponseEntity<>(localeMessageProvider.getMessage("success.product.delete"), HttpStatus.OK);
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Product>> getAllProducts(@RequestHeader HttpHeaders httpHeaders) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Product> getProduct(@RequestHeader HttpHeaders httpHeaders, @PathVariable("id") long id) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        return new ResponseEntity<>(productService.get(id), HttpStatus.OK);
    }

}
