package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import util.ResourcesManager;

/**
 * @author Gerardo
 */

public class Gerente extends Agent {

    protected void setup() {
        addBehaviour(new TickerBehaviour(this, 2501) {
            public void onTick() {
                // Por lo pronto, el limite sera 20 :)
                if (ResourcesManager.getPizzasLength() <= 20) {
                    ACLMessage mas_pizzas = new ACLMessage();
                    mas_pizzas.setContent("Cantidad de pizzas menor a 20, favor de ponerse a trabajar");
                    send(mas_pizzas);
                }
            }
        });
    }

}
