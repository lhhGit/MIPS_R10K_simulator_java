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

	public void addInstruction(Instruction instr) {
		instrs.add(instr);
		//logger.addLog(instr, stage);
		RegisterManager regmgr = (RegisterManager)next;
		// for the source registers, we don't assign the physical registers
		for (int i = 0; i < 2; i++) {
			if (instr.logicalIdx[i] == -1) continue;
			instr.physicalIdx[i] = regmgr.physicalReg[instr.logicalIdx[i]];
		}
		
		if (instr.intr_type == 'S') return;
		
		// for the target register, we will create a new mapping,
		// we may run out of physical registers
		instr.physicalIdx[2] = regmgr.freelist.poll();
		// mark the register as busy
		regmgr.isBusy[instr.physicalIdx[2]] = true;
		regmgr.physicalReg[instr.logicalIdx[2]] = instr.physicalIdx[2];
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
		if (instr.intr_type == 'L') {
			return instr.physicalIdx[0] < 0 || !regmgr.isBusy[instr.physicalIdx[0]];
		} else {
			for (int i = 0; i < 2; i++) {
				if (instr.physicalIdx[i] >= 0 &&
						regmgr.isBusy[instr.physicalIdx[i]]) {
					return false;
				}
			}
			return true;
		}
	}
	
	public boolean isFull() {
		return instrs.size() == CAPACITY;
	}
}
