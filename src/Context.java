public class Context {
    public int id;
    public int[] registers;
    public int pc;
    public boolean isDone;

    public Context(int id, int pc){
        this.id = id;
        this.registers = new int[33];
        this.pc = pc;
        this.isDone = false;
    }
}
