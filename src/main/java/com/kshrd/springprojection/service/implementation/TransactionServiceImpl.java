package com.kshrd.springprojection.service.implementation;

import com.kshrd.springprojection.dto.baseResponse.PagedResponse;
import com.kshrd.springprojection.dto.baseResponse.PaginationInfo;
import com.kshrd.springprojection.dto.projection.TransactionSummary;
import com.kshrd.springprojection.dto.projection.TransactionWithAccount;
import com.kshrd.springprojection.dto.request.TransactionRequest;
import com.kshrd.springprojection.dto.response.AccountResponse;
import com.kshrd.springprojection.dto.response.TransactionResponse;
import com.kshrd.springprojection.entity.Account;
import com.kshrd.springprojection.entity.Transaction;
import com.kshrd.springprojection.enumeration.TransactionProperty;
import com.kshrd.springprojection.exception.BadRequestException;
import com.kshrd.springprojection.exception.NotFoundException;
import com.kshrd.springprojection.repository.AccountRepository;
import com.kshrd.springprojection.repository.TransactionRepository;
import com.kshrd.springprojection.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.kshrd.springprojection.utils.ResponseUtil.pagedResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    public List<TransactionWithAccount> getByType(String type) {
        if (type == null || type.isBlank()) {
            throw new BadRequestException("Transaction type is required");
        }

        return transactionRepository.findByType(type.toUpperCase());
    }

    @Override
    public <T> List<T> getByAmountGreaterThan(BigDecimal amount, Class<T> type) {
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Amount must be a positive value");
        }

        return transactionRepository.findByAmountGreaterThan(amount, type);
    }

    @Override
    public List<TransactionSummary> getAll() {
        return transactionRepository.findAllBy();
    }

    @Override
    public TransactionResponse create(TransactionRequest req) {

        // Check account exists
        var account = accountRepository.findById(req.accountId())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        Transaction tx = Transaction.builder()
                .id(null)
                .account(account)
                .amount(req.amount())
                .type(req.type())
                .timestamp(LocalDateTime.now())
                .build();

        Transaction saved = transactionRepository.save(tx);

        return mapToResponse(saved);
    }

    @Override
    public TransactionResponse getById(Long id) {
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));

        return mapToResponse(tx);
    }

    @Override
    public TransactionResponse update(Long id, TransactionRequest req) {

        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));

        var account = accountRepository.findById(req.accountId())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        tx.setAmount(req.amount());
        tx.setType(req.type());
        tx.setAccount(account);

        Transaction updated = transactionRepository.save(tx);

        return mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {
        boolean exists = transactionRepository.existsById(id);
        if (!exists) {
            throw new NotFoundException("Transaction not found");
        }
        transactionRepository.deleteById(id);
    }

    @Override
    public PagedResponse<Page<TransactionSummary>> getPaged(
            Integer page,
            Integer size,
            Sort.Direction direction,
            TransactionProperty sortBy
    ) {
        int zeroBased = Math.max(page, 1) - 1;

        Pageable pageable = PageRequest.of(
                zeroBased,
                size,
                Sort.by(direction, sortBy.getFieldName())
        );

        Page<TransactionSummary> pageResult = transactionRepository.findAllBy(pageable);
        PaginationInfo paginationInfo = new PaginationInfo(
                pageResult.getTotalElements(),
                pageResult.getSize(),
                pageResult.getNumber() + 1,
                pageResult.getTotalPages());

        return pagedResponse(
                pageResult,
                paginationInfo.totalElements(),
                paginationInfo.currentPage(),
                paginationInfo.pageSize(),
                paginationInfo.totalPages()
        );
    }

    private TransactionResponse mapToResponse(Transaction tx) {
        return new TransactionResponse(
                tx.getId(),
                tx.getType(),
                tx.getAmount(),
                tx.getTimestamp(),
                tx.getAccount().getId(),
                mapToAccountResponse(tx.getAccount())
        );
    }

    private AccountResponse mapToAccountResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getHolderName()
        );
    }
}
