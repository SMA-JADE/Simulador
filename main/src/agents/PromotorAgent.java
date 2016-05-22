/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import UI.MainFrame;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import util.Pizza;
import util.ResourcesManager;

import javax.swing.*;
import java.util.Date;

/**
 * @author Erick
 */
public class PromotorAgent extends Agent {
    public final static String ORDEN = "Pizza peperoni";
    public final static String WAIT = "Aguanta";


    MessageTemplate clientTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
    MessageTemplate generalTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

    public static int clientesA=0;
    public static int clientesF= ResourcesManager.getSizeClients();
    //El promotor debe tener quien lo creo para acceder a los metodos y modificar la progres bar

    protected void setup() {
        System.out.println("Agent " + getLocalName());
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                //TODO: receive INFORM template
                ACLMessage msg = receive(generalTemplate);
                if(msg != null){
                    System.out.println(getLocalName() + ": hot n ready baby");
                    //empleado general termino la orden
                    Pizza p = ResourcesManager.popPizza(msg.getContent());
                    String cliente = p.getCliente();
                    try {
                        AgentController client = getContainerController().getAgent(cliente, true);
                        ACLMessage response = new ACLMessage(ACLMessage.CONFIRM);
                        response.addReceiver(new AID(client.getName(), true));
                        response.setContent("hot n ready");
                        send(response);
                    } catch (ControllerException e) {
                        e.printStackTrace();
                    }
                }else{

                    AgentController a = ResourcesManager.getClient();
                    if(a == null) return;
                    try {
                        System.out.println(getLocalName() + ": atendiendo a " + a.getName());
                        ACLMessage welcome = new ACLMessage(ACLMessage.INFORM);
                        welcome.setContent("holi, bienvenido");
                        welcome.addReceiver(new AID(a.getName(), true));
                        send(welcome);
                        MessageTemplate tmplt = MessageTemplate.and(clientTemplate,
                                MessageTemplate.MatchSender(new AID(a.getName(), true)));
                        System.out.println(getLocalName() + ": bloqueo paternal bloqueo bloque bloqueo");
                        msg = blockingReceive(tmplt);
                        System.out.println(getLocalName() + ": desbloqueado");
                        ACLMessage mResp = msg.createReply();//respondemos la orden del cliente
                        if(msg.getContent() == null && ResourcesManager.popPizza() != null){
                            mResp.setContent("Orden lista");
                        }else{
                            mResp.setContent(WAIT);
                            getContainerController().createNewAgent(
                                    "order-"+a.getName(),
                                    OrderAgent.class.getName(),
                                    new String[]{ a.getName(), msg.getContent(), getName()}).start();
                        }
                        send(mResp);
                        clientesA++;
                        //incremetBar();
                    } catch (StaleProxyException e) {
                        e.printStackTrace();
                    }
                    //getContainerController().getAgent("cliente").activate();
                }
            }
        });
    }
        public void incremetBar() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //((MainFrame) jframe).prog_rumourCount.setValue( Math.round( 100 * 4/10) );
            }
        } );
    }

}
