/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

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

import java.util.Date;

/**
 * @author Erick
 */
public class PromotorAgent extends Agent {
    public final static String ORDEN = "Pizza peperoni";

    MessageTemplate clientTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
    MessageTemplate generalTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

    protected void setup() {
        System.out.println("Agent " + getLocalName());
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                //TODO: receive INFORM template
                ACLMessage msg = receive(generalTemplate);
                if(msg != null && msg.getPerformative() == ACLMessage.INFORM){
                    //empleado general termino la orden
                    Pizza p = ResourcesManager.popPizza();
                    String cliente = p.getCliente();
                    try {
                        AgentController client = getContainerController().getAgent(cliente);
                        client.activate();
                        ACLMessage response = new ACLMessage(ACLMessage.CONFIRM);
                        response.addReceiver(new AID(client.getName(), true));
                        response.setContent("hot n ready");
                        send(response);
                    } catch (ControllerException e) {
                        e.printStackTrace();
                    }
                }else if (!ResourcesManager.noClients()) {
                    AgentController a = ResourcesManager.getClient();
                    try {
                        a.start();
                        ACLMessage welcome = new ACLMessage(ACLMessage.INFORM);
                        welcome.setContent("holi, bienvenido");
                        welcome.setSender(new AID(a.getName(), false));
                        send(welcome);
                        msg = blockingReceive(clientTemplate);
                        ACLMessage mResp = msg.createReply();//respondemos la orden del cliente
                        if(msg.getContent() == null && !ResourcesManager.noPizzas()){
                            mResp.setContent("Orden lista");
                        }else{
                            mResp.setContent("Aguanta");
                            getContainerController().createNewAgent(
                                    "order"+new Date().toString(),
                                    OrderAgent.class.getName(),
                                    new String[]{ a.getName(), msg.getContent()});
                        }
                        send(mResp);
                    } catch (StaleProxyException e) {
                        e.printStackTrace();
                    }
                    //getContainerController().getAgent("cliente").activate();
                }
            }
        });
    }
}
