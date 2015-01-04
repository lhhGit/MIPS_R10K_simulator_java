public class Committer extends Widget {

	private ActiveList aclist;
	private AddressQueue addr_queue;
	private RegisterManager reg_mgr;

	@Override
	public boolean isEmpty() {
		return aclist.isEmpty();
	}

	@Override
	public void calc() {
		int i = 0;
		while (!aclist.isEmpty() && i < count) {
			Instruction instr = aclist.getHead();
			if (!instr.isDone)
				break;
			logger.addLog(instr, stage);
			i++;
			aclist.removeHead();
			if (instr.getType() == Instruction.Type.ADDR) {
				addr_queue.removeHead();
				reg_mgr.markDone(instr);
			}
		}
	}

	@Override
	public void edge() {

	}

	Committer(Logger logger, ActiveList aclist,int count, char stage) {
		super(logger);
		this.count = count;
		this.stage = stage;
		this.aclist = aclist;
	}
	
	void setAddressQueue(AddressQueue addr_queue) {
		this.addr_queue = addr_queue;
	}
	
	void setRegisterManager(RegisterManager reg_mgr) {
		this.reg_mgr = reg_mgr;
	}
}
