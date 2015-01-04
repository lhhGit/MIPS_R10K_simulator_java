import java.util.ArrayDeque;
import java.util.Queue;


public class AddressQueue extends Widget {
	
	private Queue<Instruction> instrs;
	private static final int CAPACITY = 16;
	public AddressQueue(Logger logger, RegisterManager reg_mgr) {
		super(logger);
		this.next = reg_mgr;
		instrs = new ArrayDeque<Instruction>();
		this.count = 1;
		stage = 'I';
	}

	public void calc() {
		int i = 0; 
		while (!pending_queue.isEmpty() && i < count ) {
			addInstruction(pending_queue.poll());
			i++;
		}
	}
	
	public void addInstruction(Instruction instr) {
		instrs.add(instr);
		logger.addLog(instr, stage);
		RegisterManager regmgr = (RegisterManager)next;
		// for the source registers, we don't assign the physical registers
		instr.physicalIdx[1] = regmgr.physicalReg[instr.logicalIdx[1]];
		
		if (instr.intr_type == 'S') return;
		
		// for the target register, we will create a new mapping,
		// we may run out of physical registers
		instr.physicalIdx[0] = regmgr.freelist.poll();
		// mark the register as busy
		regmgr.isBusy[instr.physicalIdx[0]] = true;
		regmgr.physicalReg[instr.logicalIdx[0]] = instr.physicalIdx[0];
	}
		
	public Instruction deliverInstruction() {
		if (!instrs.isEmpty() && isInstructionReady(instrs.peek())) {
			return instrs.poll();
		} else {
			return null;
		}
	}
	
	private boolean isInstructionReady(Instruction instr) {	
		RegisterManager regmgr = (RegisterManager)next;
		return (instr.intr_type == 'L' && (instr.physicalIdx[1] < 0 || !regmgr.isBusy[instr.physicalIdx[1]])) 
				||
			   (instr.intr_type == 'S' && (instr.physicalIdx[0] < 0 || !regmgr.isBusy[instr.physicalIdx[0]]));
	}
	
	public boolean isFull() {
		return instrs.size() == CAPACITY;
	}
}
