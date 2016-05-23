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
    private String puesto;//debe ser  igual al args Horno

    public void setup(){
        System.out.println("Agent " + getLocalName());
        Object args[] = getArguments();
        if(args.length > 0) puesto = (String)args[0];//obtenemos si es peperoniador o e horno
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                if(puesto.equals("Horno")) {
                    OrderAgent orden = ResourcesManager.removeFromHorno();
                    if(orden == null) return;
                    ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
                    mensaje.setContent(OrderAgent.REPLY_SACANDO_HORNO);
                    mensaje.addReceiver(orden.getAID());
                    send(mensaje);
                    ACLMessage msg = blockingReceive();//este mensaje se lo deberia de enviar el peperoniador para que se sepa que hay pizza en el horno
                    //Esta listo y se lo pasamos al promotor
                    System.out.println(getLocalName() + ": Orden terminada");
                    ACLMessage listo = new ACLMessage(ACLMessage.INFORM);
                    listo.setContent(orden.clientName);
                    listo.addReceiver(new AID(orden.promotor, true));
                    send(listo);

                }else {//entonces es peperoniador
                    OrderAgent orden = ResourcesManager.removeOrder();
                    if(orden == null) return;
                    ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
                    mensaje.setContent(OrderAgent.REPLY_ENTRA_ORDEN);
                    mensaje.addReceiver(orden.getAID());
                    send(mensaje);
                    mensaje = blockingReceive();

                }
            }
        });
    }
}
