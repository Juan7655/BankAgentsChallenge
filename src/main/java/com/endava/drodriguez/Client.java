package com.endava.drodriguez;

/**
 * Data Class representing a Client to be attended in the bank. Has a name, id and an operation to execute in the bank.
 */
public class Client {
    /**
     * Operation to execute at the Bank
     */
    private BankOperation bankOperation;
    /**
     * Name of the client to be attended
     */
    private String name;
    /**
     * Identifier of the client
     */
    private int id;

    public Client(int id, String name, BankOperation bankOperation) {
        this.bankOperation = bankOperation;
        this.name = name;
        this.id = id;
    }

    /**
     * Initializes Client object assigning a random operation
     *
     * @param id   identifier of the Client in th system
     * @param name of the given client
     */
    public Client(int id, String name) {
        this(id, name, BankOperation.getRandomBankOperation());
    }

    public BankOperation getBankOperation() {
        return bankOperation;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
