import java.util.HashSet;
import java.util.Set;

public class InstructionQueue extends Widget{
	public Set<Instruction> instrs;
	private RegisterManager regmgr;
	private static final int CAPACITY = 16;
	private char stage;

	private boolean isInstructionReady(Instruction instr) {
		for (int i = 0; i < 2; i++) {
			if (instr.physicalIdx[i] >= 0 &&
					this.regmgr.isBusy[instr.physicalIdx[i]]) {
				return false;
			}
		}
		return true;
	}

	public InstructionQueue(Logger logger, RegisterManager regmgr) {
		super(logger);
		this.regmgr = regmgr;
		instrs = new HashSet<Instruction>();
		this.logger = logger;
		stage = 'I';
		this.count = 1;
	}

	public boolean isFull() {
		return instrs.size() == CAPACITY;
	}
	
	// for a decoded instruction, adding into the instruction queue and
	// active list, creates the mapping between logical and physical
	// registers
	public void addInstruction(Instruction instr) {
		instrs.add(instr);
		System.out.println(instr.idx +" add: " + instrs.size());
		//logger.addLog(instr, stage);
		// for the source registers, we don't assign the physical registers
		for (int i = 0; i < 2; i++) {
			instr.physicalIdx[i] = regmgr.physicalReg[instr.logicalIdx[i]];
		}

		// for the target register, we will create a new mapping,
		// we may run out of physical registers
		instr.physicalIdx[2] = regmgr.freelist.poll();
		// mark the register as busy
		regmgr.isBusy[instr.physicalIdx[2]] = true;
		regmgr.physicalReg[instr.logicalIdx[2]] = instr.physicalIdx[2];
	}

	// called by ALU to execute a ready instruction
	// and remove it from instruction queue
	// If multiple ready, which one should I choose?
	Instruction deliverInstruction() {
		if (logger.currentCycle == 32) {
			System.out.println();
		}
		for (Instruction instr : instrs) {
			if (isInstructionReady(instr)) {
				instrs.remove(instr);
				
				System.out.println(instr.idx+" remove: " + instrs.size());
				return instr;
			}
		}
		return null;
	}
}
