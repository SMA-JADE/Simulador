package agents;

import behaviours.PizzaState;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import util.Pizza;
import util.ResourcesManager;

/**
 * Created by nancio on 22/04/16.
 * Este agente se encarga de manejar un pedido por el proceso de peperonniar y horno.
 * PromotorAgent crea un OrderAgent que sera manejado por sus procesos con ayuda del
 * empleado general
 */
public class OrderAgent extends Agent {


    /**
     * Contenido del mensaje que manda el OrderAgent para empezar el proceso
     */
    public static final String MSG_ENTRA_ORDEN = "sale una";
    /**
     * Contenido del mensaje que manda el empleado general para empezar el proceso
     */
    public static final String REPLY_ENTRA_ORDEN = "entra una";
    /**
     * Contenido del mensaje que manda OrderAgent para terminar de vestir
     */
    public static final String MSG_VESTIDA = "pizza peperonniada";
    /**
     * Contendio del mensaje que manda empleado general para sacar pizza del horno
     */
    public static final String MSG_SACAR_HORNO = "sacame del horno";
    /**
     * Contendio del mensaje que manda empleado general para sacar pizza del horno
     */
    public static final String REPLY_SACANDO_HORNO = "sacando del horno";
    /**
     * Contenido del mensaje que manda OrderAgent para dejar pizza en crescor (cola de
     * pizzas listas)
     */
    public static final String MSG_EN_CRESCOR = "pizza en crescor";

    public static final int SUCCESS = 1, FAIL = 0;
    private ACLMessage msg;
    public String clientName = null, order = null, promotor = null;

    @Override
    protected void setup() {
        FSMBehaviour fsm = new FSMBehaviour(this);//maquina de estados

        //crear agente con nombre del cliente y orden
        Object args[] = getArguments();
        if(args.length > 0) clientName = ((String)args[0]);
        if(args.length > 1) order = (String)args[1];
        if (args.length > 2) promotor = (String)args[2];

        fsm.registerFirstState(new PizzaState(this, 0, () -> {//funcion lambda
            System.out.println(getLocalName() + ": Esperando...");
            //meterse a la cola
            ResourcesManager.addOrder(this);//metemos la orden a la cola
            msg = blockingReceive();
            return msg.getContent().equals(REPLY_ENTRA_ORDEN) ? SUCCESS : FAIL;
            //TODO: empleado general deberá bloquearse esperando respuesta
        }), "a");

        fsm.registerState(new PizzaState(this, ResourcesManager.TIEMPO_VESTIDO, () -> {//aweb va a pasar al estado
            System.out.println(getLocalName() + ": Pizza peperoniada");
            ACLMessage response = msg.createReply();
            response.setContent(MSG_VESTIDA);
            send(response);
            return SUCCESS;
            // se desocupa empleado general
        }), "b");

        fsm.registerState(new PizzaState(this, ResourcesManager.TIEMPO_HORNO, () -> {
            System.out.println(getLocalName() + ": Pizza saliendo del horno");
            //meterse a la cola
            ResourcesManager.addToHorno(this);
            msg = blockingReceive();
            return msg.getContent().equals(REPLY_SACANDO_HORNO) ? SUCCESS : FAIL;
            //TODO: empleado general deberá bloquearse esperando respuesta
        }), "c");

        fsm.registerLastState(new PizzaState(this, ResourcesManager.TIEMPO_CORTADO, () -> {
            System.out.println("Pizza en crescor");
            ACLMessage response = msg.createReply();
            response.setContent(MSG_EN_CRESCOR);
            send(response);
            // se desocupa empleado general
            Pizza p = new Pizza(order != null);
            p.setCliente(clientName);
            ResourcesManager.addPizza(p);
            if(promotor == null) return SUCCESS;
            ACLMessage listo = new ACLMessage(ACLMessage.INFORM);
            listo.setContent(clientName);
            listo.addReceiver(new AID(promotor, true));
            send(listo);
            this.doDelete();
            return SUCCESS;
        }), "d");

        fsm.registerDefaultTransition("a","a",new String[]{"a"});
        fsm.registerTransition("a", "b", 1);//si regresa uno es decir succes pasa de estado a b
        fsm.registerDefaultTransition("b", "c");
        //fsm.registerDefaultTransition("c", "a",new String[]{"a", "b", "c"});
        fsm.registerDefaultTransition("c", "d");
        addBehaviour(fsm);
        super.setup();
    }
    public String getOrderName(){return order;}
}
