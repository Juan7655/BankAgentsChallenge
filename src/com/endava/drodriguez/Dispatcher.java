package com.endava.drodriguez;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.*;

/**
 * Singleton class to manage Client attention. Creates a multi-threaded pool, assigning a client to an available
 * Bank Agent and executing all the processes.
 */
public class Dispatcher implements Agent.OnAgentAvailabilityChangedListener {
    /**
     * Singleton instance (static) of the Dispatcher class
     */
    private static Dispatcher instance;
    /**
     * List of agents ordered with priority. First to be busy are cashiers, then supervisors and finally directors
     */
    private PriorityQueue<Agent> agentList = new PriorityQueue<>(new AgentComparator());
    /**
     * Executor service to create the Thread pool
     */
    private ExecutorService executor;
    /**
     * Queue of clients blocked by maximum capacity
     */
    private BlockingQueue<Client> clients = new ArrayBlockingQueue<>(10);

    /**
     * Creates the ExecutorService for the class, and creates a list with all the available Agents.
     */
    private Dispatcher() {
        agentList.addAll(AgentFactory.getAgentList(this, AgentFactory.AgentType.SUPERVISOR, 3));
        agentList.addAll(AgentFactory.getAgentList(this, AgentFactory.AgentType.DIRECTOR, 1));
        agentList.addAll(AgentFactory.getAgentList(this, AgentFactory.AgentType.CASHIER, 6));
    }

    public static Dispatcher getInstance() {
        if (instance == null)
            instance = new Dispatcher();

        return instance;
    }

    /**
     * Creates a new pool of Threads with a maximum capacity of 10 concurrent Threads. Records the beginning time of
     * the Thread pool.
     */
    private void startExecutor() {
        executor = Executors.newFixedThreadPool(10);
    }

    /**
     * Simulates the attention of an incoming client. If the attention clients is shutdown, then starts the Thread pool
     * and assigns the Client to a Available Agent and starts the attention process in a new Thread.
     *
     * @param client Incoming Client to be attended. It is added to a Blocking Queue representing the waiting line.
     */
    public void attend(Client client) {
        if (executor == null || executor.isShutdown())
            startExecutor();
        try {
            clients.put(client);
            startAttention();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the given Agent becomes available. Whenever an Agent finishes the Client processing, it
     * notifies the Dispatcher that it became available, and the Dispatcher adds it again to the Agents PriorityQueue
     * ready to attend new clients.
     *
     * @param agent The agent listened to. Represents the Agent that has finished its process and is available to attend
     *              another client.
     */
    @Override
    public void onAgentAvailabilityChanged(Agent agent) {
        agentList.add(agent);
        if (clients.size() > 0)
            startAttention();
        else executor.shutdown();

    }

    /**
     * Creates the Thread with Completable Future for any Clients that may be waiting in clients, if there are enough
     * Agents available. Elsewise, attends as much clients as it may be possible. In oder words, creates the number
     * of tasks equivalent to the minimum value between number of waiting Clients, and number of available Agents.
     */
    public void startAttention() {
        int availableAgentsCount = agentList.size(),
                clientsCount = clients.size();

        for (int i = 0; i < Math.min(availableAgentsCount, clientsCount); i++) {
            final Agent agent = agentList.poll();
            if (agent == null)
                return;

            agent.setClient(clients.poll());

            CompletableFuture
                    .supplyAsync(agent::call, executor)
                    .thenAccept(System.out::println);
        }
    }

    /**
     * Compares the Agents by the level of responsibility. That is, the Cashiers have the first use priority, the
     * Supervisors are next, and at last, the Directors.
     */
    private class AgentComparator implements Comparator<Agent> {
        @Override
        public int compare(Agent o1, Agent o2) {
            int firstAgentLevel, secondAgentLevel;

            //which type of Agents are the objects. Save as String
            String firstAgentType = o1.getClass().getSimpleName(),
                    secondAgentType = o2.getClass().getSimpleName();

            //Assign a number value corresponding with the rank of the Agent.
            firstAgentLevel = firstAgentType.equals("Cashier") ? 2 : (firstAgentType.equals("Supervisor") ? 1 : 0);
            secondAgentLevel = secondAgentType.equals("Cashier") ? 2 : (secondAgentType.equals("Supervisor") ? 1 : 0);

            return secondAgentLevel - firstAgentLevel;
        }
    }
}
