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
    private String estadoOrden;//debe ser  igual al args Horno

    public void setup(){
        System.out.println("Agent " + getLocalName());
        Object args[] = getArguments();
        if(args.length > 0) estadoOrden = (String)args[0];
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                OrderAgent orden = ResourcesManager.removeOrder();
                ACLMessage msg = receive();
                if (msg != null && msg.getPerformative() == ACLMessage.INFORM) {
                    ACLMessage verificar = new ACLMessage();
                    verificar.setContent("sacando del horno");
                    verificar.addReceiver(msg.getSender());
                    send(verificar);
                    msg = blockingReceive();
                    if (OrderAgent.MSG_EN_CRESCOR.equals(msg.getContent())) {//Falta obtener la orden y saber su estado
                        ACLMessage listo = new ACLMessage(ACLMessage.INFORM);
                        listo.setContent("Orden Terminada");
                        listo.addReceiver(new AID(orden.promotor));
                        send(listo);
                    } else {
                        myAgent.doWait(5000);
                    }
                }
            }
        });
    }
}
