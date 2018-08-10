package com.endava.drodriguez;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Dispatcher {
    private static Dispatcher instance;
    private List<Agent> agentList;
    private ExecutorService executor;

    private Dispatcher() {
        agentList = new ArrayList<>();
        executor = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 3; i++)
            agentList.add(new Cashier());

        for (int i = 0; i < 2; i++)
            agentList.add(new Supervisor());

        for (int i = 0; i < 1; i++)
            agentList.add(new Director());

    }

    public static Dispatcher getInstance() {
        if (instance == null)
            instance = new Dispatcher();

        return instance;
    }

    public void attend(Client c) {
        Agent agent;
        agent = agentList.stream()
                .filter(agentItem -> agentItem instanceof Cashier)
                .filter(Agent::isAvailable)
                .limit(1)
                .findAny()
                .orElse(null);

        if (agent == null)
            agent = agentList.stream()
                    .filter(agentItem -> agentItem instanceof Supervisor)
                    .filter(Agent::isAvailable)
                    .limit(1)
                    .findAny()
                    .orElse(null);

        if (agent == null)
            agent = agentList.stream()
                    .filter(agentItem -> agentItem instanceof Director)
                    .filter(Agent::isAvailable)
                    .limit(1)
                    .findAny()
                    .orElse(null);

        if (agent == null) {
            System.out.println("No se pudo atender al cliente. Todos los agentes se encuentran ocupados");
            return;
        }

        agent.setClient(c);
        CompletableFuture
                .supplyAsync(agent, executor)
                .thenAccept(System.out::println);

    }

    public void closeExecutor() {
        try {
            if(executor.awaitTermination(30000, TimeUnit.MILLISECONDS))
                System.out.println("Process finished correctly");
            else System.out.println("Process timeout reached");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            executor.shutdown();
        }
    }
}
