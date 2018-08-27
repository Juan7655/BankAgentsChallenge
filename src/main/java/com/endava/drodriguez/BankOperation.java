package com.endava.drodriguez;


import io.vavr.collection.Stream;

/**
 * The transaction type that a client can make at the Bank Application
 */
public enum BankOperation {
    DEPOSITS(0),
    WITHDRAWALS(1),
    CUSTOMER_ISSUES(2);

    private final int id;

    BankOperation(int id) {
        this.id = id;
    }

    /**
     * Generates a random integer from 0 to 2 and returns a related operation, i.e. Operation 1, Operation 2, etc.
     *
     * @return random BankOperation
     */
    public static BankOperation getRandomBankOperation() {
        return getBankOperationById((int) (Math.random() * 3));
    }

    /**
     * Given an input id, returns the related BankOperation
     *
     * @param id BankOperation identifier
     * @return BankOperation given its id
     */
    public static BankOperation getBankOperationById(int id) {
        return Stream.of(BankOperation.values()).filter(b -> b.id == id).getOrElse(DEPOSITS);
    }
}
