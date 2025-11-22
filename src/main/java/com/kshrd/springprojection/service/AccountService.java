package com.kshrd.springprojection.service;

import com.kshrd.springprojection.dto.request.AccountRequest;
import com.kshrd.springprojection.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {
    List<AccountResponse> getAll();

    AccountResponse getById(Long id);

    AccountResponse create(AccountRequest req);

    AccountResponse update(Long id, AccountRequest req);

    void delete(Long id);
}
