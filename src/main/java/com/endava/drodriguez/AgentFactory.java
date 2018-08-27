package com.endava.drodriguez;

import io.vavr.collection.Array;
import io.vavr.collection.List;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


/**
 * Responsible for creating new instances of Agents of the given type. It also creates lists of Agents
 */
public class AgentFactory {
    private Map<AgentType, Supplier<Agent>> map;

    AgentFactory(){
        map = new HashMap<>();
        map.put(AgentType.CASHIER, Cashier::new);
        map.put(AgentType.SUPERVISOR, Supervisor::new);
        map.put(AgentType.DIRECTOR, Director::new);
    }

    /**
     * Creates a new agent from the given type and relates the Dispatcher as the Observer object.
     *
     * @param agentType type of agent describing expected class of agent
     * @return new instance of the type of agent specified
     */
    public Agent getAgent(Agent.OnAgentAvailableListener listener, AgentType agentType) {
        return map.get(agentType).get().addListener(listener);
    }

    /**
     * Creates a list of agents of specific type
     *
     * @param listener  Observer object responsible of handling Agent availability
     * @param agentType Concrete class of agents to return in list
     * @param listSize  List size, or listSize of agents to return
     * @return List with new instances of Agents, all with same concrete type of Agent
     */
    public List<Agent> getAgentList(Agent.OnAgentAvailableListener listener, AgentType agentType, int listSize) {
        return List.range(0, listSize).map(i -> getAgent(listener, agentType));
    }

    enum AgentType {
        CASHIER(0), SUPERVISOR(1), DIRECTOR(2);
        int id;

        AgentType(int id) {
            this.id = id;
        }

        static AgentType agentFromString(String className) {
            return Array.of(values()).filter(a -> a.toString().equalsIgnoreCase(className)).head();
        }

        static AgentType agentTypeFromAgent(Agent agent) {
            return AgentType.agentFromString(agent.getClass().getSimpleName());
        }

        static AgentType agentTypeFromId(int id){
            return Array.of(values()).filter(i->i.id==id).head();
        }
    }
}
