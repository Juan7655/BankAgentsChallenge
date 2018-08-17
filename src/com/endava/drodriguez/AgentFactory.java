package com.endava.drodriguez;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;


/**
 * Responsible for creating new instances of Agents of the given type. It also creates lists of Agents
 */
public class AgentFactory {

    /**
     * Creates a new agent from the given type and relates the Dispatcher as the Observer object.
     * @param agentType type of agent describing expected class of agent
     * @return new instance of the type of agent specified
     */
    public static Agent getAgent(Observer o, AgentType agentType) {
        Agent agent;
        switch (agentType) {
            case CASHIER:
                agent = new Cashier();
                break;
            case SUPERVISOR:
                agent = new Supervisor();
                break;
            case DIRECTOR:
                agent = new Director();
                break;
            default:
                return null;
        }

        agent.addObserver(o);
        return agent;
    }

    /**
     * Creates a list of agents of specific type
     * @param o Observer object responsible of handling Agent availability
     * @param agentType Concrete class of agents to return in list
     * @param listSize List size, or listSize of agents to return
     * @return List with new instances of Agents, all with same concrete type of Agent

     */
    public static List<Agent> getAgentList(Observer o, AgentType agentType, int listSize) {
        List<Agent> agentList = new ArrayList<>();

        for (int i = 0; i < listSize; i++)
            agentList.add(getAgent(o, agentType));
        return agentList;
    }

    enum AgentType{
        CASHIER, SUPERVISOR, DIRECTOR
    }

}
