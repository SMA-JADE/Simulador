package util;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

/**
 * Created by ernesto on 19/05/16.
 */
public class ClientsWave extends TickerBehaviour {
    int count, num_clients;
    PlatformController container;
    String wave;

    public ClientsWave(Agent a, long period, int num_clients, String wave) {
        super(a, period);
        this.num_clients = num_clients;
        count = 0;
        container = a.getContainerController();
        this.wave = wave;
    }

    @Override
    protected void onTick() {
        String localName = wave + "-client-" + count;
        try {
            AgentController client = container.createNewAgent(localName, "agents.ClientAgent", null);
            ResourcesManager.addClient(client);
            client.start();
            count++;
            if(count == num_clients){
                stop();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


