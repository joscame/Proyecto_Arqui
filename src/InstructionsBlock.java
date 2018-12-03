import java.util.ArrayList;

public class InstructionsBlock {
    public ArrayList<IR> words;
    public int blockId;

    public InstructionsBlock() {
        this.words = new ArrayList<>(4);
        this.blockId = -1;
    }

}

