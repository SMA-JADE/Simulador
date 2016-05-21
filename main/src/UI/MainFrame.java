package UI;

import agents.MasterAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import util.ClientsWave;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import static agents.MasterAgent.*;


public class MainFrame extends JFrame{
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel pnl_main = new JPanel();
    JButton btn_Exit = new JButton();
    Component component3;
    JButton btn_stop = new JButton();
    Component component2;
    JButton btn_start = new JButton();
    Box box_buttons;
    JPanel pnl_num_clients = new JPanel();
    BorderLayout borderLayout3 = new BorderLayout();
    JLabel lbl_numClients = new JLabel();
    JLabel lbl_numEmpleados = new JLabel();
    JLabel lbl_numPromo = new JLabel();
    Box box_numClients;
    JLabel lbl_clientsCount = new JLabel();
    JLabel lbl_promoCount = new JLabel();
    JLabel lbl_empleaCount = new JLabel();
    JSlider slide_numClients = new JSlider();
    JSlider slide_numPromotores = new JSlider();
    JSlider slide_numEmpleados = new JSlider();
    Component component1;
    Component component4;
    GridLayout gridLayout1 = new GridLayout();
    JLabel lbl_numIntroductions = new JLabel();
    JLabel jLabel4 = new JLabel();
    JLabel lbl_partyState = new JLabel();
    Box box1;
    public JProgressBar prog_rumourCount = new JProgressBar();
    Component component6;
    Component component5;
    JLabel lbl_rumourAvg = new JLabel();


    protected MasterAgent m_owner;


    // Constructors
    //////////////////////////////////

    public MainFrame( MasterAgent owner ) {
        try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        m_owner = owner;
    }

