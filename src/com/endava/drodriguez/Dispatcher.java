package com.endava.drodriguez;

import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;
import java.util.PriorityQueue;
import java.util.concurrent.*;

/**
 * Singleton class to manage Client attention. Creates a multi-threaded pool, assigning a client to an available
 * Bank Agent and executing all the processes.
 */
public class Dispatcher implements Observer {
    private static Dispatcher instance;
    long time;
    private PriorityQueue<Agent> agentList = new PriorityQueue<>(new AgentComparator());
    private ExecutorService executor;
    private BlockingQueue<Client> queue = new ArrayBlockingQueue<>(10);

    /**
     * Creates the ExecutorService for the class, and creates a list with all the available Agents.
     */
    private Dispatcher() {
        startExecutor();
        agentList.addAll(AgentFactory.getAgentList(this, AgentFactory.AgentType.SUPERVISOR, 3));
        agentList.addAll(AgentFactory.getAgentList(this, AgentFactory.AgentType.DIRECTOR, 1));
        agentList.addAll(AgentFactory.getAgentList(this, AgentFactory.AgentType.CASHIER, 6));
    }

    public static Dispatcher getInstance() {
        if (instance == null)
            instance = new Dispatcher();

        return instance;
    }

    private void startExecutor() {
        executor = Executors.newFixedThreadPool(10);
        time = System.currentTimeMillis();
    }

    /**
     * closes the executor service if it is still open with a timeout of 15 seconds.
     */
    public void closeExecutor() {
        try {
            if (executor.awaitTermination(150000, TimeUnit.MILLISECONDS))
                System.out.println("Process finished correctly");
            else System.out.println("Process timeout reached");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
            System.out.println("Run time: " + (System.currentTimeMillis() - time));
            time = 0;
        }
    }

    public void attend(Client client) {
        if (executor.isShutdown())
            startExecutor();
        try {
            queue.put(client);
            if (queue.size() == 10)
                startAttention();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Agent)
            agentList.add((Agent) o);
        if (queue.size() > 0)
            startAttention();
        else {
            executor.shutdown();
            System.out.println("Run time: " + (System.currentTimeMillis() - time));
            time = 0;
        }

    }

    public void startAttention() {
        int availableAgentsCount = agentList.size(),
                clientsCount = queue.size();

        for (int i = 0; i < Math.min(availableAgentsCount, clientsCount); i++) {
            final Agent agent = agentList.poll();
            if (agent == null)
                return;

            agent.setClient(queue.poll());

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
            String firstAgentType = o1.getClass().getSimpleName(),
                    secondAgentType = o2.getClass().getSimpleName();
            firstAgentLevel = firstAgentType.equals("Cashier") ? 3 : (firstAgentType.equals("Supervisor") ? 2 : 1);
            secondAgentLevel = secondAgentType.equals("Cashier") ? 3 : (secondAgentType.equals("Supervisor") ? 2 : 1);

            return secondAgentLevel - firstAgentLevel;
        }
    }
}
