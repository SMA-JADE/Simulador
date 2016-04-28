import agents.OrderAgent;

public class Main {

    /**
     * Aqui corremos jade.Boot con nuestros agentitos. En vez de mandar argumentos
     * por consola, los mandamos aqui y asi (R)
     * @param args console args
     */
    public static void main(String[] args) {
        jade.Boot.main(new String[]{
                "-gui",
                "Order:"+ OrderAgent.class.getName()
        });
    }
}