    private void jbInit() throws Exception {
        component3 = Box.createHorizontalStrut(10);
        component2 = Box.createHorizontalStrut(5);
        box_buttons = Box.createHorizontalBox();

        box_numClients = Box.createHorizontalBox();
        component1 = Box.createGlue();
        component4 = Box.createHorizontalStrut(5);
        box1 = Box.createVerticalBox();
        component6 = Box.createGlue();
        component5 = Box.createGlue();
        this.getContentPane().setLayout(borderLayout1);
        pnl_main.setLayout(gridLayout1);
        btn_Exit.setText("Exit");
        btn_Exit.addActionListener(e -> btn_Exit_actionPerformed(e));
        btn_stop.setEnabled(false);
        btn_stop.setText("Stop");
        btn_stop.addActionListener(e -> btn_stop_actionPerformed(e));
        btn_start.setText("Start");
        btn_start.addActionListener(e -> btn_start_actionPerformed(e));
        this.setTitle("Lilur sisar Simulation");
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                this_windowClosing(e);
            }
        });
        pnl_num_clients.setLayout(borderLayout3);
        lbl_numClients.setText("Clientes");
        lbl_numPromo.setText(("Promotes"));
        lbl_numEmpleados.setText("Empleados");
        lbl_clientsCount.setMaximumSize(new Dimension(30, 17));
        lbl_clientsCount.setMinimumSize(new Dimension(30, 17));
        lbl_clientsCount.setPreferredSize(new Dimension(30, 17));
        lbl_clientsCount.setText("10");
        lbl_promoCount.setText("4");
        lbl_promoCount.setMaximumSize(new Dimension(30, 17));
        lbl_promoCount.setMinimumSize(new Dimension(30, 17));
        lbl_promoCount.setPreferredSize(new Dimension(30, 17));
        lbl_empleaCount.setText("4");
        slide_numClients.setValue(10);
        slide_numClients.setMaximum(1000);
        slide_numClients.addChangeListener(e -> slide_numGuests_stateChanged(e));
        slide_numPromotores.setValue(4);
        slide_numPromotores.setMaximum(10);
        slide_numPromotores.addChangeListener(e -> slide_numPromotores_stateChanged(e));
        slide_numEmpleados.setValue(4);
        slide_numEmpleados.setMaximum(10);
        slide_numEmpleados.addChangeListener(e -> slide_numEmpleados_stateChanged(e));
        gridLayout1.setRows(4);
        gridLayout1.setColumns(2);
        lbl_numIntroductions.setBackground(Color.white);
        lbl_numIntroductions.setText("0");
        jLabel4.setToolTipText("");
        jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel4.setText("Clients attended: ");
        lbl_partyState.setBackground(Color.white);
        lbl_partyState.setText("Not started");
        prog_rumourCount.setForeground(new Color(0, 255, 128));
        prog_rumourCount.setStringPainted(true);
        lbl_rumourAvg.setToolTipText("");
        lbl_rumourAvg.setText("0.0");
        this.getContentPane().add(pnl_main, BorderLayout.CENTER);
        //pnl_main.add(jLabel1, null);
        pnl_main.add(lbl_partyState, null);
        //pnl_main.add(jLabel2, null);
        pnl_main.add(lbl_numIntroductions, null);
        pnl_main.add(jLabel4, null);
        pnl_main.add(box1, null);
        box1.add(component5, null);
        box1.add(prog_rumourCount, null);
        box1.add(component6, null);
        //pnl_main.add(jLabel3, null);
        pnl_main.add(lbl_rumourAvg, null);
        this.getContentPane().add(pnl_num_clients, BorderLayout.NORTH);
        pnl_num_clients.add(box_numClients, BorderLayout.CENTER);
        pnl_num_clients.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );

        box_numClients.add(lbl_numClients, null);
        box_numClients.add(slide_numClients, null);
        box_numClients.add(lbl_clientsCount, null);
        box_numClients.add(lbl_numPromo,null);
        box_numClients.add(slide_numPromotores, null);
        box_numClients.add(lbl_promoCount, null);
        box_numClients.add(lbl_numEmpleados, null);
        box_numClients.add(slide_numEmpleados, null);
        box_numClients.add(lbl_empleaCount, null);
        this.getContentPane().add(box_buttons, BorderLayout.SOUTH);
        box_buttons.add(component2, null);
        box_buttons.add(btn_start, null);
        box_buttons.add(component3, null);
        box_buttons.add(btn_stop, null);
        box_buttons.add(component1, null);
        box_buttons.add(btn_Exit, null);
        box_buttons.add(component4, null);
        lbl_partyState.setForeground( Color.black );
        lbl_numIntroductions.setForeground( Color.black );
        lbl_rumourAvg.setForeground( Color.black );
    }

    void slide_numGuests_stateChanged(ChangeEvent e) {
        lbl_clientsCount.setText( Integer.toString( slide_numClients.getValue() ) );
    }

    void slide_numPromotores_stateChanged(ChangeEvent e){
        lbl_promoCount.setText(Integer.toString(slide_numPromotores.getValue()));
    }

    void slide_numEmpleados_stateChanged(ChangeEvent e){
        lbl_empleaCount.setText(Integer.toString(slide_numEmpleados.getValue()));
    }

    void btn_start_actionPerformed(ActionEvent e) {
        enableControls( true );

        // add a behaviour to the host to start the conversation going
        m_owner.addBehaviour( new OneShotBehaviour() {
            public void action() {
                //((MasterAgent) myAgent).createClients(slide_numClients.getValue());
                SequentialBehaviour b = new SequentialBehaviour();
                b.addSubBehaviour(new ClientsWave(myAgent, 2000, slide_numClients.getValue()/10*2,"w1"));
                b.addSubBehaviour(new ClientsWave(myAgent, 500, slide_numClients.getValue()/10*5,"w2"));
                b.addSubBehaviour(new ClientsWave(myAgent, 1000, slide_numClients.getValue()/10*3,"w3"));
                myAgent.addBehaviour(b);

                ((MasterAgent) myAgent).createPromotores(slide_numPromotores.getValue());
                ((MasterAgent) myAgent).createEmpleados(slide_numEmpleados.getValue());

            }
        } );
    }

    void btn_stop_actionPerformed(ActionEvent e) {
        enableControls( false );

        // add a behaviour to the host to end the party
        m_owner.addBehaviour( new OneShotBehaviour() {
            public void action() {
                ((MasterAgent) myAgent).close();
            }
        } );
    }

    void enableControls( boolean starting ) {
        btn_start.setEnabled( !starting );
        btn_stop.setEnabled( starting );
        slide_numClients.setEnabled( !starting );
        btn_Exit.setEnabled( !starting );
    }

    void btn_Exit_actionPerformed(ActionEvent e) {
        m_owner.addBehaviour( new OneShotBehaviour() {
            public void action() {
                ((MasterAgent) myAgent).terminateMaster();
            }
        } );
    }

    void this_windowClosing(WindowEvent e) {
        // simulate the user having clicked exit
        btn_Exit_actionPerformed( null );
    }


}