package com.kshrd.springprojection.dto.projection;

import java.math.BigDecimal;

public interface TransactionWithAccount {
    Long getId();
    BigDecimal getAmount();
    String getType();
    AccountView getAccount();

    interface AccountView {
        String getAccountNumber();
        String getHolderName();
    }
}
