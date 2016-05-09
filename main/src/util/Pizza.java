package util;

import java.util.Date;

/**
 * Created by ernesto on 6/05/16.
 */
public class Pizza {
    private boolean especial;
    private Date hora_preparacion;

    public Pizza(boolean especial) {
        this.hora_preparacion = new Date();
        this.especial = especial;
    }

    public boolean estaCaducada(){
        return new Date().getTime() - hora_preparacion.getTime()
                > ResourcesManager.TIEMPO_CADUCIDAD;
    }

}
