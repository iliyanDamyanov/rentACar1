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
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class WalletService {
    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     * Създава нов портфейл за потребителя с баланс 0.0 EUR.
     */
    public void createNewWallet(User user) {
        Wallet wallet = initializeWallet(user);
        walletRepository.save(wallet);
        log.info("Wallet with id [{}] and balance [{} EUR] is successfully created!", wallet.getId(), wallet.getBalance());
    }

    /**
     * Инициализира нов портфейл с 0.0 EUR.
     */
    private Wallet initializeWallet(User user) {
        return Wallet.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .currency(Currency.getInstance("EUR"))
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }

    /**
     * Връща портфейла на потребителя по ID.
     */
    public Wallet getWalletById(UUID id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    /**
     * Добавя средства към портфейла.
     */
    public void addFunds(UUID walletId, BigDecimal amount) {
        Wallet wallet = getWalletById(walletId);
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setUpdatedOn(LocalDateTime.now());
        walletRepository.save(wallet);
    }

    /**
     * Проверява дали потребителят има достатъчно средства в портфейла си.
     */
    public boolean hasSufficientFunds(User user, BigDecimal finalPrice) {
        Optional<Wallet> walletOpt = walletRepository.findByUser(user);

        if (walletOpt.isEmpty()) {
            return false; // Няма портфейл → няма средства
        }

        Wallet wallet = walletOpt.get();
        return wallet.getBalance().compareTo(finalPrice) >= 0;
    }

    /**
     * Изважда средства от портфейла, ако балансът е достатъчен.
     */
    public void deductAmount(User user, BigDecimal amount) {
        Optional<Wallet> walletOpt = walletRepository.findByUser(user);

        if (walletOpt.isEmpty()) {
            throw new RuntimeException("Wallet not found for user: " + user.getUsername());
        }

        Wallet wallet = walletOpt.get();

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds in wallet for user: " + user.getUsername());
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setUpdatedOn(LocalDateTime.now());
        walletRepository.save(wallet);
    }
}
