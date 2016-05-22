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
                    ACLMessage mensaje = new ACLMessage();
                    ACLMessage msg = receive();//este mensaje se lo deberia de enviar el peperoniador para que se sepa que hay pizza en el horno
                    if (msg != null && msg.getPerformative() == ACLMessage.INFORM) {
                        ACLMessage verificar = new ACLMessage();
                        verificar.setContent(OrderAgent.REPLY_SACANDO_HORNO);
                        verificar.addReceiver(msg.getSender());//aqui seria al igual que el de abajo del peperoniador
                        send(verificar);
                        msg = blockingReceive();
                        if (OrderAgent.MSG_EN_CRESCOR.equals(msg.getContent())) {//Esta listo y se lo pasamos al promotor
                            ACLMessage listo = new ACLMessage(ACLMessage.INFORM);
                            listo.setContent("Orden Terminada");
                            //listo.addReceiver(new AID(orden.promotor));
                            send(listo);
                        } else {
                            myAgent.doWait(5000);
                        }
                    }
                }else{//entonces es peperoniador
                    OrderAgent orden = ResourcesManager.removeOrder();
                    ACLMessage mensaje = new ACLMessage();
                    mensaje.setContent(OrderAgent.REPLY_ENTRA_ORDEN);
                    mensaje.addReceiver(new AID(orden.getOrderName()));
                    send(mensaje);
                    mensaje = blockingReceive();
                    if(mensaje.getContent().equals(OrderAgent.MSG_VESTIDA)){//aqui ya esta en el horno
                        blockingReceive();//deberia de empezar a sacar otra orden
                    }

                }
            }
        });
    }
}
