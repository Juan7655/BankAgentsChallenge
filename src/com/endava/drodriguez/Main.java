package com.endava.drodriguez;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static String[] names = {"Juan", "Laura", "David", "Tatiana", "Carlos", "Diana", "Camilo", "Ana", "Sebastián", "Margarita"};

    public static void main(String[] args) {
        List<Client> clients = createClients();
        Dispatcher d = Dispatcher.getInstance();

        for(Client c:clients)
            d.attend(c);


        d.closeExecutor();
    }

    private static List<Client> createClients() {
        List<Client> clients = new ArrayList<>();

        for (int i = 0; i < 10; i++)
            clients.add(new Client(i, names[i], BankOperation.getRandomBankOperation()));

        return clients;
    }
}
