package com.kshrd.springprojection.repository;

import com.kshrd.springprojection.dto.projection.DashboardDto;
import com.kshrd.springprojection.dto.projection.TransactionSummary;
import com.kshrd.springprojection.dto.projection.TransactionWithAccount;
import com.kshrd.springprojection.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // 1. Interface Projection
    List<TransactionSummary> findAllBy();

    // 2. Nested Projection
    List<TransactionWithAccount> findByType(String type);

    // 3. Dynamic Projection
    <T> List<T> findByAmountGreaterThan(BigDecimal amount, Class<T> type);

    // 4. DTO Projection
    @Query("""
        SELECT new com.kshrd.springprojection.dto.projection.DashboardDto(
            COUNT(t),
            SUM(t.amount),
            SUM(CASE WHEN t.type = 'DEPOSIT' THEN 1 ELSE 0 END),
            SUM(CASE WHEN t.type = 'WITHDRAWAL' THEN 1 ELSE 0 END)
        )
        FROM Transaction t
    """)
    DashboardDto getDashboardStats();

    // 5. Pagination + Projection
    Page<TransactionSummary> findAllBy(Pageable pageable);
}
