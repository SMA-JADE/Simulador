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
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import util.ResourcesManager;

/**
 * @author Erick
 */
public class PromotorAgent extends Agent {
    public final static String ORDEN = "Pizza peperoni";

    protected void setup() {
        System.out.println("Agent " + getLocalName());
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                if (!ResourcesManager.noClients()) {
                    AgentController a = ResourcesManager.getClient();
                    try {
                        a.start();
                    } catch (StaleProxyException e) {
                        e.printStackTrace();
                    }
                    ACLMessage msg = blockingReceive();
                    if (msg != null) {
                        doWait(1000);//Espera para que el pedido quede (?
                        doWake();
                        ACLMessage mResp = msg.createReply();//respondemos la orden del cliente
                        mResp.setContent("Orden lista");
                        send(mResp);
                    }
                }
            }
        });
    }
}
