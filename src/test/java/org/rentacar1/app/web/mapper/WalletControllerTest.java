package org.rentacar1.app.web.mapper;

import org.junit.jupiter.api.Test;
import org.rentacar1.app.security.AuthenticationMetadata;
import org.rentacar1.app.user.model.User;
import org.rentacar1.app.user.model.UserRole;
import org.rentacar1.app.user.service.UserService;
import org.rentacar1.app.wallet.model.Wallet;
import org.rentacar1.app.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {


    @MockitoBean
    private WalletService walletService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getWalletPage_shouldReturnWalletView() throws Exception {

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("user@example.com");

        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal("100.00"));
        wallet.setCurrency(Currency.getInstance("EUR"));


        AuthenticationMetadata principal = new AuthenticationMetadata(
                UUID.randomUUID(),
                "user@example.com",
                "123123",
                UserRole.USER,
                true
        );


        when(userService.findByUsername("user@example.com")).thenReturn(user);
        when(walletService.getWalletByUserId(any(UUID.class))).thenReturn(wallet);


        MockHttpServletRequestBuilder request = get("/wallet")
                .with(user(principal))
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("wallet"))
                .andExpect(model().attributeExists("wallet"));
    }

    @Test
    void addFunds_shouldRedirectToWallet() throws Exception {

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("user@example.com");

        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal("100.00"));
        wallet.setCurrency(Currency.getInstance("EUR"));


        AuthenticationMetadata principal = new AuthenticationMetadata(
                UUID.randomUUID(),
                "user@example.com",
                "123123",
                UserRole.USER,
                true
        );

        when(userService.findByUsername("user@example.com")).thenReturn(user);
        when(walletService.getWalletByUserId(any(UUID.class))).thenReturn(wallet);


        MockHttpServletRequestBuilder request = post("/wallet/add")
                .with(user(principal))
                .with(csrf())
                .param("amount", "50.00");


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/wallet"));
    }

    @Test
    void addFunds_withInvalidAmount_shouldReturnError() throws Exception {

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("user@example.com");

        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal("100.00"));
        wallet.setCurrency(Currency.getInstance("EUR"));


        AuthenticationMetadata principal = new AuthenticationMetadata(
                UUID.randomUUID(),
                "user@example.com",
                "123123",
                UserRole.USER,
                true
        );


        when(userService.findByUsername("user@example.com")).thenReturn(user);
        when(walletService.getWalletByUserId(any(UUID.class))).thenReturn(wallet);


        MockHttpServletRequestBuilder request = post("/wallet/add")
                .with(user(principal))
                .with(csrf())
                .param("amount", "-50.00");


        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage", "Amount must be positive"));
    }
}
