package org.rentacar1.app.transaction.service;

import lombok.extern.slf4j.Slf4j;
import org.rentacar1.app.exeptions.DomainExeption;
import org.rentacar1.app.transaction.model.Transaction;
import org.rentacar1.app.transaction.model.TransactionStatus;
import org.rentacar1.app.transaction.model.TransactionType;
import org.rentacar1.app.transaction.repository.TransactionRepository;
import org.rentacar1.app.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createNewTransaction(User owner, String sender, String receiver, BigDecimal amount, BigDecimal balanceLeft, Currency currency, TransactionType type, TransactionStatus status, String description, String failureReason, LocalDateTime createdOn){

        Transaction transaction = Transaction.builder()
                .owner(owner)
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .balanceLeft(balanceLeft)
                .currency(currency)
                .type(type)
                .status(status)
                .description(description)
                .failureReason(failureReason)
                .createdOn(createdOn)
                .build();

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllByOwnerId(UUID ownerId) {

        return transactionRepository.findAllByOwnerIdOrderByCreatedOnDesc(ownerId);
    }

    public Transaction getById(UUID id) {

        return transactionRepository.findById(id).orElseThrow(() -> new DomainExeption("Transaction with id [%s] does not exist.".formatted(id)));
    }

}
