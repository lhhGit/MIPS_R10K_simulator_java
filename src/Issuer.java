public class Issuer extends Widget {
	private InstructionQueue fp_queue;
	private AddressQueue addr_queue;
	private InstructionQueue integer_queue;
	private ActiveList aclist;
	private RegisterManager regmgr;

	public Issuer(Logger logger, InstructionQueue fp_queue,
			AddressQueue addr_queue, InstructionQueue integer_queue,
			ActiveList aclist, RegisterManager regmgr, int count, char stage) {
		super(logger);
		this.fp_queue = fp_queue;
		this.addr_queue = addr_queue;
		this.integer_queue = integer_queue;
		this.aclist = aclist;
		this.regmgr = regmgr;
		this.count = count;
		this.stage = stage;
	}

	@Override
	void calc() {
		// for each instruction, dispatch to according instruction queues
		// according to their type
		// haven't considered what to deal with the instructions that cannot be
		// issued.
		int i = 0;
		while (!pending_queue.isEmpty() && i < count) {
			Instruction instr = pending_queue.peek();
			if (aclist.isFull() || regmgr.freelist.isEmpty()) {
				return;
			}
			switch (instr.getType()) {
			case FLOAT:
				if (fp_queue.isFull()) {					
					System.out.println(instr.idx+ " full: " +16);
					return;
				} else {
					fp_queue.addInstruction(instr);
				}
				break;
			case INTEGER:
				if (integer_queue.isFull()) {
					return;
				} else {
					integer_queue.addInstruction(instr);
				}
				break;
			case ADDR:
				if (addr_queue.isFull()) {
					return;
				} else {
					addr_queue.addInstruction(instr);
				}
			}
			//System.out.println(fp_queue.instrs.size());
			i++;
			logger.addLog(instr, stage);
			pending_queue.poll();
			aclist.append(instr);
			if (instr.logicalIdx[2] >= 0)
				instr.oldDest = regmgr.physicalReg[instr.logicalIdx[2]];
		}
	}

	@Override
	void edge() {}

}
