import java.util.ArrayList;

/**
 * Created by Me on 11/18/2018.
 */
public class IR {
    private ArrayList<Integer> instruction;

    public IR(ArrayList<Integer> instruction){
        this.instruction = new ArrayList<>(instruction);
    }

    public int getOperationCode(){
        return this.instruction.get(0);
    }

    public int getDestinyResgister(){
        return this.instruction.get(1);
    }

    public int getFirstSourceRegister(){
        return this.instruction.get(2);
    }

    public int getSecondSourceRegister(){
        return this.instruction.get(3);
    }

    public int getImmediate(){
        return this.instruction.get(3);
    }
}
