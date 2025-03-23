package org.rentacar1.app.user.service;

import lombok.extern.slf4j.Slf4j;
import org.rentacar1.app.exeptions.DomainExeption;
import org.rentacar1.app.exeptions.UsernameAlreadyExistException;
import org.rentacar1.app.rent.service.RentService;
import org.rentacar1.app.security.AuthenticationMetadata;
import org.rentacar1.app.user.model.User;
import org.rentacar1.app.user.model.UserRole;
import org.rentacar1.app.user.repository.UserRepository;
import org.rentacar1.app.wallet.service.WalletService;
import org.rentacar1.app.web.dto.RegisterRequest;
import org.rentacar1.app.web.dto.UpdateProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

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
                .isActive(true)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new DomainExeption("User with this username does not exist"));

        return new AuthenticationMetadata(user.getId(),username,user.getPassword(),user.getRole(),user.isActive());
    }

    public User getById(UUID id) {

        Optional<User> user = userRepository.findById(id);

        user.orElseThrow(() -> new DomainExeption("User with id [%s] does not exist.".formatted(id)));

        return user.get();
    }

    public void updateProfile(UUID userId, UpdateProfileRequest updateProfileRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updateProfileRequest.getEmail() != null) user.setEmail(updateProfileRequest.getEmail());
        if (updateProfileRequest.getFirstName() != null) user.setFirstName(updateProfileRequest.getFirstName());
        if (updateProfileRequest.getLastName() != null) user.setLastName(updateProfileRequest.getLastName());
        if (updateProfileRequest.getPassword() != null && !updateProfileRequest.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updateProfileRequest.getPassword()));
        }

        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
}
