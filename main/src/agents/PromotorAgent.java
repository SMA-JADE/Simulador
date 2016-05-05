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

/**
 *
 * @author Erick
 */
public class PromotorAgent extends Agent{
    public final static String ORDEN = "Pizza peperoni";
    protected void setup (){
        System.out.println("Agent "+getLocalName());
        addBehaviour( new CyclicBehaviour( this ) {
            public void action() {
                ACLMessage msg = receive();
                    if (msg != null) {
                        if (ORDEN.equals( msg.getContent() )) {
                            doWait(500);//Espera medio segundo
                            doWake();
                            ACLMessage mResp = msg.createReply();//respondemos la orden del cliente
                            mResp.setContent("Orden lista");
                            send(mResp);
                        }             
                    else {
                            block();
                        }
                    }
            }
        });
    }
}
