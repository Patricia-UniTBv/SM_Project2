package com.example;

import com.example.Models.User;
import com.example.Repositories.UserRepository;
import com.example.Services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void saveUser_shouldReturnSavedUser() {
        User user = new User("John Doe", "john.doe@example.com");
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("John Doe", savedUser.getName());
        assertEquals("john.doe@example.com", savedUser.getEmail());
    }

    @Test
    void getUser_shouldReturnUser_whenUserExists() {
        User user = new User("John Doe", "john.doe@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUser(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
    }

    @Test
    void getUser_shouldReturnEmpty_whenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUser(1L);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void deleteUser_shouldDeleteUser() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}