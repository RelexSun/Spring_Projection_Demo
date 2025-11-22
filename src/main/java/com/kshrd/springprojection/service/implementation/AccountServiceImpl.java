package com.kshrd.springprojection.service.implementation;

import com.kshrd.springprojection.dto.request.AccountRequest;
import com.kshrd.springprojection.dto.response.AccountResponse;
import com.kshrd.springprojection.entity.Account;
import com.kshrd.springprojection.exception.NotFoundException;
import com.kshrd.springprojection.repository.AccountRepository;
import com.kshrd.springprojection.service.AccountService;
import com.kshrd.springprojection.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public List<AccountResponse> getAll() {
        return accountRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AccountResponse getById(Long id) {
        return accountRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Account not found with id " + id));
    }

    public AccountResponse create(AccountRequest req) {
        Account account = Account.builder()
                .accountNumber(new RandomUtil().generateAccountNum())
                .holderName(req.holderName())
                .build();
        return toResponse(accountRepository.save(account));
    }

    public AccountResponse update(Long id, AccountRequest req) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found with id " + id));
        account.setHolderName(req.holderName());
        return toResponse(accountRepository.save(account));
    }

    public void delete(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found with id " + id));
        accountRepository.delete(account);
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getHolderName()
        );
    }
}