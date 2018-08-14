package com.endava.drodriguez;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * Bank employee attending one client at a time. Specifies a random attention time, receives the client and attends it,
 * i.e. returning a response String
 */
public abstract class Agent implements Callable<String>{

    private Client client = null;


    public Agent setClient(Client client) {
        this.client = client;
        return this;
    }

    /**
     * States a random waiting time (sleep) and builds a response string
     * @return Response String with agent type, and client attended
     */
    @Override
    public String call() {
        if (this.client == null) return "No client was found :'v";
        //time formula is equivalent to t=1000 * ((r * (15 - 10)) + 10)
        long time = (long) (5000 * (Math.random() + 2));

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println("Exception in Thread sleep " + e);
        }

        String response = String.format("I'm a %s. Just finished attending customer %s, with name %s and Operation %s. Delay time:%sms",
                getClass().getSimpleName(), client.getId(), client.getName(), client.getBankOperation(), time);

        this.client = null;
        return response;
    }

    /**
     * Determines whether the agent can receive a new Client, or already has one (busy/free)
     * @return availability of agent based on having or not a client assigned
     */
    boolean isAvailable() {
        return this.client == null;
    }

}
