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

    public Client(String name, int id) {
        this.name = name;
        this.id = id;
        this.bankOperation = BankOperation.getRandomBankOperation();
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
