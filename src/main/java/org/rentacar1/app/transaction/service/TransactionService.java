package org.rentacar1.app.transaction.service;

import org.rentacar1.app.transaction.model.Transaction;
import org.rentacar1.app.transaction.repository.TransactionRepository;

import java.util.List;
import java.util.UUID;

public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(UUID id){
        return transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public Transaction saveTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Transaction transaction){
        transactionRepository.delete(transaction);
    }

   public void deleteTransactionById(UUID id){
        transactionRepository.deleteById(id);
   }
}
