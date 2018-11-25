public class EXM {

    public int aluOutput;
    public int b;
    public IR ir;
    public boolean memBlocked;
    public boolean exBlocked;
    public boolean copyToMemory;

    public EXM (){
        this.aluOutput = 0;
        this.b = 0;
        this.ir = null;
        this.memBlocked = false;
        this.exBlocked = false;
        this.copyToMemory = false;
    }
}
