package com.endava.drodriguez;


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
     * @return random BankOperation
     */
    public static BankOperation getRandomBankOperation() {
        return getBankOperationById((int) (Math.random() * 3));
    }

    /**
     * given an input id, returns the related BankOperation
     *
     * @param id BankOperation identifier
     * @return BankOperation given its id
     */
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
