package com.example;

import com.example.Controllers.ExpenseController;
import com.example.Models.Expense;
import com.example.Services.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ExpenseController expenseController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build();
    }

    @Test
    void createExpense_shouldReturnCreatedExpense() throws Exception {
        Expense expense = new Expense("Lunch", 20.5, LocalDate.now(), null, null);
        when(expenseService.saveExpense(any(Expense.class))).thenReturn(expense);

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Lunch\", \"amount\": 20.5, \"date\": \"2025-05-11\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Lunch"))
                .andExpect(jsonPath("$.amount").value(20.5));
    }

    @Test
    void getExpense_shouldReturnExpense() throws Exception {
        Expense expense = new Expense("Lunch", 20.5, LocalDate.now(), null, null);
        when(expenseService.getExpense(1L)).thenReturn(Optional.of(expense));

        mockMvc.perform(get("/api/expenses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Lunch"))
                .andExpect(jsonPath("$.amount").value(20.5));
    }

    @Test
    void getExpense_shouldReturnNotFound() throws Exception {
        when(expenseService.getExpense(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/expenses/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteExpense_shouldReturnNoContent() throws Exception {
        doNothing().when(expenseService).deleteExpense(1L);

        mockMvc.perform(delete("/api/expenses/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateExpense_shouldReturnUpdatedExpense() throws Exception {
        Expense updatedExpense = new Expense();
        updatedExpense.setId(1L);
        updatedExpense.setTitle("Updated Title");
        updatedExpense.setAmount(200.0);
        updatedExpense.setDate(LocalDate.of(2024, 12, 31));  // Ensure the date is a LocalDate

        when(expenseService.updateExpense(eq(1L), any(Expense.class))).thenReturn(updatedExpense);

        mockMvc.perform(put("/api/expenses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "title": "Updated Title",
                    "amount": 200.0,
                    "date": "2024-12-31"
                }
            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.amount").value(200.0))
                .andExpect(jsonPath("$.date").value("2024-12-31"));
    }



}
