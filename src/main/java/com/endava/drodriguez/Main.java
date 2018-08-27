package com.endava.drodriguez;

import io.vavr.collection.List;


/**
 * Main process of the application. It creates the dispatcher, clients and relates them.
 */
public class Main {

    private static String[] names = {"Juan", "Laura", "David", "Tatiana", "Carlos", "Diana", "Camilo", "Ana", "Sebastian", "Margarita"};

    /**
     * Creates a list of clients and the Dispatcher object, and send each client to the dispatcher to be attended.
     *
     * @param args default arguments for main class
     */
    public static void main(String[] args) {
        Dispatcher d = Dispatcher.getInstance();
        createClients().forEach(d::attend);
    }

    /**
     * Creates a list of clients
     *
     * @return new list with Client objects
     */
    private static List<Client> createClients() {
        return List.range(0, 20).map(i -> new Client(i, names[i % names.length]));
    }
}