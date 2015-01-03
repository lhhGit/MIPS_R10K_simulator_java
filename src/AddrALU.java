public class AddrALU extends ALU {
	private Instruction stage1_instr;
	private Instruction stage2_instr;

	public void calc() {
		stage2_instr = stage1_instr;
		if (stage2_instr != null) {
			logger.addLog(stage2_instr, 'M');
		}
		// push in a new instruction
		stage1_instr = upstream_queue.deliverInstruction();
		if (stage1_instr != null) {
			logger.addLog(stage1_instr, 'A');
		}

	}

	public void edge() {
		if (stage2_instr != null)
			reg_mgr.push(stage2_instr);
	}

	public boolean isEmpty() {
		return stage1_instr == null && stage2_instr == null;
	}

	AddrALU(Logger logger, InstructionQueue instr_queue, RegisterManager reg_mgr) {
		super(logger, instr_queue, reg_mgr);
	}
}
