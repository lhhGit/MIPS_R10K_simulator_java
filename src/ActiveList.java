import java.util.ArrayDeque;
import java.util.Queue;

public class ActiveList {
	private Queue<Instruction> myqueue;
	private static final int CAPACITY = 16;

	ActiveList() {
		myqueue = new ArrayDeque<Instruction>();
	}
	// only remove the head of queue when it is committed
	public Instruction removeHead() {
		return myqueue.poll();
	}

	public void append(Instruction instr) {
		myqueue.add(instr);
	}

	public Instruction getHead() {
		return myqueue.peek();
	}

	boolean isEmpty() {
		return myqueue.isEmpty();
	}

	boolean isFull() {
		return myqueue.size() == CAPACITY;
	}

}
