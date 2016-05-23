package agents;

import UI.MainFrame;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;
import jade.wrapper.StaleProxyException;
import util.ResourcesManager;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by ernesto on 2/05/16.
 */
public class MasterAgent extends Agent {

    private JFrame jframe;

    public JFrame getFrame(){
        return jframe;
    }

    @Override
    protected void setup() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        try {
            ResourcesManager.setClientsType(System.getProperty("user.dir")+"/main/src/util/Clientes.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void createPromotores(int num_promotes){//En el MainFrame son creados los prmotores
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
            for (int i = 0; i < num_empleados/2; i++) {
                String localname = "horno-" + i;
                AgentController promotor = container.createNewAgent(localname, "agents.EGeneralAgent", new String[]{"Horno"});
                promotor.start();
            }
            for (int i = 0; i < (num_empleados+1)/2; i++) {
                String localname = "peperoni-" + i;
                AgentController promotor = container.createNewAgent(localname, "agents.EGeneralAgent", new String[]{"Peperoni"});
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

    public void createGerente() {
        AgentController promotor = null;
        try {
            promotor = getContainerController().createNewAgent("Gerente", "agents.Gerente", null);
            promotor.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}