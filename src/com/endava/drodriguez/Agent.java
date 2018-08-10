package com.endava.drodriguez;

import java.util.function.Supplier;

public abstract class Agent implements Supplier<String> {

    String agentType;
    private boolean available = true;
    private Client client = null;

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String get() {
        this.available = false;
        if (this.client == null) return "No client was found :'v";
        //time formula is equivalent to t=1000 * ((Math.random() * (15 - 10)) + 10)
        long time = (long) (5000 * (Math.random() + 2));

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println("Exception in Thread sleep " + e);
        }

        this.client = null;
        this.available = true;
        return String.format("I'm a %s. Just finished attending customer %s, with name %s and Operation %s",
                agentType, client.getId(), client.getName(), client.getBankOperation());
    }

    boolean isAvailable() {
        return this.available;
    }
}
