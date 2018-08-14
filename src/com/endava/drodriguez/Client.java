package com.endava.drodriguez;

public class Client {
    private BankOperation bankOperation;
    private String name;
    private int id;

    public Client(int id, String name, BankOperation bankOperation) {
        this.bankOperation = bankOperation;
        this.name = name;
        this.id = id;
    }

    /**
     * Initializes Client object assigning a random operation
     * @param id identifier of the Client in th system
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
