package com.kshrd.springprojection.service;

import com.kshrd.springprojection.dto.baseResponse.PagedResponse;
import com.kshrd.springprojection.dto.projection.TransactionSummary;
import com.kshrd.springprojection.dto.projection.TransactionWithAccount;
import com.kshrd.springprojection.dto.request.TransactionRequest;
import com.kshrd.springprojection.dto.response.TransactionResponse;
import com.kshrd.springprojection.enumeration.TransactionProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    List<TransactionWithAccount> getByType(String type);

    <T> List<T> getByAmountGreaterThan(BigDecimal amount, Class<T> type);

    List<TransactionSummary> getAll();

    TransactionResponse create(TransactionRequest req);

    TransactionResponse getById(Long id);

    TransactionResponse update(Long id, TransactionRequest req);

    void delete(Long id);

    PagedResponse<Page<TransactionSummary>> getPaged(Integer page, Integer size, Sort.Direction direction, TransactionProperty sortBy);
}
