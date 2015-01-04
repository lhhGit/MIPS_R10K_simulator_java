import java.util.ArrayDeque;
import java.util.Queue;

public class Widget {
	// stores unexecuted instrs
	protected Queue<Instruction> pending_queue;
	// completed instrs;
	protected Queue<Instruction> completed_queue;
	protected Widget next;
	protected Logger logger;
	protected char stage;
	protected int count;

	public boolean isEmpty() {
		return pending_queue.isEmpty();
	}

	void calc() {
		while (!pending_queue.isEmpty()) {
			Instruction instr = pending_queue.poll();
			completed_queue.add(instr);
			logger.addLog(instr, stage);
		}
	}

	void edge() {
		int i=0; 
		while (!completed_queue.isEmpty() && i<count) {
			next.push(completed_queue.poll());
		}
	}

	// how many instructions can executed in one cycle
	public Widget(Logger logger, Widget next, int count, char stage) {
		this(logger);
		this.next = next;
		this.count = count;
		this.stage = stage;
	}

	Widget(Logger logger) {
		this.logger = logger;
		pending_queue = new ArrayDeque<Instruction>();
		completed_queue = new ArrayDeque<Instruction>();
	}

	void push(Instruction newInstr) {
		pending_queue.add(newInstr);
	}

}
