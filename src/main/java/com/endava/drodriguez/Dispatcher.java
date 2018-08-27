package com.endava.drodriguez;

import com.endava.drodriguez.AgentFactory.AgentType;
import io.vavr.collection.List;
import io.vavr.collection.PriorityQueue;
import io.vavr.collection.Queue;
import io.vavr.control.Option;

import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton class to manage Client attention. Creates a multi-threaded pool, assigning a client to an available
 * Bank Agent and executing all the processes.
 */
public class Dispatcher implements Agent.OnAgentAvailableListener {
    /**
     * Singleton instance (static) of the Dispatcher class
     */
    private static Dispatcher instance = new Dispatcher();
    /**
     * List of agents ordered with priority. First to be busy are cashiers, then supervisors and finally directors
     */
    private PriorityQueue<Agent> agentList = PriorityQueue.empty(
            Comparator.comparingInt(a -> AgentType.agentTypeFromAgent(a).id));
//
    /**
     * Executor service to create the Thread pool
     */
    private ExecutorService executor;
    /**
     * Queue of this.clients blocked by maximum capacity
     */
    private Queue<Client> clients = Queue.empty();


    /**
     * Creates the ExecutorService for the class, and creates a list with all the available Agents.
     */
    private Dispatcher() {
        AgentFactory factory = new AgentFactory();

        List<Integer> agentNumber = List.of(6, 3, 1);
        this.agentList = this.agentList.enqueueAll(List.of(0, 1, 2)
                .map(n -> factory.getAgentList(this, AgentType.agentTypeFromId(n), agentNumber.get(n)))
                .flatMap(s -> s));

        this.executor = Executors.newFixedThreadPool(10);
        this.executor.shutdown();
    }

    public static Dispatcher getInstance() {
        return instance;
    }

    /**
     * Creates a new pool of Threads with a maximum capacity of 10 concurrent Threads. Records the beginning time of
     * the Thread pool.
     */
    private void startExecutor() {
        this.executor = Executors.newFixedThreadPool(10);
    }

    /**
     * Simulates the attention of an incoming client. If the attention this.clients is shutdown, then starts the Thread pool
     * and assigns the Client to a Available Agent and starts the attention process in a new Thread.
     *
     * @param client Incoming Client to be attended. It is added to a Blocking Queue representing the waiting line.
     */
    public void attend(Client client) {
        if (this.executor.isShutdown())
            startExecutor();
        this.clients = this.clients.append(client);
        startAttention();
    }

    /**
     * Called when the given Agent becomes available. Whenever an Agent finishes the Client processing, it
     * notifies the Dispatcher that it became available, and the Dispatcher adds it again to the Agents PriorityQueue
     * ready to attend new this.clients.
     *
     * @param agent The agent listened to. Represents the Agent that has finished its process and is available to attend
     *              another client.
     */
    @Override
    public void onAgentAvailabilityChanged(Agent agent) {
        this.agentList = this.agentList.enqueue(agent);
        if (this.clients.size() > 0)
            startAttention();
        else this.executor.shutdown();
    }

    private Client pollClient() {
        Client head = this.clients.head();
        this.clients = this.clients.tail();
        return head;
    }

    private Agent pollAgent() {
        Agent head = this.agentList.head();
        this.agentList = this.agentList.tail();
        return head;
    }

    /**
     * Creates the Thread with Completable Future for any this.clients that may be waiting in this.clients, if there are enough
     * Agents available. Elsewise, attends as much this.clients as it may be possible. In oder words, creates the number
     * of tasks equivalent to the minimum value between number of waiting this.clients, and number of available Agents.
     */
    public void startAttention() {
        int minAttentionCalls = Math.min(this.agentList.size(), this.clients.size());
        List.range(0, minAttentionCalls)
                .forEach(i -> CompletableFuture
                        .supplyAsync(Option.of(pollAgent()).get().setClient(pollClient())::call, this.executor)
                        .thenApply(Option::get)
                        .thenAccept(System.out::println));
    }
}
