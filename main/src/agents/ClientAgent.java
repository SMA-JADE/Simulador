package agents;


import jade.lang.acl.ACLMessage;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import util.Archivo;
import util.ResourcesManager;

import java.util.Random;

/**
 * @author Gerardo
 */
public class ClientAgent extends Agent {

    String elapsedTimeText;
    long start;
    Random rand;
    float randonNum;
    protected void setup() {
        //esperando bienvenida

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage wakeMsg = blockingReceive();
                ACLMessage order = wakeMsg.createReply();
                order.addReceiver(wakeMsg.getSender());
                order.setPerformative(ACLMessage.REQUEST);

                randonNum = rand.nextFloat();

                if (randonNum > (ResourcesManager.numSpecialClients/(ResourcesManager.numSpecialClients+
                        ResourcesManager.numNormalClients))) {
                    order.setContent("Quiero una pizza bien Hot & Ready.");
                    ResourcesManager.numNormalClients--;
                } else {
                    ResourcesManager.numSpecialClients--;
                }
                send(order);
                // Get current time
                start = System.currentTimeMillis();
                System.out.print(getLocalName() + ": Orden pedida.\n");
                ACLMessage msg = blockingReceive();
                System.out.print(getLocalName() + ": Promotor envio mensaje..\n");
                if (msg != null) {
                    if (msg.getContent().equals(PromotorAgent.WAIT)) {
                        System.out.println(getLocalName()+", en espera de una pizzona");
                        //myAgent.doSuspend(); // Para reactivar el agente usar: doActivate()
                        //System.out.println("Agente >> "+myAgent.getLocalName()+", suspendido");
                        msg = blockingReceive();

                        System.out.println(getLocalName() + ": porfin bye");
                        System.out.println(getLocalName() + ": msg=" + msg.toString());
                    }else {
                        System.out.println(getLocalName() + ": yey bye");
                    }
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
                    doDelete();
                }
            }
        });
    }
}