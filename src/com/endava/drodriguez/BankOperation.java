package com.endava.drodriguez;

public enum BankOperation {
    DEPOSITS(0),
    WITHDRAWALS(1),
    CUSTOMER_ISSUES(2);

    private final int id;

    BankOperation(int id) {
        this.id = id;
    }

    public static BankOperation getRandomBankOperation() {
        return getBankOperationById((int) (Math.random() * 3));
    }

    public static BankOperation getBankOperationById(int id) {
        switch (id) {
            case 0:
                return DEPOSITS;
            case 1:
                return WITHDRAWALS;
            case 2:
                return CUSTOMER_ISSUES;
            default:
                return null;
        }
    }


}
