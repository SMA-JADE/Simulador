package agents;

import UI.MainFrame;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;
import util.ResourcesManager;

import javax.swing.*;

/**
 * Created by ernesto on 2/05/16.
 */
public class MasterAgent extends Agent {

    private JFrame jframe;

    @Override
    protected void setup() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        setupUI();

        try {
            DFService.register(this, dfd);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setupUI() {
        jframe = new MainFrame( this );

        jframe.setSize( 600, 200 );
        jframe.setLocation( 400, 400 );
        jframe.setVisible( true );
        jframe.validate();
    }

    public void createClients(int num_clients) {
        PlatformController container = getContainerController();

        try {
            for (int i = 0; i < num_clients; i++) {
                String localName = "client-" + i;
                AgentController client = container.createNewAgent(localName, "agents.ClientAgent", null);
                //client.start();
                ResourcesManager.addClient(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        ResourcesManager.removeAllClients();
    }

    public void terminateMaster() {
        try{
            if(ResourcesManager.noClients()){
                close();
            }
            DFService.deregister( this );
            jframe.dispose();
            doDelete();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
