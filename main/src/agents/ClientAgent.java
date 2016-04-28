package agents;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

/**
 * Created by gerardo on 27/04/16.
 */
public class ClientAgent extends Agent {

    protected void setup() {
        ACLMessage order = new ACLMessage();
        order.addReceiver(new AID("PromotorAgent", false));
        order.setContent("Quiero una hamburgesa.");
        send(order);
        System.out.print("Orden del cliente "+getName()+" pedida.\n");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
//                if (msg.getSender().getName() == "PromotorAgent") {
                System.out.print("Promotor envio mensaje..\n");
                if (msg != null) {
                    System.out.println(msg.getContent());
                    myAgent.doDelete();
                } else {
                    block();
                }
//                }
            }
        } );
    }
}