public class FPALU extends ALU {
	private Instruction stage1_instr;
	private Instruction stage2_instr;
	private float default_value = 0;

	public void calc() {
		// pop out the instruction at the last stage,
		// only do that when the queue is full
		if (stage2_instr != null) {
			// update the register table
			float result = 0;
			float opr1 = stage2_instr.physicalIdx[0] == -1 ? default_value
					: reg_mgr.regValue[stage2_instr.physicalIdx[0]];
			float opr2 = stage2_instr.physicalIdx[1] == -1 ? default_value
					: reg_mgr.regValue[stage2_instr.physicalIdx[1]];
			switch (type) {
			case ADD:
				result = opr1 + opr2;
				break;
			case MULTIPLY:
				result = opr1 * opr2;
				break;
			default:
				break;
			}
			stage2_instr.result = result;
		}
		stage2_instr = stage1_instr;
		// push in a new instruction
		stage1_instr = upstream_queue.deliverInstruction();
		if (stage1_instr != null) {
			if(stage1_instr.idx == 15) {
				System.out.println();
			}
			logger.addLog(stage1_instr, 'E');
		}
	}

	public void edge() {
		if (stage2_instr != null)
			reg_mgr.push(stage2_instr);
	}

	public boolean isEmpty() {
		return stage1_instr == null && stage2_instr == null;
	}

	public FPALU(Logger logger, InstructionQueue instr_queue,
			RegisterManager reg_mgr, ALU.ALUType type) {
		super(logger, instr_queue, reg_mgr);
		this.type = type;
	}
}
