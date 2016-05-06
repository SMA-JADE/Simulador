package util;

import jade.wrapper.AgentController;

import java.util.ArrayList;

/**
 * Created by ernesto on 2/05/16.
 */
public class ResourcesManager {
    private static ArrayList<AgentController> clients = new ArrayList<>();

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


}
