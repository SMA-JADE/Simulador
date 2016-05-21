package util;

import javax.swing.*;
import java.io.*;

/**
 * Created by Erick on 21/05/2016.
 */
public class Archivo {

    public void guardar(String nameFile){
        File f;
        FileWriter w;
        BufferedWriter bw;
        PrintWriter wr;
        try{
            f=new File(nameFile);
            w=new FileWriter(f);
            bw=new BufferedWriter(w);
            wr=new PrintWriter(bw);

            wr.write("prueba de escritura");

            wr.close();
            bw.close();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Se ha producido un error "+ e);
        }
    }
}
