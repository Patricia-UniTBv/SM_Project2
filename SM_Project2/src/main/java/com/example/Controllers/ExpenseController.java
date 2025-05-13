package com.example.Controllers;

import com.example.Models.Expense;
import com.example.Services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<Expense> getExpenses(@RequestParam(required = false) String userEmail) {
        if (userEmail != null) {
            return expenseService.getExpensesByUserEmail(userEmail);
        } else {
            return expenseService.getAllExpenses();
        }
    }


    @PostMapping
    public ResponseEntity<Expense>  addExpense(@RequestBody Expense expense) {
        if (expense.getUserEmail() == null || expense.getUserEmail().isEmpty()) {
            throw new IllegalArgumentException("User email must be provided");
        }
        Expense createdExpense = expenseService.addExpense(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense updatedExpense) {
        Expense expense = expenseService.updateExpense(id, updatedExpense);
        return ResponseEntity.ok(expense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}