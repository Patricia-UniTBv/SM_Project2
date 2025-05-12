package com.example;

import com.example.Models.Expense;
import com.example.Repositories.ExpenseRepository;
import com.example.Services.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private Expense expense;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a sample expense
        expense = new Expense();
        expense.setId(1L);
        expense.setTitle("Test Expense");
        expense.setAmount(100.0);
        expense.setDate(LocalDate.of(2024, 12, 31));
    }

    @Test
    void saveExpense_shouldReturnSavedExpense() {
        Expense expense = new Expense("Lunch", 20.5, LocalDate.now(), null, null);
        when(expenseRepository.save(expense)).thenReturn(expense);

        Expense savedExpense = expenseService.saveExpense(expense);

        assertNotNull(savedExpense);
        assertEquals("Lunch", savedExpense.getTitle());
        assertEquals(20.5, savedExpense.getAmount());
    }

    @Test
    void getExpense_shouldReturnExpense_whenExpenseExists() {
        Expense expense = new Expense("Lunch", 20.5, LocalDate.now(), null, null);
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));

        Optional<Expense> foundExpense = expenseService.getExpense(1L);

        assertTrue(foundExpense.isPresent());
        assertEquals("Lunch", foundExpense.get().getTitle());
    }

    @Test
    void getExpense_shouldReturnEmpty_whenExpenseDoesNotExist() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Expense> foundExpense = expenseService.getExpense(1L);

        assertFalse(foundExpense.isPresent());
    }

    @Test
    void updateExpense_shouldReturnUpdatedExpense() {
        Expense updatedExpense = new Expense();
        updatedExpense.setId(1L);
        updatedExpense.setTitle("Updated Title");
        updatedExpense.setAmount(200.0);
        updatedExpense.setDate(LocalDate.of(2024, 12, 31));

        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(updatedExpense);

        Expense result = expenseService.updateExpense(1L, updatedExpense);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals(200.0, result.getAmount());
        assertEquals(LocalDate.of(2024, 12, 31), result.getDate());
    }

    @Test
    void updateExpense_shouldThrowExceptionWhenExpenseNotFound() {
        Expense updatedExpense = new Expense();
        updatedExpense.setId(1L);
        updatedExpense.setTitle("Updated Title");
        updatedExpense.setAmount(200.0);
        updatedExpense.setDate(LocalDate.of(2024, 12, 31));

        when(expenseRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            expenseService.updateExpense(1L, updatedExpense);
        });

        assertEquals("Expense not found with id: 1", exception.getMessage());
    }

    @Test
    void deleteExpense_shouldDeleteExpense() {
        when(expenseRepository.existsById(1L)).thenReturn(true);

        expenseService.deleteExpense(1L);

        verify(expenseRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteExpense_shouldThrowExceptionWhenExpenseNotFound() {
        when(expenseRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            expenseService.deleteExpense(1L);
        });

        assertEquals("Expense not found with id: 1", exception.getMessage());
    }
}