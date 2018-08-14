package com.endava.drodriguez;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Singleton class to manage Client attention. Creates a multi-threaded pool, assigning a client to an available
 * Bank Agent and executing all the processes.
 */
public class Dispatcher {
    private static Dispatcher instance;
    private List<Agent> agentList;
    private ExecutorService executor;
    private List<Future<String>> futures = new ArrayList<>();

    /**
     * Creates the ExecutorService for the class, and creates a list with all the available Agents.
     */
    private Dispatcher() {
        agentList = new ArrayList<>();
        executor = Executors.newFixedThreadPool(10);

        agentList.addAll(AgentFactory.getAgentList("Cashier", 6));
        agentList.addAll(AgentFactory.getAgentList("Supervisor", 3));
        agentList.addAll(AgentFactory.getAgentList("Director", 1));

    }

    public static Dispatcher getInstance() {
        if (instance == null)
            instance = new Dispatcher();

        return instance;
    }

    /**
     * Assigns an input client to an available Bank Agent; then creates a Future and includes it in a list of Future
     * to be completed.
     *
     * @param c client to be attended
     */
    public void attend(Client c) {
        Agent agent;
        agent = getAgent("Cashier");

        if (agent == null)
            agent = getAgent("Supervisor");

        if (agent == null)
            agent = getAgent("Director");

        if (agent == null) {
            System.out.println("No se pudo atender al cliente. Todos los agentes se encuentran ocupados");
            return;
        }


        Agent finalAgent = agent.setClient(c);
        futures.add(CompletableFuture
                .supplyAsync(finalAgent::call, executor)
                .thenApply(s -> s));
    }

    /**
     * Searches the Agent pool for an available Agent of the type given
     *
     * @param classType class type of the agent to return if available
     * @return available agent of the given type from the agent pool
     */
    private Agent getAgent(String classType) {
        return agentList.stream()
                .filter(agent -> agent.getClass().getSimpleName().equals(classType))
                .filter(Agent::isAvailable)
                .findAny()
                .orElse(null);
    }


    /**
     * For all the Futures in the waiting list, execute the process and print the result
     */
    void getAll() {
        long init = Date.from(Instant.now()).getTime();
        for (Future<String> f : futures) {
            try {
                System.out.println(f.get());
            } catch (InterruptedException | ExecutionException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        System.out.println(String.format("execution time:%ss", (Date.from(Instant.now()).getTime() - init) / 1000));
        executor.shutdown();
    }

    /**
     * closes the executor service if it is still open with a timeout of 15 seconds.
     */
    public void closeExecutor() {
        try {
            if (executor.awaitTermination(15000, TimeUnit.MILLISECONDS)) {
                System.out.println("Process finished correctly");

            } else System.out.println("Process timeout reached");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
