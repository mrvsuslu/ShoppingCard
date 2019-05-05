package com.system.shoppingcard.controller;

import com.system.shoppingcard.authentication.ShoppingCardAuthService;
import com.system.shoppingcard.domain.Category;
import com.system.shoppingcard.dto.CategoryDTO;
import com.system.shoppingcard.messageProvider.LocaleMessageProvider;
import com.system.shoppingcard.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ShoppingCardAuthService shoppingCardAuthService;

    private final LocaleMessageProvider localeMessageProvider;

    @PostMapping(value = "/add")
    @ResponseBody
    public ResponseEntity addCategory(@RequestHeader HttpHeaders httpHeaders, @RequestBody CategoryDTO category) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        categoryService.add(category);
        return new ResponseEntity<String>(localeMessageProvider.getMessage("success.category.add"), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}")
    @ResponseBody
    public ResponseEntity updateCategory(@RequestHeader HttpHeaders httpHeaders, @RequestBody CategoryDTO category, @PathVariable("id") final long id) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        categoryService.update(category, id);
        return new ResponseEntity<String>(localeMessageProvider.getMessage("success.category.update"), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @ResponseBody
    public ResponseEntity deleteCategory(@RequestHeader HttpHeaders httpHeaders, @PathVariable("id") final long id) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        categoryService.delete(id);
        return new ResponseEntity<String>(localeMessageProvider.getMessage("success.category.delete"), HttpStatus.OK);
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Category>> getAllCategories(@RequestHeader HttpHeaders httpHeaders) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        return new ResponseEntity<List<Category>>(categoryService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Category> getCategory(@RequestHeader HttpHeaders httpHeaders, @PathVariable("id") final long id) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        return new ResponseEntity<Category>(categoryService.get(id), HttpStatus.OK);
    }

}
