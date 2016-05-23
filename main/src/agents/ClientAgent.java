package agents;


import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import util.Archivo;
import util.ResourcesManager;


/**
 * @author Gerardo
 */
public class ClientAgent extends Agent {

    String elapsedTimeText;
    long start;
    double randonNum;
    protected void setup() {
        //esperando bienvenida

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage wakeMsg = blockingReceive();
                ACLMessage order = wakeMsg.createReply();
                order.addReceiver(wakeMsg.getSender());
                order.setPerformative(ACLMessage.REQUEST);

                randonNum = Math.random();
                int wave = (int)getArguments()[0];
                int specials = ResourcesManager.numSpecialClients.get(wave);
                int normales = ResourcesManager.numNormalClients.get(wave);
                if (randonNum > (specials/(normales+specials))) {
                    order.setContent("Quiero una pizza bien Hot & Ready.");
                    ResourcesManager.numSpecialClients.set(wave, specials-1);
                } else {
                    ResourcesManager.numNormalClients.set(wave, normales-1);
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