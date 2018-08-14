package com.endava.drodriguez;

import java.util.ArrayList;
import java.util.List;

public class AgentFactory {

    /**
     * used to create a new agent from the given type
     * @param agentType string describing expected class of agent
     * @return new instance of the type of agent specified
     */
    public static Agent getAgent(String agentType) {
        switch (agentType) {
            case "Cashier":
                return new Cashier();
            case "Supervisor":
                return new Supervisor();
            case "Director":
                return new Supervisor();
            default:
                return null;
        }
    }

    /**
     * creates a list of agents of specific type
     * @param agentType concrete class of agents to return in list
     * @param number list size, or number of agents to return
     * @return list with new instances of Agents, all with same concrete type of Agent
     */
    public static List<Agent> getAgentList(String agentType, int number) {
        List<Agent> agentList = new ArrayList<>();

        for (int i = 0; i < number; i++)
            agentList.add(getAgent(agentType));
        return agentList;
    }

}
