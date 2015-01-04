
public class Instruction {
	enum Type {ADDR, FLOAT, INTEGER};
	public static final int OPERAND_COUNT = 3;
	public int[] logicalIdx;
    public int[]  physicalIdx;
    public int  ALTag;
    //instruction type
    public char intr_type;
    public boolean isDone;
    public float result;
    public int oldDest;
    public Instruction() {
        logicalIdx = new int[OPERAND_COUNT];
        physicalIdx = new int[OPERAND_COUNT];
        ALTag = -1;
    }
    
    Type getType() {
        switch (intr_type) {
            case 'M':
            case 'A':
                return Type.FLOAT;
            case 'L':
            case 'S':
                return Type.ADDR;
            case 'I':
                return Type.INTEGER;
        }
        return Type.INTEGER;
    }
}
