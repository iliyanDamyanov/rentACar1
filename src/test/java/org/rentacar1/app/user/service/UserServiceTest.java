package org.rentacar1.app.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rentacar1.app.exeptions.DomainExeption;
import org.rentacar1.app.user.model.User;
import org.rentacar1.app.user.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp(){

        user=new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testUser");
        user.setEmail("test@example.com");
    }

    @Test
    void testGetById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User found = userService.getById(user.getId());

        assertNotNull(found);
        assertEquals(user.getUsername(), found.getUsername());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void testGetById_ShouldThrowException_WhenUserNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(DomainExeption.class, () ->
                userService.getById(nonExistentId));

        assertTrue(exception.getMessage().contains("does not exist"));
    }
}
