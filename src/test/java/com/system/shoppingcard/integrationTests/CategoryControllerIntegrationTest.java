package com.system.shoppingcard.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.shoppingcard.domain.Category;
import com.system.shoppingcard.messageProvider.LocaleMessageProvider;
import com.system.shoppingcard.repository.CategoryRepository;
import junit.framework.TestCase;
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
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private LocaleMessageProvider localeMessageProvider;

    private JacksonTester<Category> jsonCategory;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void createCategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        // given
        given(categoryRepository.save(category)).willReturn(category);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                post("/category/add")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonCategory.write(category).getJson()
                )).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.category.add"));
    }


    @Test
    public void updateCategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        // given
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

        Category newCategory = new Category();
        newCategory.setId(1L);
        newCategory.setTitle("drink");

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                put("/category/update/" + category.getId())
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonCategory.write(newCategory).getJson()
                )).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.category.update"));
    }

    @Test
    public void getAllCategories() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        // given
        given(categoryRepository.save(category)).willReturn(category);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/category/getAll")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getCategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        // given
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/category/get/" + category.getId())
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    public void deleteCategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        // given
        given(categoryRepository.save(category)).willReturn(category);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                delete("/category/delete/" + category.getId())
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        TestCase.assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.category.delete"));

    }

}
