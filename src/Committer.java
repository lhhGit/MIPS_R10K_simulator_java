public class Committer extends Widget {

	private ActiveList aclist;

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
		}
	}

	@Override
	public void edge() {

	}

	Committer(Logger logger, ActiveList aclist, int count, char stage) {
		super(logger);
		this.count = count;
		this.stage = stage;
		this.aclist = aclist;
	}
}
