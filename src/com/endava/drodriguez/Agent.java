package com.endava.drodriguez;

import java.util.concurrent.Callable;

/**
 * Bank employee attending one client at a time. Specifies a random attention time, receives the client and attends it,
 * i.e. returning a response String
 */
public abstract class Agent implements Callable<String> {

    /**
     * Client to be attended, one at a time. If null, then the Agent is available and can be assign a new client,
     * otherwise, considered busy
     */
    private Client client = null;
    /**
     * Dispatcher that is responsible to handle the Agent when it is available after the execution of a client-process
     */
    private OnAgentAvailabilityChangedListener mListener;


    public Agent setClient(Client client) {
        this.client = client;
        return this;
    }

    public void addListener(OnAgentAvailabilityChangedListener listener) {
        mListener = listener;
    }

    /**
     * States a random waiting time (sleep) and builds a response string. Finally notifies the Dispatcher (Observer)
     * that the process has been completed and the agent is now available again.
     *
     * @return Response String with agent type, and client attended
     */
    @Override
    public String call() {
        System.out.println("Beginning attending of client " + client.getId());
        if (this.client == null) return "No client was found :'v";

        //time formula is equivalent to t=1000 * ((r * (15 - 10)) + 10)
        long time = (long) (5000 * (Math.random() + 2));

        try {
            Thread.sleep(time);
            String response = String.format("I'm a %s. Just finished attending customer %s, with name %s and Operation %s. Delay time:%sms",
                    getClass().getSimpleName(), client.getId(), client.getName(), client.getBankOperation(), time);

            this.client = null;
            mListener.onAgentAvailabilityChanged(this);
            return response;
        } catch (InterruptedException e) {
            System.out.println("Exception in Thread sleep " + e);
            return null;
        }
    }

    /**
     * Determines whether the agent can receive a new Client, or already has one (busy/free)
     *
     * @return availability of agent based on having or not a client assigned
     */
    boolean isAvailable() {
        return this.client == null;
    }

    public interface OnAgentAvailabilityChangedListener {
        void onAgentAvailabilityChanged(Agent agent);
    }

}
