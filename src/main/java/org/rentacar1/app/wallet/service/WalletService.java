package org.rentacar1.app.wallet.service;

import lombok.extern.slf4j.Slf4j;
import org.rentacar1.app.user.model.User;
import org.rentacar1.app.wallet.model.Wallet;
import org.rentacar1.app.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

@Slf4j
@Service
public class WalletService {
    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public void createNewWallet(User user) {
        Wallet wallet = initializeWallet(user);

        log.info("Wallet with id [%s] and balance [%.2f] is successfully created!".formatted(wallet.getId(), wallet.getBalance()));
        walletRepository.save(wallet);
    }

    private Wallet initializeWallet(User user) {
        return Wallet.builder()
                .user(user)
                .balance(new BigDecimal("0.0"))
                .currency(Currency.getInstance("EUR"))
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }




    public Wallet getWalletById(UUID id) {
        return walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found"));
    }


    public void addFunds(UUID walletId, BigDecimal amount) {
        Wallet wallet = getWalletById(walletId);
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }

    public void deductFunds(UUID walletId, BigDecimal amount) {
        Wallet wallet = getWalletById(walletId);
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
    }


}
