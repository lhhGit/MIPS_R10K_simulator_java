public class AddrALU extends ALU {
	private Instruction stage1_instr;
	private Instruction stage2_instr;
	private AddressQueue addr_queue;

	public void calc() {
		stage2_instr = stage1_instr;
		if (stage2_instr != null) {
			logger.addLog(stage2_instr, 'M');
			stage2_instr.isDone = true;
		}
		// push in a new instruction
		stage1_instr = addr_queue.deliverInstruction();
		if (stage1_instr != null) {
			stage1_instr.isAddrCalcuted = true;
			logger.addLog(stage1_instr, 'A');
		}
	}

	public void edge() {
	}

	public boolean isEmpty() {
		return stage1_instr == null && stage2_instr == null;
	}

	AddrALU(Logger logger, AddressQueue instr_queue, Committer committer, 
			RegisterManager reg_mgr) {
		super(logger);
		this.addr_queue = instr_queue;
		this.next = committer;
		this.reg_mgr = reg_mgr;
	}
}
