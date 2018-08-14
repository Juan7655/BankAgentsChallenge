package com.endava.drodriguez;

import java.util.ArrayList;
import java.util.List;


/**
 * Main process of the application. It creates the dispatcher, clients and relates them.
 */
public class Main {

    private static String[] names = {"Juan", "Laura", "David", "Tatiana", "Carlos", "Diana", "Camilo", "Ana", "Sebastián", "Margarita"};

    /**
     * creates a list of clients and the Dispatcher object, and send each client to the dispatcher to be attended.
     * @param args
     */
    public static void main(String[] args) {
        List<Client> clients = createClients();
        Dispatcher d = Dispatcher.getInstance();

        clients.forEach(d::attend);

        d.getAll();
        d.closeExecutor();
    }

    /**
     * Creates a list of clients
     * @return new list with Client objects
     */
    private static List<Client> createClients() {
        List<Client> clients = new ArrayList<>();

        for (int i = 0; i < 10; i++)
            clients.add(new Client(i, names[i]));

        return clients;
    }
}
