package com.endava.drodriguez;

import java.util.concurrent.Callable;

public abstract class Agent implements Callable<String> {

    private Client client = null;


    public void setClient(Client client) {
        this.client = client;
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

        String response = String.format("I'm a %s. Just finished attending customer %s, with name %s and Operation %s",
                getClass().getSimpleName(), client.getId(), client.getName(), client.getBankOperation());

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
