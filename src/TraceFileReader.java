import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class TraceFileReader {
	private List<Instruction> instrs;
	int read_ptr;
	
	private void dummyLoad(){}
	
	TraceFileReader() {
		instrs = new ArrayList<Instruction>();
	}
	
	public int read(int count, Instruction[] buf) {
		int i = read_ptr;
	    while (i < instrs.size() && i - read_ptr < count) {
	        buf[i-read_ptr] = instrs.get(i);
	        i++;
	    }
	    int true_count = i - read_ptr;
	    read_ptr = i;
	    return true_count;
	}
	
	public void load(String filename) throws Exception {
		  BufferedReader reader = new BufferedReader(new FileReader(filename));
		  String line = null;
		  int idx = 0;
		  while ( (line=reader.readLine())!=null) {
			  String[] segments = line.split(" ");
			  Instruction instr = new Instruction();
	          instr.intr_type = segments[0].charAt(0);
	          if (instr.intr_type == 'M' || instr.intr_type == 'I' ||
	        		  instr.intr_type == 'A') {
		          for (int i=0; i<Instruction.OPERAND_COUNT; i++) {
		              instr.logicalIdx[i] = Integer.parseInt(segments[i+1]);
		          }
	          } else if (instr.intr_type == 'L') {
	        	  instr.logicalIdx[2] = Integer.parseInt(segments[1]);
	        	  instr.logicalIdx[0] = Integer.parseInt(segments[2]);
	          } else if (instr.intr_type == 'S') {
	        	  instr.logicalIdx[0] = Integer.parseInt(segments[1]);
	        	  instr.logicalIdx[1] = Integer.parseInt(segments[2]);
	          }
              instr.idx = idx;
	          instrs.add(instr);
	          idx++;
		  }
		  reader.close();
	}
}
