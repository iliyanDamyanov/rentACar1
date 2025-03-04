package org.rentacar1.app.wallet.service;

import org.rentacar1.app.user.model.User;
import org.rentacar1.app.wallet.model.Wallet;
import org.rentacar1.app.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createNewWallet(User user) {
       return new Wallet();
    }

    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    public Wallet getWalletById(UUID id) {
        return walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    public Wallet update(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    public void  deleteWallet(UUID id) {
        walletRepository.deleteById(id);
    }

    public void addFunds(UUID walletId, BigDecimal amount) {
        Wallet wallet = getWalletById(walletId);
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }

    public void deductFunds(UUID walletId, BigDecimal amount) {
        Wallet wallet = getWalletById(walletId);
        if (wallet.getBalance().compareTo(amount)<0) {
            throw new RuntimeException("Insufficient balance");
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
    }


}
