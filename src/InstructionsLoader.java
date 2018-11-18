import javax.swing.text.html.HTMLDocument;
import java.io.BufferedReader;
import java.io.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class InstructionsLoader {

    private final String hilillo0 = "hilillo_0.txt";
    private final String hilillo1 = "hilillo_1";
    private final String hilillo2 = "hilillo_2";
    private final String hilillo3 = "hilillo_3";
    private final String hilillo4 = "hilillo_4";
    private final String hilillo5 = "hilillo_5";
    private final String hilillo6 = "hilillo_6";

    /**
     * Carga los archivos de hilillos en la memoria de instrucciones
     *
     * Parametros:
     *      instructionsMemory: Memoria de instrucciones que quiere ser llenada
     *
     * retorna:
     *      true si se pudieron cargar los archivos
     *      false si no se pudieron cargar
     */
    public static boolean loadInstructions(ArrayList<Integer> instructionsMemory) {

        String path = "assets";
        Scanner s0;

        File f = new File(path);

        if (f.isDirectory()) {

            File[] arr_cont = f.listFiles();
            int size = arr_cont.length;

            for (int i = 0; i < size; i++) {

                if (arr_cont[i].isFile()) {
                    try {
                        s0 = new Scanner(new File(String.valueOf(arr_cont[i])));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return false;
                    }

                    while (s0.hasNext()) {
                        instructionsMemory.add(s0.nextInt());
                    }
                    s0.close();
                }
            }
            /*Imprimir memoria de instrucciones
            Iterator<Integer> it = instructionsMemory.iterator();
            while (it.hasNext()) {
                int num = it.next();
                System.out.println(num);
            }
            */
        } else {
            System.err.println("El path NO es válido!");
            return false;
        }
        return true;
    }
 }