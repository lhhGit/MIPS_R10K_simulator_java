import java.io.PrintWriter;
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
	public int currentCycle = 0;
	private List<Instruction> instr_list;
	int max = 0;

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
		max = Math.max(max, currentCycle);
	}

	public void increment() {
		currentCycle++;
		System.out.println("cycle: "+currentCycle);
	}

	public void print(PrintWriter writer) {
		for (int i=0; i<11; i++)
			writer.print(" ");
		for (int i=0; i<=max; i++) {
			writer.print(i);
			writer.print("|");
		}
		writer.println();
		for (Instruction instr : instr_list) {
			Entry entry = instr_log.get(instr);
			writer.print(String.valueOf(instr.intr_type)+" ");
			for (int i=0; i<Instruction.OPERAND_COUNT; i++)
				writer.print(String.format("%02d ",instr.logicalIdx[i]));
		   	System.out.print(instr.idx+" ");
			int i = 0;
			while (!entry.pipeline_history.isEmpty()) {
				if (i == entry.pipeline_history.get(0).cycle) {
					writer.print(entry.pipeline_history.get(0).stage); 
					System.out.print(String.format("%s,%d ", String.valueOf(entry.pipeline_history.get(0).stage), 
							entry.pipeline_history.get(0).cycle));
					entry.pipeline_history.remove(0);
				} else {
					writer.print(" ");
				}
				writer.print("|");
				i++;
			}
			writer.println();
			System.out.println();
		}
		writer.close();
	}
}
