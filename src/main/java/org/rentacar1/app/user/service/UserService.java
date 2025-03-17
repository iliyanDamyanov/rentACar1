package org.rentacar1.app.user.service;

import lombok.extern.slf4j.Slf4j;
import org.rentacar1.app.exeptions.UsernameAlreadyExistException;
import org.rentacar1.app.rent.service.RentService;
import org.rentacar1.app.user.model.User;
import org.rentacar1.app.user.model.UserRole;
import org.rentacar1.app.user.repository.UserRepository;
import org.rentacar1.app.wallet.service.WalletService;
import org.rentacar1.app.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RentService rentService;
    private final WalletService walletService;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RentService rentService,
                       WalletService walletService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.rentService = rentService;
        this.walletService = walletService;
    }

    public User register(RegisterRequest registerRequest) {

        Optional<User> userOptional = userRepository.findByUsername(registerRequest.getUserName());
        if (userOptional.isPresent()) {
            throw new UsernameAlreadyExistException("Username [%s] already exist.".formatted(registerRequest.getUserName()));
        }

        User user = userRepository.save(initilizeUser(registerRequest));
        walletService.createNewWallet(user);
        log.info("Succesfuly creaatet new User acc username [%s] and id [%s]".formatted(user.getUsername(), user.getId()));


        return user;
    }

    private User initilizeUser(RegisterRequest registerRequest) {
        return User.builder()
                .username(registerRequest.getUserName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .role(UserRole.USER)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }
}
