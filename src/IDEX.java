public class IDEX {

    public int dir;
    public int npc;
    public int a;
    public int b;
    public int imm;
    public int RL;
    public IR ir;
    public boolean idBlocked;
    public boolean exBlocked;

    public IDEX(){
        this.dir = 0;
        this.npc = 0;
        this.a = 0;
        this.b = 0;
        this.imm = 0;
        this.RL = 0;
        this.ir = null;
        this.idBlocked = false;
        this.exBlocked = false;
    }
}
