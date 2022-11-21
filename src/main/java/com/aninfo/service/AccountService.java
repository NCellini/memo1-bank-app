package com.aninfo.service;

import com.aninfo.exceptions.DepositNegativeSumException;
import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.exceptions.InvalidTransactionTypeException;
import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.repository.AccountRepository;
import com.aninfo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.InvalidTransactionException;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionService transactionService;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Collection<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> findById(Long cbu) {
        return accountRepository.findById(cbu);
    }

    public void save(Account account) {
        accountRepository.save(account);
    }

    public void deleteById(Long cbu) {
        accountRepository.deleteById(cbu);
    }

    @Transactional
    public Account withdraw(Long cbu, Double sum) {
        Account account = accountRepository.findAccountByCbu(cbu);

        if (account.getBalance() < sum) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - sum);
        accountRepository.save(account);

        return account;
    }

    @Transactional
    public Account deposit(Long cbu, Double sum) {

        if (sum <= 0) {
            throw new DepositNegativeSumException("Cannot deposit negative sums");
        }

        Account account = accountRepository.findAccountByCbu(cbu);
        account.setBalance(account.getBalance() + transactionService.promo(sum));
        accountRepository.save(account);

        return account;
    }

    public Transaction createDeposit(Transaction transaction){
        Account account = accountRepository.findAccountByCbu(transaction.getCbu());
        if (account == null){
            throw new InvalidTransactionTypeException("account doesn't exist");
        }
        deposit(account.getCbu(), transaction.getValue());

        return transactionService.createDeposit(transaction);
    }

    public Transaction newWithdraw(Transaction transaction) {
        Account account = accountRepository.findAccountByCbu((transaction.getCbu()));
        if (account == null) {
            throw new InvalidTransactionTypeException("account doesn's exist");
        }
        withdraw(account.getCbu(), transaction.getValue());

        return transactionService.newWithdraw(transaction);
    }

    public void deleteTransaction(Long id) {
        transactionService.deleteTransaction(id);
    }

    public Optional<Transaction> getTransaction(Long id) {
        return transactionService.getTransaction(id);
    }

    public Collection<Transaction> getTransactionsFromCbu(Long cbu) {
        return transactionService.getTransactionsFromCbu(cbu);
    }
}
