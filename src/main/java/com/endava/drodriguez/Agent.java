package com.endava.drodriguez;

import io.vavr.control.Option;

import java.util.concurrent.Callable;

/**
 * Bank employee attending one client at a time. Specifies a random attention time, receives the client and attends it,
 * i.e. returning a response String
 */
public abstract class Agent implements Callable<Option> {

    /**
     * Client to be attended, one at a time. If null, then the Agent is available and can be assign a new client,
     * otherwise, considered busy
     */
    private Option<Client> client = Option.none();
    /**
     * Dispatcher that is responsible to handle the Agent when it is available after the execution of a client-process
     */
    private OnAgentAvailableListener mListener;


    public Agent setClient(Client client) {
        this.client = Option.of(client);
        return this;
    }

    public Agent addListener(OnAgentAvailableListener listener) {
        this.mListener = listener;
        return this;
    }

    /**
     * States a random waiting time (sleep) and builds a response string. Finally notifies the Dispatcher (Observer)
     * that the process has been completed and the agent is now available again.
     *
     * @return Response String with agent type, and client attended
     */
    @Override
    public Option<String> call() {
        if(client.isEmpty()) return Option.of("No client was found :'v");

        System.out.println("Beginning attending of client " + client.get().getId());

        //time formula is equivalent to t=1000 * ((r * (15 - 10)) + 10)
        long time = (long) (5000 * (Math.random() + 2));

        try {
            Thread.sleep(time);
            String response = String.format("I'm a %s. Just finished attending customer %s, " +
                            "with name %s " +
                            "and Operation %s. " +
                            "Delay time:%sms",
                    getClass().getSimpleName(),
                    client.get().getId(),
                    client.get().getName(),
                    client.get().getBankOperation(),
                    time);

            this.client = Option.none();
            mListener.onAgentAvailabilityChanged(this);
            return Option.of(response);
        } catch (InterruptedException e) {
            System.out.println("Exception in Thread sleep " + e);
            return Option.none();
        }
    }

    public interface OnAgentAvailableListener {
        void onAgentAvailabilityChanged(Agent agent);
    }

}
