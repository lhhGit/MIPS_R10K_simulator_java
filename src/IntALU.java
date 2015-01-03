class IntALU extends ALU {
	private float default_value;
	private Instruction instr;

	@Override
	public void calc() {
		instr = upstream_queue.deliverInstruction();
		if (instr != null) {
			float result = 0;
			float opr1 = instr.physicalIdx[0] == -1 ? default_value
					: reg_mgr.regValue[instr.physicalIdx[0]];
			float opr2 = instr.physicalIdx[1] == -1 ? default_value
					: reg_mgr.regValue[instr.physicalIdx[1]];
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
			instr.result = result;
			logger.addLog(instr, 'E');
		}
	}

	@Override
	void edge() {
		if (instr != null)
			reg_mgr.push(instr);
	}

	@Override
	public boolean isEmpty() {
		return instr == null;
	}

	IntALU(Logger logger, InstructionQueue instr_queue, RegisterManager reg_mgr) {
		super(logger, instr_queue, reg_mgr);
	}
};
