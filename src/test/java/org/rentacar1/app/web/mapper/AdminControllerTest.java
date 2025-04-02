package org.rentacar1.app.web.mapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rentacar1.app.security.AuthenticationMetadata;
import org.rentacar1.app.user.model.User;
import org.rentacar1.app.user.model.UserRole;
import org.rentacar1.app.user.repository.UserRepository;
import org.rentacar1.app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;



@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAdminUsers_ShouldReturnUserListAndView() throws Exception {

        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setUsername("admin1");

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setUsername("user2");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));


        mockMvc.perform(get("/admin/users")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void postDeleteUser_ShouldDeleteUserAndRedirect() throws Exception {

        UUID userId = UUID.randomUUID();

        mockMvc.perform(post("/admin/delete/{userId}", userId)
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(userId);
    }

    @Test
    void getEditUserForm_ShouldReturnEditUserView_WhenUserExists() throws Exception {

        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setUsername("TestUser");
        when(userService.getById(userId)).thenReturn(user);

        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "admin", "password", UserRole.ADMIN, true);

        mockMvc.perform(get("/admin/edit-user/{id}", userId)
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/edit-user"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void getEditUserForm_ShouldReturn404_WhenUserDoesNotExist() throws Exception {

        UUID invalidId = UUID.randomUUID();
        when(userService.getById(invalidId)).thenThrow(new RuntimeException("User not found"));

        AuthenticationMetadata principal = new AuthenticationMetadata(invalidId, "admin", "password", UserRole.ADMIN, true);

        mockMvc.perform(get("/admin/edit-user/{id}", invalidId)
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }


    @Test
    void postUpdateUser_ShouldSaveUserAndRedirect() throws Exception {

        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setUsername("UpdatedUser");

        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "admin", "password", UserRole.ADMIN, true);


        mockMvc.perform(post("/admin/update")
                        .with(user(principal))
                        .with(csrf())
                        .param("id", userId.toString())
                        .param("username", "UpdatedUser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        verify(userRepository, times(1)).save(any(User.class));
    }






}
