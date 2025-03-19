package org.rentacar1.app.wallet.repository;

import org.rentacar1.app.user.model.User;
import org.rentacar1.app.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    List<Wallet> id(UUID id);

    Optional<Wallet> findByUser(User user);

}
