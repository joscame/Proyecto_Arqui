public class IFID {

    public int npc;
    public IR ir;
    public boolean ifBlocked;
    public boolean idBlocked;
    public boolean branchInProgress;
    public boolean idReady;

    public IFID(){
        this.npc = 0;
        this.ir = null;
        this.ifBlocked = false;
        this.idBlocked = false;
        this.branchInProgress = false;
        this.idReady = false;
    }
}
