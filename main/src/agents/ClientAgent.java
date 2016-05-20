package agents;

import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

/**
 * @author Gerardo
 */
public class ClientAgent extends Agent {

    protected void setup() {
        //esperando bienvenida

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage wakeMsg = blockingReceive();
                ACLMessage order = wakeMsg.createReply();
                order.addReceiver(wakeMsg.getSender());
                order.setPerformative(ACLMessage.REQUEST);
                //TODO: si tiene contenido,es pizza especial
                order.setContent("Quiero una pizza bien Hot & Ready.");
                send(order);
                System.out.print("Orden del cliente " + getName() + " pedida.\n");
                ACLMessage msg = blockingReceive();
                System.out.print("Promotor envio mensaje..\n");
                if (msg != null) {
                    if (msg.getContent().equals(PromotorAgent.WAIT)) {
                        System.out.println("Agente >> "+myAgent.getLocalName()+", en espera de una pizzona");
                        //myAgent.doSuspend(); // Para reactivar el agente usar: doActivate()
                        //System.out.println("Agente >> "+myAgent.getLocalName()+", suspendido");
                    } else {
                        System.out.println(msg.getContent());
                        myAgent.doDelete();
                    }
                }
            }
        });
    }
}