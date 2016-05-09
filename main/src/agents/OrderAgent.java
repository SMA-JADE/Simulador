package agents;

import util.Pizza;
import behaviours.PizzaState;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import util.ResourcesManager;

/**
 * Created by nancio on 22/04/16.
 * Este agente se encarga de manejar un pedido por el proceso de peperonniar y horno.
 * PromotorAgent crea un OrderAgent que sera manejado por sus procesos con ayuda del
 * empleado general
 */
public class OrderAgent extends Agent {

    /**
     * Contenido del mensaje que manda el empleado general para empezar el proceso
     */
    public static String REPLY_ENTRA_ORDEN = "entra una";
    /**
     * Contenido del mensaje que manda OrderAgent para terminar de vestir
     */
    public static String MSG_VESTIDA = "pizza peperonniada";
    /**
     * Contendio del mensaje que manda empleado general para sacar pizza del horno
     */
    public static String REPLY_SACANDO_HORNO = "sacando del horno";
    /**
     * Contenido del mensaje que manda OrderAgent para dejar pizza en crescor (cola de
     * pizzas listas)
     */
    public static String MSG_EN_CRESCOR = "pizza en crescor";

    public static int SUCCESS = 1, FAIL = 0;
    private ACLMessage msg;

    @Override
    protected void setup() {
        FSMBehaviour fsm = new FSMBehaviour(this);

        //TODO: empleado general tiene que estar pendiente de las ordenes entrantes

        fsm.registerFirstState(new PizzaState(this, 0, () -> {
            System.out.println("esperando...");
            msg = blockingReceive();
            return msg.getPerformative() == ACLMessage.AGREE
                    && msg.getContent().equals(REPLY_ENTRA_ORDEN) ? SUCCESS : FAIL;
            //TODO: empleado general deberá bloquearse esperando respuesta
        }), "a");

        fsm.registerState(new PizzaState(this, ResourcesManager.TIEMPO_VESTIDO, () -> {
            System.out.println("Pizza peperoniada");
            ACLMessage response = msg.createReply();
            response.setContent(MSG_VESTIDA);
            send(response);
            return SUCCESS;
            // se desocupa empleado general
        }), "b");

        fsm.registerState(new PizzaState(this, ResourcesManager.TIEMPO_HORNO, () -> {
            System.out.println("Pizza saliendo del horno");
            //TODO: empleado general tiene que estar pendiente del horno
            msg = blockingReceive(ResourcesManager.TIEMPO_QUEMADO);
            if(msg == null){
                System.out.println("me quemé");
                return 0;
            }
            return msg.getContent().equals(REPLY_SACANDO_HORNO) ? SUCCESS : FAIL;
            //TODO: empleado general deberá bloquearse esperando respuesta
        }), "c");

        fsm.registerLastState(new PizzaState(this, ResourcesManager.TIEMPO_CORTADO, () -> {
            System.out.println("Pizza en crescor");
            ACLMessage response = msg.createReply();
            response.setContent(MSG_EN_CRESCOR);
            send(response);
            // se desocupa empleado general
            ResourcesManager.addPizza(new Pizza(false));
            this.doDelete();
            return SUCCESS;
        }), "d");

        fsm.registerDefaultTransition("a","a",new String[]{"a"});
        fsm.registerTransition("a", "b", 1);
        fsm.registerDefaultTransition("b", "c");
        fsm.registerDefaultTransition("c", "a",new String[]{"a", "b", "c"});
        fsm.registerTransition("c", "d", 1);
        addBehaviour(fsm);
        super.setup();
    }
}

