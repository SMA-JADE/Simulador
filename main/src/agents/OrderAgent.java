package agents;

import behaviours.PizzaState;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;

/**
 * Created by nancio on 22/04/16.
 * Este agente se encarga de manejar un pedido por el proceso de peperonniar y horno
 */
public class OrderAgent extends Agent {
    @Override
    protected void setup() {
        FSMBehaviour fsm = new FSMBehaviour(this);

        //TODO: ResourcesAgent.enterOrder(this)

        fsm.registerFirstState(new PizzaState(this, 0, () -> {
            System.out.println("esperando...");
            return -1;
        }), "a");

        fsm.registerState(new PizzaState(this, 5000, () -> {
            System.out.println("Pizza peperoniada");
            return -1;
        }), "b");
        fsm.registerState(new PizzaState(this, 5000, () -> {
            System.out.println("Pizza saliendo del horno");
            return -1;
        }), "c");

        fsm.registerLastState(new PizzaState(this, 5000, () -> {
            System.out.println("Pizza en crescor");
            return -1;
        }), "d");

        fsm.registerDefaultTransition("a", "b");
        fsm.registerDefaultTransition("b", "c");
        fsm.registerDefaultTransition("c", "d");
        addBehaviour(fsm);
        super.setup();
    }
}

