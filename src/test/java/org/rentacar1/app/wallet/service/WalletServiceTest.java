package org.rentacar1.app.wallet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rentacar1.app.user.model.User;
import org.rentacar1.app.wallet.model.Wallet;
import org.rentacar1.app.wallet.repository.WalletRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    private User user;
    private Wallet wallet;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testUser");

        wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .user(user)
                .balance(BigDecimal.valueOf(100))
                .currency(Currency.getInstance("EUR"))
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }

    @Test
    void testAddFunds_ShouldIncreaseBalance_WhenWalletExists() {
        BigDecimal amountToAdd = BigDecimal.valueOf(50);
        when(walletRepository.findByUserId(user.getId())).thenReturn(Optional.of(wallet));

        walletService.addFunds(user.getId(), amountToAdd);

        assertEquals(BigDecimal.valueOf(150), wallet.getBalance());
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void testAddFunds_ShouldThrowException_WhenWalletNotFound() {
        when(walletRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                walletService.addFunds(user.getId(), BigDecimal.valueOf(50)));

        assertEquals("Wallet not found", exception.getMessage());
    }

    @Test
    void testHasSufficientFunds_ShouldReturnTrue_WhenEnoughBalance() {
        when(walletRepository.findByUser(user)).thenReturn(Optional.of(wallet));

        boolean result = walletService.hasSufficientFunds(user, BigDecimal.valueOf(80));

        assertTrue(result);
    }

    @Test
    void testHasSufficientFunds_ShouldReturnFalse_WhenNotEnoughBalance() {
        when(walletRepository.findByUser(user)).thenReturn(Optional.of(wallet));

        boolean result = walletService.hasSufficientFunds(user, BigDecimal.valueOf(200));

        assertFalse(result);
    }

    @Test
    void testDeductAmount_ShouldDecreaseBalance_WhenEnoughFunds() {
        when(walletRepository.findByUser(user)).thenReturn(Optional.of(wallet));

        walletService.deductAmount(user, BigDecimal.valueOf(30));

        assertEquals(BigDecimal.valueOf(70), wallet.getBalance());
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void testDeductAmount_ShouldThrowException_WhenInsufficientFunds() {
        when(walletRepository.findByUser(user)).thenReturn(Optional.of(wallet));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                walletService.deductAmount(user, BigDecimal.valueOf(200)));

        assertTrue(exception.getMessage().contains("Insufficient funds"));
    }

    @Test
    void testGetWalletById_ShouldReturnWallet_WhenExists() {
        when(walletRepository.findById(wallet.getId())).thenReturn(Optional.of(wallet));

        Wallet result = walletService.getWalletById(wallet.getId());

        assertNotNull(result);
        assertEquals(wallet.getId(), result.getId());
    }

    @Test
    void testGetWalletById_ShouldThrowException_WhenNotFound() {
        UUID walletId = UUID.randomUUID();
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                walletService.getWalletById(walletId));

        assertEquals("Wallet not found", exception.getMessage());
    }


}
