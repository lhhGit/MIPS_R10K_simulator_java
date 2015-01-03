public class ALU extends Widget {
	enum ALUType {
		ADD, MULTIPLY
	};

	protected InstructionQueue upstream_queue;
	protected RegisterManager reg_mgr;
	protected ALUType type;

	public ALU(Logger logger, InstructionQueue instr_queue,
			RegisterManager reg_mgr) {
		super(logger);
		this.upstream_queue = instr_queue;
		this.reg_mgr = reg_mgr;
	}

	ALU(Logger logger, InstructionQueue instr_queue) {
		super(logger);
		this.upstream_queue = instr_queue;
	}
};