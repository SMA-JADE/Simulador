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
    long start;
    protected void setup() {
        //esperando bienvenida

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage wakeMsg = blockingReceive();
                if (wakeMsg.getContent().equals("hot n ready")){
                    System.out.println(wakeMsg.getContent() + " LISTOOOOO");
                    // Get elapsed time in milliseconds
                    long elapsedTimeMillis = System.currentTimeMillis()-start;
                    // Get elapsed time in seconds
                    float elapsedTimeSec = elapsedTimeMillis/1000F;
                    elapsedTimeText = "\nAgente: " + getName() + "\n" +
                            "Tiempo en ser atendido (seg): " + elapsedTimeSec + "\n" +
                            "Pizza entregada por: " + wakeMsg.getSender().getLocalName() + "\n";
                    Archivo.guardar("archivin.txt", elapsedTimeText);
                    myAgent.doDelete();
                    System.out.println("Me mori: " + getLocalName());
                }
                ACLMessage order = wakeMsg.createReply();
                order.addReceiver(wakeMsg.getSender());
                order.setPerformative(ACLMessage.REQUEST);
                //TODO: si tiene contenido,es pizza especial
                order.setContent("Quiero una pizza bien Hot & Ready.");
                send(order);
                // Get current time
                start = System.currentTimeMillis();
                System.out.print("Orden del cliente " + getName() + " pedida.\n");
                ACLMessage msg = blockingReceive();
                System.out.print("Promotor envio mensaje..\n");
                if (msg != null) {
                    if (msg.getContent().equals(PromotorAgent.WAIT)) {
                        System.out.println("Agente >> "+myAgent.getLocalName()+", en espera de una pizzona");
                        //myAgent.doSuspend(); // Para reactivar el agente usar: doActivate()
                        //System.out.println("Agente >> "+myAgent.getLocalName()+", suspendido");
                    }else
                        doDelete();
                }
            }
        });
    }
}