package com.aninfo.service;

import com.aninfo.exceptions.DepositNegativeSumException;
import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.exceptions.WithdrawNegativeSumException;
import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.repository.AccountRepository;
import com.aninfo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createDeposit(Transaction transaction) {
        if(transaction.getValue() <= 0){
            throw new DepositNegativeSumException("Cannot deposit negative amount");
        }
        transaction.setValue(this.promo(transaction.getValue()));
        return transactionRepository.save(transaction);
    }

    public double promo(double sum){
        if(sum >= 2000){
            double extra = sum * 0.1;
            if(extra > 500) extra = 500;
            sum += extra;
        }
        return sum;
    }

    public void deleteTransaction(Long transactionID){
        transactionRepository.deleteById(transactionID);
    }

    public Transaction newWithdraw(Transaction transaction) {
        if(transaction.getValue() <= 0){
            throw new WithdrawNegativeSumException("Cannot deposit negative amount");
        }
        double sum = - transaction.getValue();
        transaction.setValue(sum);
        return transactionRepository.save(transaction);
    }

    public Optional<Transaction> getTransaction(Long id) {
        return transactionRepository.findById(id);
    }

    public Collection<Transaction> getTransactionsFromCbu(Long cbu) {
        return transactionRepository.findAllByCbu(cbu);
    }
}
