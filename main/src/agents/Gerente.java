package agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.wrapper.StaleProxyException;
import util.ResourcesManager;

/**
 * @author Gerardo
 */

public class Gerente extends Agent {

    int pedidos = 0;
    int currentWave = 0;

    protected void setup() {
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                // Por lo pronto, el limite sera 20 :)
                int debeHaber = ResourcesManager.numNormalClients.get(ResourcesManager.wave);
                if(currentWave != ResourcesManager.wave) {
                    currentWave = ResourcesManager.wave;
                    pedidos = 0;
                }
                if (pedidos < debeHaber) {
                    try {
                        getContainerController().createNewAgent("orden"+System.nanoTime(), "agents.OrderAgent", new Object[]{}).start();
                        System.out.println(getLocalName() + ": metan pizzas ergas");
                        ++pedidos;
                    } catch (StaleProxyException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
