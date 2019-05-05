package com.system.shoppingcard.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.shoppingcard.domain.Category;
import com.system.shoppingcard.domain.Product;
import com.system.shoppingcard.messageProvider.LocaleMessageProvider;
import com.system.shoppingcard.repository.ProductRepository;
import com.system.shoppingcard.service.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private LocaleMessageProvider localeMessageProvider;

    private JacksonTester<Product> jsonProduct;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void createProduct() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Product product = new Product();
        product.setId(1L);
        product.setTitle("apple");
        product.setCategory(category);
        product.setPrice(10.0);

        // given
        given(categoryService.get(category.getId())).willReturn(category);
        given(productRepository.save(product)).willReturn(product);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                post("/product/add")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonProduct.write(product).getJson()
                )).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.product.add"));
    }

    @Test
    public void updateProduct() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Product product = new Product();
        product.setId(1L);
        product.setTitle("apple");
        product.setCategory(category);
        product.setPrice(10.0);

        // given
        given(categoryService.get(category.getId())).willReturn(category);
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        Product newProduct = new Product();
        newProduct.setId(1L);
        newProduct.setTitle("drink");
        newProduct.setCategory(category);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                put("/product/update/" + product.getId())
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonProduct.write(newProduct).getJson()
                )).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.product.update"));
    }

    @Test
    public void getAllCategories() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Product product = new Product();
        product.setId(1L);
        product.setTitle("apple");
        product.setCategory(category);
        product.setPrice(10.0);

        // given
        given(productRepository.save(product)).willReturn(product);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/product/getAll")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getProduct() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Product product = new Product();
        product.setId(1L);
        product.setTitle("apple");
        product.setCategory(category);
        product.setPrice(10.0);

        // given
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/product/get/" + product.getId())
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    public void deleteProduct() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Product product = new Product();
        product.setId(1L);
        product.setTitle("apple");
        product.setCategory(category);
        product.setPrice(10.0);

        // given
        given(productRepository.save(product)).willReturn(product);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                delete("/product/delete/" + product.getId())
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.product.delete"));
    }
}