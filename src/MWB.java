public class MWB {

    public int aluOutput;
    public int lmd;
    public IR ir;
    public boolean memBlocked;
    public boolean wbBlocked;

    public MWB (){
        this.aluOutput = 0;
        this.lmd = 0;
        this.ir = null;
        this.memBlocked = false;
        this.wbBlocked = false;
    }
}
