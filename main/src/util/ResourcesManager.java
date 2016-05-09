package util;

import jade.wrapper.AgentController;

import java.util.ArrayList;

/**
 * Created by ernesto on 2/05/16.
 */
public class ResourcesManager {
    public static final long TIEMPO_VESTIDO = 2000;
    public static final long TIEMPO_HORNO = 5000;
    public static final long TIEMPO_CORTADO = 1000;
    public static final long TIEMPO_QUEMADO = 1000;
    public static final long TIEMPO_CADUCIDAD = 30000;

    private static ArrayList<AgentController> clients = new ArrayList<>();
    private static ArrayList<Pizza> pizzas = new ArrayList<>();

    // Añadiendo de forma elegante
    public static void addClient(AgentController c){
        clients.add(c);
    }
    // Removiendo siempre el primero de la lista. Usando Queue es un show más extenso xD
    public static void removeClient(){
        clients.remove(0);
    }

    public static AgentController getClient(){
        AgentController a =clients.get(0);
        removeClient();
        return a;
    }

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




}
