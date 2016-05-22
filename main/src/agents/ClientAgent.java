package agents;

import jade.lang.acl.ACLMessage;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import util.Archivo;


/**
 * @author Gerardo
 */
public class ClientAgent extends Agent {

    String elapsedTimeText;
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
                // Get current time
                long start = System.currentTimeMillis();
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
                        // Get elapsed time in milliseconds
                        long elapsedTimeMillis = System.currentTimeMillis()-start;
                        // Get elapsed time in seconds
                        float elapsedTimeSec = elapsedTimeMillis/1000F;
                        elapsedTimeText = "Agente: " + getName() + ", atendido en: " +
                                elapsedTimeSec + " segundos.\n";
                        //Archivo.guardar("archivin.txt", elapsedTimeText);
                        myAgent.doDelete();
                    }
                }
            }
        });
    }
}