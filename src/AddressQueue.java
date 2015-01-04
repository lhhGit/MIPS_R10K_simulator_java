import java.util.ArrayList;
import java.util.List;


public class AddressQueue extends Widget {
	
	private List<Instruction> instrs;
	private static final int CAPACITY = 16;
	public AddressQueue(Logger logger, RegisterManager reg_mgr) {
		super(logger);
		this.next = reg_mgr;
		instrs = new ArrayList<Instruction>();
		this.count = 1;
		stage = 'I';
	}

	public void addInstruction(Instruction instr) {
		instrs.add(instr);
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
	
	void removeHead() {
		if (!instrs.isEmpty()) {
			instrs.remove(0);
			System.out.println("addr queue size: "+instrs.size());
		}
	}
		
	public Instruction deliverInstruction() {
		for (int i=0; i<instrs.size(); i++) {
			Instruction instr = instrs.get(i);
			if (isInstructionReady(instrs.get(i)) 
					&& !instrs.get(i).isAddrCalcuted) {
				return instr;
			}
		}
		return null;
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
