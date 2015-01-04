import java.util.HashSet;
import java.util.Set;

public class AddrALU extends ALU {
	private Set<Instruction> instrs;
	private AddressQueue addr_queue;
	
	public void calc() {	
		Instruction instr = addr_queue.deliverInstruction();
		if (instr!=null) {
			instrs.add(instr);
		}
		for (Instruction instruction :instrs) {
			if (isInstructionReady(instruction)) {
				instruction.isAddrCalcuted = true;
				logger.addLog(instruction, stage);
				instrs.remove(instruction);
				return;
			}
		}
	}

	public void edge() {
	}

	public boolean isEmpty() {
		return instrs.isEmpty();
	}
	
	private boolean isInstructionReady(Instruction instr) {
		if (instr.intr_type == 'L') {
			return instr.physicalIdx[0] < 0 || !reg_mgr.isBusy[instr.physicalIdx[0]];
		} else {
			for (int i = 0; i < 2; i++) {
				if (instr.physicalIdx[i] >= 0 &&
						reg_mgr.isBusy[instr.physicalIdx[i]]) {
					return false;
				}
			}
			return true;
		}
	}

	AddrALU(Logger logger, AddressQueue instr_queue, RegisterManager reg_mgr, char stage) {
		super(logger);
		this.addr_queue = instr_queue;
		this.reg_mgr = reg_mgr;
		this.stage = stage;
		instrs = new HashSet<Instruction>();
	}
}
