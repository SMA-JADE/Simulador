package util;

import javax.swing.*;
import java.io.*;

/**
 * Created by Erick on 21/05/2016.
 */
public class Archivo {

    public static void guardar(String nameFile, String textContent){
        File f;
        FileWriter w;
        BufferedWriter bw;
        PrintWriter wr;
        try{
            f=new File(nameFile);
            w=new FileWriter(f, true);
            bw=new BufferedWriter(w);
            wr=new PrintWriter(bw);

            wr.write(textContent);

            wr.close();
            bw.close();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Se ha producido un error "+ e);
        }
    }
}
