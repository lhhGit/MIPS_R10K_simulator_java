import java.util.ArrayDeque;
import java.util.Queue;

public class RegisterManager extends Widget {
	private static final int LOGICAL_COUNT = 32;
	private static final int PHYSICAL_COUNT = 64;

	public int[] physicalReg; // mapping from logical register to physical, 32
								// entries
	public float[] regValue; // the values in 64 registers
	boolean[] isBusy; // Indicator whether each physical register contains a
						// valid value
	Queue<Integer> freelist; // maintains a free list of available physical
								// registers.

	RegisterManager(Logger logger, Widget commiter, char stage) {
		super(logger);
		this.next = commiter;
		this.stage = stage;
		physicalReg = new int[LOGICAL_COUNT];
		regValue = new float[PHYSICAL_COUNT];
		isBusy = new boolean[PHYSICAL_COUNT];
		freelist = new ArrayDeque<Integer>();
		for (int i = 0; i < PHYSICAL_COUNT; i++)
			freelist.add(i);
		for (int i = 0; i < LOGICAL_COUNT; i++) {
			isBusy[i] = false;
			physicalReg[i] = -1;
		}
	}

	@Override
	public void calc() {
		while (!pending_queue.isEmpty()) {
			Instruction instr = pending_queue.poll();
			markDone(instr);
			logger.addLog(instr, stage);
		}
	}
	
	public void markDone(Instruction instr) {
		if (instr.physicalIdx[2] >= 0) {
			regValue[instr.physicalIdx[2]] = instr.result;
			isBusy[instr.physicalIdx[2]] = false;
		}
		instr.isDone = true;
		if (instr.oldDest > 0) {
			freelist.add(instr.oldDest);
		}
	}

	@Override
	public boolean isEmpty() {
		return true;
	}
}
