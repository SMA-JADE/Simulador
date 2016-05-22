package util;

import java.util.Date;

/**
 * Created by ernesto on 6/05/16.
 */
public class Pizza {
    private boolean especial;
    private Date hora_preparacion;
    private String nombre_cliente;

    public Pizza(boolean especial) {
        this.hora_preparacion = new Date();
        this.especial = especial;
    }

    public void setCliente(String nombre){
        nombre_cliente = nombre;
    }

    public boolean estaCaducada(){
        return new Date().getTime() - hora_preparacion.getTime()
                > ResourcesManager.TIEMPO_CADUCIDAD;
    }

    public String getCliente(){
        return nombre_cliente;
    }

    public boolean isEspecial() {
        return especial;
    }
}
