public class Issuer extends Widget {
	private InstructionQueue fp_queue;
	private InstructionQueue addr_queue;
	private InstructionQueue integer_queue;
	private ActiveList aclist;
	private RegisterManager regmgr;

	public Issuer(Logger logger, InstructionQueue fp_queue,
			InstructionQueue addr_queue, InstructionQueue integer_queue,
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
			i++;
			pending_queue.poll();
			logger.addLog(instr, stage);
			aclist.append(instr);
			completed_queue.add(instr);
		}
	}

	@Override
	void edge() {
	}

}
