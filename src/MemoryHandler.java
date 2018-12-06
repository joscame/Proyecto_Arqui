import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MemoryHandler {
    public static ArrayList<Integer> instructionsMemory;
    public static ArrayList<Integer> dataMemory;

    static{
        /* La memoria de instrucciones tiene espacio para 40 bloques de 4 instrucciones cada uno (Intruccion = 4 enteros) */
        instructionsMemory = new ArrayList<Integer>(640);
        loadInstructions();

        dataMemory = new ArrayList<Integer>(96);
        initializeDataMemory();
    }

    public static void initializeDataMemory(){
        for (int i = 0; i < 96; i++) {
            dataMemory.add(0);
        }
    }

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
    public static boolean loadInstructions() {

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
