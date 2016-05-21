package agents;

import UI.MainFrame;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
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

    public void createPromotores(int num_promotes){
        ((MainFrame) jframe).prog_rumourCount.setValue( 0 );
        PlatformController container = getContainerController();
        try {
            for (int i = 0; i < num_promotes; i++) {
                String localname = "promotor-" + i;
                AgentController promotor = container.createNewAgent(localname, "agents.PromotorAgent", null);
                promotor.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createEmpleados(int num_empleados){
        PlatformController container = getContainerController();
        try {
            for (int i = 0; i < num_empleados; i++) {
                String localname = "empleado-" + i;
                AgentController promotor = container.createNewAgent(localname, "agents.EGeneralAgent", null);
                promotor.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void close() {
        ResourcesManager.removeAllClients();
    }

    public void incremetBar(int clientesAt, int clientesNA) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                ((MainFrame) jframe).prog_rumourCount.setValue( Math.round( 100 * clientesAt/clientesNA) );
            }
        } );
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