package util;

import agents.OrderAgent;
import jade.wrapper.AgentController;

import java.io.*;
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

    static boolean accesingClients = false;

    private static ArrayList<AgentController> clients = new ArrayList<>();
    private static ArrayList<Pizza> pizzas = new ArrayList<>();
    private static ArrayList<OrderAgent> ordenes = new ArrayList<>();
    private static ArrayList<OrderAgent> horno = new ArrayList<>();
    public static int numNormalClients, numSpecialClients;

    public static void setTypeClients(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String[] everything = sb.toString().trim().split(" ");
            numNormalClients = parseInt(everything[0]);
            numSpecialClients = parseInt(everything[1]);


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
        while(accesingClients);

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

    public static Pizza popPizza(){
        return pizzas.remove(0);
    }

    public static Pizza peekPizza(){
        return pizzas.get(0);
    }

    public static boolean noPizzas(){
        return pizzas.size() == 0;
    }

    public static void addOrder(OrderAgent e){
        ordenes.add(e);
    }

    public static OrderAgent removeOrder(){
        return ordenes.remove(0);
    }

    public static boolean noOrders(){
        return ordenes.size() == 0;
    }

    public static int getPizzasLength() { return pizzas.size(); }

    public static void addToHorno(OrderAgent o){
        horno.add(o);
    }

    public static OrderAgent removeFromHorno(){
        return horno.remove(0);
    }

    public static boolean noHorno() { return horno.isEmpty(); }

}
