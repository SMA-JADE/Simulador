package util;

import agents.OrderAgent;
import jade.wrapper.AgentController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * Created by ernesto on 2/05/16.
 */
public class ResourcesManager {
    public static final long TIEMPO_VESTIDO = 2000;
    public static final long TIEMPO_HORNO = 5000;
    public static final long TIEMPO_CORTADO = 1000;
    public static final long TIEMPO_CADUCIDAD = 30000;

    private static boolean accesingClients = false;
    private static boolean accesingPizzas = false;
    private static boolean accesingOrdenes = false;
    private static boolean accesingHorno = false;

    public static int wave;

    private static ArrayList<AgentController> clients = new ArrayList<>();
    private static ArrayList<Pizza> pizzas = new ArrayList<>();
    private static ArrayList<OrderAgent> ordenes = new ArrayList<>();
    private static ArrayList<OrderAgent> horno = new ArrayList<>();
    public static ArrayList<Integer> numNormalClients = new ArrayList<>(), numSpecialClients = new ArrayList<>();

    public static void setClientsType(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                String[] everything = line.split(" ");
                numNormalClients.add(parseInt(everything[0]));
                numSpecialClients.add(parseInt(everything[1]));
                line = br.readLine();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
    }


    // Añadiendo de forma elegante
    public static void addClient(AgentController c){
        clients.add(c);
    }
    // Removiendo siempre el primero de la lista. Usando Queue es un show más extenso xD
    public static void removeClient(){
        clients.remove(0);
    }

    public static AgentController getClient(){
        AgentController a;
        //TODO: tiempos oscuros de desesperacion, no se si sirva si lo quitas
        while(accesingClients) {}

        accesingClients = true;
        a = clients.size() <= 0 ? null : clients.remove(0);
        accesingClients = false;
        return a;
    }

    public static  int getSizeClients(){return clients.size();}
    //El nombre habla por si solo 8)
    public static void removeAllClients(){
        clients.clear();
    }

    public static boolean noClients(){
        return clients.size() == 0;
    }

    public static void addPizza(Pizza pizza){
        pizzas.add(pizza);
    }

    public static Pizza popPizza(String clientName){
        if(clientName == null)
            return popPizza();
        while(accesingPizzas) {}
        accesingPizzas = true;
        Pizza p = null;
        for(int i=0; i<pizzas.size() ; ++i){
            if(clientName.equals(pizzas.get(i).getCliente())) {
                p = pizzas.remove(i);
            }
        }
        accesingPizzas = false;
        return p;
    }

    public static Pizza popPizza(){
        while(accesingPizzas) {}
        accesingPizzas = true;
        Pizza p = null;
        for(int i=0; i<pizzas.size(); ++i){
            if(!pizzas.get(i).isEspecial())
                p = pizzas.remove(i);
        }
        accesingPizzas = false;
        return p;
    }

    public static boolean noPizzas(){
        return pizzas.size() == 0;
    }

    public static void addOrder(OrderAgent e){
        ordenes.add(e);
    }

    public static OrderAgent removeOrder(){
        while(accesingOrdenes) {}
        accesingOrdenes = true;
        OrderAgent o = ordenes.size() <= 0 ? null : ordenes.remove(0);
        accesingOrdenes = false;
        return o;
    }

    public static boolean noOrders(){
        return ordenes.size() == 0;
    }

    public static int getPizzasLength() { return pizzas.size(); }

    public static void addToHorno(OrderAgent o){
        horno.add(o);
    }

    public static OrderAgent removeFromHorno(){
        while(accesingHorno) {}
        accesingHorno = true;
        OrderAgent o = horno.size() <= 0 ? null : horno.remove(0);
        accesingHorno = false;
        return o;
    }

    public static boolean noHorno() { return horno.isEmpty(); }

}
