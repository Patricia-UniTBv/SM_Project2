package com.example;

import com.example.Models.Category;
import com.example.Repositories.CategoryRepository;
import com.example.Services.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void saveCategory_shouldReturnSavedCategory() {
        Category category = new Category("Food", null);
        when(categoryRepository.save(category)).thenReturn(category);

        Category savedCategory = categoryService.saveCategory(category);

        assertNotNull(savedCategory);
        assertEquals("Food", savedCategory.getName());
    }

    @Test
    void getCategory_shouldReturnCategory_whenCategoryExists() {
        Category category = new Category("Food", null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<Category> foundCategory = categoryService.getCategory(1L);

        assertTrue(foundCategory.isPresent());
        assertEquals("Food", foundCategory.get().getName());
    }

    @Test
    void getCategory_shouldReturnEmpty_whenCategoryDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Category> foundCategory = categoryService.getCategory(1L);

        assertFalse(foundCategory.isPresent());
    }

    @Test
    void deleteCategory_shouldDeleteCategory() {
        Long categoryId = 1L;
        doNothing().when(categoryRepository).deleteById(categoryId);

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
}