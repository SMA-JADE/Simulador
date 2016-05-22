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
    private String estadoOrden;

    public void setup(){

        Object args[] = getArguments();
        if(args.length > 0) estadoOrden = (String)args[0];

        System.out.println("Agent " + getLocalName());
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getPerformative() == ACLMessage.INFORM) {
                    if (estadoOrden == "pizza en crescor") {
                        ResourcesManager.removeOrder();
                        ACLMessage listo = new ACLMessage(ACLMessage.INFORM);
                        listo.addReceiver(msg.getSender());
                        send(listo);
                    } else {
                        myAgent.doWait(5000);
                    }
                }
            }
        });
    }

}
