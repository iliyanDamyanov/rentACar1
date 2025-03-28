package org.rentacar1.app.wallet.service;

import lombok.extern.slf4j.Slf4j;
import org.rentacar1.app.user.model.User;
import org.rentacar1.app.wallet.model.Wallet;
import org.rentacar1.app.wallet.repository.WalletRepository;
import org.rentacar1.app.web.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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


    public void createNewWallet(User user) {
        Wallet wallet = initializeWallet(user);
        walletRepository.save(wallet);
        log.info("Wallet with id [{}] and balance [{} EUR] is successfully created!", wallet.getId(), wallet.getBalance());
    }


    private Wallet initializeWallet(User user) {
        return Wallet.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .currency(Currency.getInstance("EUR"))
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }


    public Wallet getWalletById(UUID id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }


    public void addFunds(UUID userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setUpdatedOn(LocalDateTime.now());
        walletRepository.save(wallet);

        User user = wallet.getUser();


        sendAddMoneyNotification(user, amount.doubleValue());
        log.info("Successfully added [{}] лв. to wallet of user [{}]", amount, user.getUsername());

    }


    public boolean hasSufficientFunds(User user, BigDecimal finalPrice) {
        Optional<Wallet> walletOpt = walletRepository.findByUser(user);

        if (walletOpt.isEmpty()) {
            return false;
        }

        Wallet wallet = walletOpt.get();
        return wallet.getBalance().compareTo(finalPrice) >= 0;
    }


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

        sendWithdrawMoneyNotification(user, amount.doubleValue());

        log.info("Successfully deducted [{}] лв. from wallet of user [{}]", amount, user.getUsername());
    }


    public Wallet getWalletByUserId(UUID userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user with ID: " + userId));
    }

    private void sendAddMoneyNotification(User user, double amount) {
        String notificationUrl = "http://localhost:8081/api/notifications";

        NotificationDTO notificationDTO = new NotificationDTO(
                user.getId(),
                user.getUsername(),
                "Успешно добавени " + amount + " лв. в портфейла.",
                false,
                LocalDateTime.now()
        );

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity(notificationUrl, notificationDTO, Void.class);
            log.info("Successfully sent add money notification for user [{}]", user.getUsername());
        } catch (Exception e) {
            log.error("Failed to send add money notification for user [{}]: {}", user.getUsername(), e.getMessage());
        }
    }

    private void sendWithdrawMoneyNotification(User user, double amount) {
        String notificationUrl = "http://localhost:8081/api/notifications";

        NotificationDTO notificationDTO = new NotificationDTO(
                user.getId(),
                user.getUsername(),
                "Успешно изтеглени " + amount + " лв. от портфейла.",
                false,
                LocalDateTime.now()
        );

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity(notificationUrl, notificationDTO, Void.class);
            log.info("Successfully sent withdraw money notification for user [{}]", user.getUsername());
        } catch (Exception e) {
            log.error("Failed to send withdraw money notification for user [{}]: {}", user.getUsername(), e.getMessage());
        }
    }
}
