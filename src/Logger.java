import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ActionTimePair {
	public int cycle;
	public char stage;
};

class Entry {
	public List<ActionTimePair> pipeline_history;
	Entry() {
		pipeline_history = new ArrayList<ActionTimePair>();
	}
	void addAction(int cycle, char stage) {
		ActionTimePair atPair = new ActionTimePair();
		atPair.cycle = cycle;
		atPair.stage = stage;
		pipeline_history.add(atPair);
	}
};

class Logger {
	private Map<Instruction, Entry> instr_log;
	private int currentCycle = 0;
	private List<Instruction> instr_list;

	Logger() {
		instr_log = new HashMap<Instruction, Entry>();
		instr_list = new ArrayList<Instruction>();
	}
	public void addLog(Instruction instr, char stage) {
		if (!instr_log.containsKey(instr)) {
			Entry entry = new Entry();
			instr_log.put(instr, entry);
			instr_list.add(instr);
		}
		instr_log.get(instr).addAction(currentCycle, stage);
	}

	public void increment() {
		currentCycle++;
	}

	public void print() {
		for (Instruction instr : instr_list) {
			Entry entry = instr_log.get(instr);
			for (ActionTimePair at_pair : entry.pipeline_history) {
				String str = String.format("%s,%d ", String.valueOf(at_pair.stage), at_pair.cycle);
				System.out.print(str);
			}
			System.out.println();
		}
	}
}
