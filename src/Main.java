import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        new Main().run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which is going to be the quantum for this processor?");
        int quantum = scanner.nextInt();
        /* La memoria de instrucciones tiene espacio para 40 bloques de 4 instrucciones cada uno (Intruccion = 4 enteros) */
        ArrayList<Integer>instructionsMemory = new ArrayList<Integer>(640);
        InstructionsLoader.loadInstructions(instructionsMemory);
        new Processor(quantum, instructionsMemory).start();
    }
}
