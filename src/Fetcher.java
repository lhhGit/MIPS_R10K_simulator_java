public class Fetcher extends Widget {
	private TraceFileReader reader;
	private int instr_count;

	public Fetcher(Logger logger, TraceFileReader reader, Widget next,
			int count, char stage) {
		super(logger, next, count, stage);
		this.reader = reader;
	}

	// calc
	void calc() {
		Instruction[] buf = new Instruction[count];
		instr_count = this.reader.read(count, buf);
		for (int i = 0; i < instr_count; i++) {
			pending_queue.add(buf[i]);
		}
		super.calc();
	}
}
