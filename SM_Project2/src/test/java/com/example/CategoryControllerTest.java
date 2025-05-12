package com.example;

import com.example.Controllers.CategoryController;
import com.example.Models.Category;
import com.example.Services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void createCategory_shouldReturnCreatedCategory() throws Exception {
        Category category = new Category("Food", null);
        when(categoryService.saveCategory(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Food\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Food"));
    }

    @Test
    void getCategory_shouldReturnCategory() throws Exception {
        Category category = new Category("Food", null);
        when(categoryService.getCategory(1L)).thenReturn(Optional.of(category));

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Food"));
    }

    @Test
    void getCategory_shouldReturnNotFound() throws Exception {
        when(categoryService.getCategory(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCategory_shouldReturnNoContent() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());
    }
}
