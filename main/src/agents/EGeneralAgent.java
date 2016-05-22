package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import util.Pizza;
import util.ResourcesManager;

import java.util.Date;

/**
 * Created by nancio on 17/05/16.
 */
public class EGeneralAgent extends Agent {
    public static final String SERVICE = "service-empleado-general";
    Object args[] = getArguments();

    public void setup(){
        System.out.println("Agent " + getLocalName());
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                    //getContainerController().getAgent("cliente").activate();
                }
        });
    }

}
