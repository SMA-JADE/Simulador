package agents;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
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
    private String clientName = null, order = null;

    @Override
    protected void setup() {
        FSMBehaviour fsm = new FSMBehaviour(this);

        //crear agente con nombre del cliente y orden
        Object args[] = getArguments();
        if(args.length > 0) order = (String)args[0];
        if(args.length > 1) clientName = (String)args[0];

        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(EGeneralAgent.SERVICE);
        template.addServices(sd);


        fsm.registerFirstState(new PizzaState(this, 0, () -> {
            System.out.println("esperando...");
            try {
                DFAgentDescription[] result = DFService.search(this, template);
                if(result.length == 0) return FAIL;
                ACLMessage m = new ACLMessage(ACLMessage.REQUEST);
                m.addReceiver(result[0].getName());
                m.setContent(MSG_ENTRA_ORDEN);
                send(m);
                msg = blockingReceive();
                return msg.getContent().equals(REPLY_ENTRA_ORDEN) ? SUCCESS : FAIL;
                //TODO: empleado general deberá bloquearse esperando respuesta
            } catch (FIPAException e) {
                e.printStackTrace();
            }
            return FAIL;
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
            try {
                DFAgentDescription[] result = new DFAgentDescription[0];
                long t = System.currentTimeMillis(), now = t;
                while(result.length == 0 && now-t < ResourcesManager.TIEMPO_QUEMADO) {
                    result = DFService.search(this, template);
                    now = System.currentTimeMillis();
                }
                if(now-t < ResourcesManager.TIEMPO_QUEMADO)
                    return FAIL;
                ACLMessage m = new ACLMessage(ACLMessage.REQUEST);
                m.addReceiver(result[0].getName());
                m.setContent(MSG_SACAR_HORNO);
                send(m);
                msg = blockingReceive(ResourcesManager.TIEMPO_QUEMADO);
                return msg.getContent().equals(REPLY_SACANDO_HORNO) ? SUCCESS : FAIL;
            } catch (FIPAException e) {
                e.printStackTrace();
            }
            return FAIL;
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

